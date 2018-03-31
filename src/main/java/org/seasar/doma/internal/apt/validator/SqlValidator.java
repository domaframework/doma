package org.seasar.doma.internal.apt.validator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.LinkedHashMap;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.HolderCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.decl.TypeDeclaration;
import org.seasar.doma.internal.expr.ExpressionException;
import org.seasar.doma.internal.expr.ExpressionParser;
import org.seasar.doma.internal.expr.node.ExpressionNode;
import org.seasar.doma.internal.jdbc.sql.SimpleSqlNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.BindVariableNode;
import org.seasar.doma.internal.jdbc.sql.node.ElseifNode;
import org.seasar.doma.internal.jdbc.sql.node.EmbeddedVariableNode;
import org.seasar.doma.internal.jdbc.sql.node.ExpandNode;
import org.seasar.doma.internal.jdbc.sql.node.ForBlockNode;
import org.seasar.doma.internal.jdbc.sql.node.ForNode;
import org.seasar.doma.internal.jdbc.sql.node.IfNode;
import org.seasar.doma.internal.jdbc.sql.node.LiteralVariableNode;
import org.seasar.doma.internal.jdbc.sql.node.PopulateNode;
import org.seasar.doma.internal.jdbc.sql.node.SqlLocation;
import org.seasar.doma.internal.jdbc.sql.node.ValueNode;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.message.Message;

public class SqlValidator extends SimpleSqlNodeVisitor<Void, Void> {

  protected static final int SQL_MAX_LENGTH = 5000;

  protected final Context ctx;

  protected final ExecutableElement methodElement;

  protected final LinkedHashMap<String, TypeMirror> parameterTypeMap;

  protected final String path;

  protected final boolean expandable;

  protected final boolean populatable;

  protected final ExpressionValidator expressionValidator;

  public SqlValidator(
      Context ctx,
      ExecutableElement methodElement,
      LinkedHashMap<String, TypeMirror> parameterTypeMap,
      String path,
      boolean expandable,
      boolean populatable) {
    assertNotNull(ctx, methodElement, parameterTypeMap, path);
    this.ctx = ctx;
    this.methodElement = methodElement;
    this.parameterTypeMap = parameterTypeMap;
    this.path = path;
    this.expandable = expandable;
    this.populatable = populatable;
    expressionValidator = new ExpressionValidator(ctx, methodElement, parameterTypeMap);
  }

  public void validate(SqlNode sqlNode) {
    try {
      sqlNode.accept(this, null);
      var validatedParameterNames = expressionValidator.getValidatedParameterNames();
      for (var parameterName : parameterTypeMap.keySet()) {
        if (!validatedParameterNames.contains(parameterName)) {
          for (VariableElement parameterElement : methodElement.getParameters()) {
            if (parameterElement.getSimpleName().contentEquals(parameterName)) {
              ctx.getNotifier()
                  .send(
                      Kind.ERROR,
                      Message.DOMA4122,
                      parameterElement,
                      new Object[] {path, parameterName});
            }
          }
        }
      }
    } catch (AptException e) {
      ctx.getNotifier().send(e);
    }
  }

  @Override
  public Void visitBindVariableNode(BindVariableNode node, Void p) {
    return visitValueNode(node, p);
  }

  @Override
  public Void visitLiteralVariableNode(LiteralVariableNode node, Void p) {
    return visitValueNode(node, p);
  }

  protected Void visitValueNode(ValueNode node, Void p) {
    var location = node.getLocation();
    var variableName = node.getVariableName();
    var typeDeclaration = validateExpressionVariable(location, variableName);
    if (node.getWordNode() != null) {
      if (!isScalar(typeDeclaration)) {
        var sql = getSql(location);
        throw new AptException(
            Message.DOMA4153,
            methodElement,
            new Object[] {
              path,
              sql,
              location.getLineNumber(),
              location.getPosition(),
              variableName,
              typeDeclaration.getBinaryName()
            });
      }
    } else {
      if (!isScalarIterable(typeDeclaration)) {
        var sql = getSql(location);
        throw new AptException(
            Message.DOMA4161,
            methodElement,
            new Object[] {
              path,
              sql,
              location.getLineNumber(),
              location.getPosition(),
              variableName,
              typeDeclaration.getBinaryName()
            });
      }
    }
    visitNode(node, p);
    return null;
  }

  protected boolean isScalar(TypeDeclaration typeDeclaration) {
    var typeMirror = typeDeclaration.getType();
    return ctx.getCtTypes().newBasicCtType(typeMirror) != null
        || ctx.getCtTypes().newHolderCtType(typeMirror) != null;
  }

  protected boolean isScalarIterable(TypeDeclaration typeDeclaration) {
    var typeMirror = typeDeclaration.getType();
    var iterableCtType = ctx.getCtTypes().newIterableCtType(typeMirror);
    if (iterableCtType != null) {
      return iterableCtType
          .getElementCtType()
          .accept(
              new SimpleCtTypeVisitor<Boolean, Void, RuntimeException>(false) {

                @Override
                public Boolean visitBasicCtType(BasicCtType ctType, Void p)
                    throws RuntimeException {
                  return true;
                }

                @Override
                public Boolean visitHolderCtType(HolderCtType ctType, Void p)
                    throws RuntimeException {
                  return true;
                }
              },
              null);
    }
    return false;
  }

  @Override
  public Void visitEmbeddedVariableNode(EmbeddedVariableNode node, Void p) {
    var location = node.getLocation();
    var variableName = node.getVariableName();
    validateExpressionVariable(location, variableName);
    visitNode(node, p);
    return null;
  }

  @Override
  public Void visitIfNode(IfNode node, Void p) {
    var location = node.getLocation();
    var expression = node.getExpression();
    var typeDeclaration = validateExpressionVariable(location, expression);
    if (!typeDeclaration.isBooleanType()) {
      var sql = getSql(location);
      throw new AptException(
          Message.DOMA4140,
          methodElement,
          new Object[] {
            path,
            sql,
            location.getLineNumber(),
            location.getPosition(),
            expression,
            typeDeclaration.getBinaryName()
          });
    }
    visitNode(node, p);
    return null;
  }

  @Override
  public Void visitElseifNode(ElseifNode node, Void p) {
    var location = node.getLocation();
    var expression = node.getExpression();
    var typeDeclaration = validateExpressionVariable(location, expression);
    if (!typeDeclaration.isBooleanType()) {
      var sql = getSql(location);
      throw new AptException(
          Message.DOMA4141,
          methodElement,
          new Object[] {
            path,
            sql,
            location.getLineNumber(),
            location.getPosition(),
            expression,
            typeDeclaration.getBinaryName()
          });
    }
    visitNode(node, p);
    return null;
  }

  @Override
  public Void visitForNode(ForNode node, Void p) {
    var location = node.getLocation();
    var identifier = node.getIdentifier();
    var expression = node.getExpression();
    var typeDeclaration = validateExpressionVariable(location, expression);
    var typeMirror = typeDeclaration.getType();
    if (!ctx.getTypes().isAssignable(typeMirror, Iterable.class)) {
      var sql = getSql(location);
      throw new AptException(
          Message.DOMA4149,
          methodElement,
          new Object[] {
            path,
            sql,
            location.getLineNumber(),
            location.getPosition(),
            expression,
            typeDeclaration.getBinaryName()
          });
    }
    var declaredType = ctx.getTypes().toDeclaredType(typeMirror);
    var typeArgs = declaredType.getTypeArguments();
    if (typeArgs.isEmpty()) {
      var sql = getSql(location);
      throw new AptException(
          Message.DOMA4150,
          methodElement,
          new Object[] {
            path,
            sql,
            location.getLineNumber(),
            location.getPosition(),
            expression,
            typeDeclaration.getBinaryName()
          });
    }

    var originalIdentifierType = expressionValidator.removeParameterType(identifier);
    expressionValidator.putParameterType(identifier, typeArgs.get(0));
    var hasNextVariable = identifier + ForBlockNode.HAS_NEXT_SUFFIX;
    var originalHasNextType = expressionValidator.removeParameterType(hasNextVariable);
    expressionValidator.putParameterType(hasNextVariable, ctx.getTypes().getType(boolean.class));
    var indexVariable = identifier + ForBlockNode.INDEX_SUFFIX;
    var originalIndexType = expressionValidator.removeParameterType(indexVariable);
    expressionValidator.putParameterType(indexVariable, ctx.getTypes().getType(int.class));
    visitNode(node, p);
    if (originalIdentifierType == null) {
      expressionValidator.removeParameterType(identifier);
    } else {
      expressionValidator.putParameterType(identifier, originalIdentifierType);
    }
    if (originalHasNextType == null) {
      expressionValidator.removeParameterType(hasNextVariable);
    } else {
      expressionValidator.putParameterType(hasNextVariable, originalHasNextType);
    }
    if (originalIndexType == null) {
      expressionValidator.removeParameterType(indexVariable);
    } else {
      expressionValidator.putParameterType(indexVariable, originalIndexType);
    }
    return null;
  }

  @Override
  public Void visitExpandNode(ExpandNode node, Void p) {
    if (!expandable) {
      var location = node.getLocation();
      var sql = getSql(location);
      throw new AptException(
          Message.DOMA4257,
          methodElement,
          new Object[] {path, sql, location.getLineNumber(), location.getPosition()});
    }
    return visitNode(node, p);
  }

  @Override
  public Void visitPopulateNode(PopulateNode node, Void p) {
    if (!populatable) {
      var location = node.getLocation();
      var sql = getSql(location);
      throw new AptException(
          Message.DOMA4270,
          methodElement,
          new Object[] {path, sql, location.getLineNumber(), location.getPosition()});
    }
    var it = parameterTypeMap.keySet().iterator();
    if (it.hasNext()) {
      expressionValidator.addValidatedParameterName(it.next());
    }
    return visitNode(node, p);
  }

  @Override
  protected Void defaultAction(SqlNode node, Void p) {
    return visitNode(node, p);
  }

  protected Void visitNode(SqlNode node, Void p) {
    for (var child : node.getChildren()) {
      child.accept(this, p);
    }
    return null;
  }

  protected TypeDeclaration validateExpressionVariable(SqlLocation location, String expression) {
    var expressionNode = parseExpression(location, expression);
    try {
      return expressionValidator.validate(expressionNode);
    } catch (AptException e) {
      var sql = getSql(location);
      throw new AptException(
          Message.DOMA4092,
          methodElement,
          new Object[] {
            path, sql, location.getLineNumber(), location.getPosition(), e.getMessage()
          });
    }
  }

  protected ExpressionNode parseExpression(SqlLocation location, String expression) {
    try {
      var parser = new ExpressionParser(expression);
      return parser.parse();
    } catch (ExpressionException e) {
      var sql = getSql(location);
      throw new AptException(
          Message.DOMA4092,
          methodElement,
          new Object[] {
            path, sql, location.getLineNumber(), location.getPosition(), e.getMessage()
          });
    }
  }

  protected String getSql(SqlLocation location) {
    var sql = location.getSql();
    if (sql != null && sql.length() > SQL_MAX_LENGTH) {
      sql = sql.substring(0, SQL_MAX_LENGTH);
      sql += Message.DOMA4185.getSimpleMessage(SQL_MAX_LENGTH);
    }
    return sql;
  }
}
