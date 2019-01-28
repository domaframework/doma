package org.seasar.doma.internal.apt.validator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.ArrayCtType;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.DomainCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
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
      Set<String> validatedParameterNames = expressionValidator.getValidatedParameterNames();
      for (String parameterName : parameterTypeMap.keySet()) {
        if (!validatedParameterNames.contains(parameterName)) {
          for (VariableElement parameterElement : methodElement.getParameters()) {
            if (parameterElement.getSimpleName().contentEquals(parameterName)) {
              ctx.getNotifier()
                  .notify(
                      Kind.ERROR,
                      Message.DOMA4122,
                      parameterElement,
                      new Object[] {path, parameterName});
            }
          }
        }
      }
    } catch (AptIllegalStateException e) {
      throw e;
    } catch (AptException e) {
      ctx.getNotifier().notify(e);
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
    SqlLocation location = node.getLocation();
    String variableName = node.getVariableName();
    TypeDeclaration typeDeclaration = validateExpressionVariable(location, variableName);
    if (node.getWordNode() != null) {
      if (!isScalar(typeDeclaration)) {
        String sql = getSql(location);
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
      if (!isScalarIterable(typeDeclaration) && !isScalarArray(typeDeclaration)) {
        String sql = getSql(location);
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
    TypeMirror typeMirror = typeDeclaration.getType();
    return ctx.getCtTypes().newBasicCtType(typeMirror) != null
        || ctx.getCtTypes().newDomainCtType(typeMirror) != null;
  }

  protected boolean isScalarIterable(TypeDeclaration typeDeclaration) {
    TypeMirror typeMirror = typeDeclaration.getType();
    IterableCtType iterableCtType = ctx.getCtTypes().newIterableCtType(typeMirror);
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
                public Boolean visitDomainCtType(DomainCtType ctType, Void p)
                    throws RuntimeException {
                  return true;
                }
              },
              null);
    }
    return false;
  }

  protected boolean isScalarArray(TypeDeclaration typeDeclaration) {
    TypeMirror typeMirror = typeDeclaration.getType();
    ArrayCtType arrayCtType = ctx.getCtTypes().newArrayCtType(typeMirror);
    if (arrayCtType != null) {
      return arrayCtType
          .getElementCtType()
          .accept(
              new SimpleCtTypeVisitor<Boolean, Void, RuntimeException>(false) {

                @Override
                public Boolean visitBasicCtType(BasicCtType ctType, Void p)
                    throws RuntimeException {
                  return true;
                }

                @Override
                public Boolean visitDomainCtType(DomainCtType ctType, Void p)
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
    SqlLocation location = node.getLocation();
    String variableName = node.getVariableName();
    validateExpressionVariable(location, variableName);
    visitNode(node, p);
    return null;
  }

  @Override
  public Void visitIfNode(IfNode node, Void p) {
    SqlLocation location = node.getLocation();
    String expression = node.getExpression();
    TypeDeclaration typeDeclaration = validateExpressionVariable(location, expression);
    if (!typeDeclaration.isBooleanType()) {
      String sql = getSql(location);
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
    SqlLocation location = node.getLocation();
    String expression = node.getExpression();
    TypeDeclaration typeDeclaration = validateExpressionVariable(location, expression);
    if (!typeDeclaration.isBooleanType()) {
      String sql = getSql(location);
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
    SqlLocation location = node.getLocation();
    String identifier = node.getIdentifier();
    String expression = node.getExpression();
    TypeDeclaration typeDeclaration = validateExpressionVariable(location, expression);
    TypeMirror typeMirror = typeDeclaration.getType();
    List<? extends TypeMirror> typeArgs;
    if (ctx.getTypes().isAssignable(typeMirror, Iterable.class)) {
      DeclaredType declaredType = ctx.getTypes().toDeclaredType(typeMirror);
      typeArgs = declaredType.getTypeArguments();
    } else if (ctx.getTypes().isArray(typeMirror)) {
      ArrayType arrayType = ctx.getTypes().toArrayType(typeMirror);
      typeArgs = Collections.singletonList(arrayType.getComponentType());
    } else {
      String sql = getSql(location);
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
    if (typeArgs.isEmpty()) {
      String sql = getSql(location);
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

    TypeMirror originalIdentifierType = expressionValidator.removeParameterType(identifier);
    expressionValidator.putParameterType(identifier, typeArgs.get(0));
    String hasNextVariable = identifier + ForBlockNode.HAS_NEXT_SUFFIX;
    TypeMirror originalHasNextType = expressionValidator.removeParameterType(hasNextVariable);
    expressionValidator.putParameterType(
        hasNextVariable, ctx.getTypes().getTypeMirror(boolean.class));
    String indexVariable = identifier + ForBlockNode.INDEX_SUFFIX;
    TypeMirror originalIndexType = expressionValidator.removeParameterType(indexVariable);
    expressionValidator.putParameterType(indexVariable, ctx.getTypes().getTypeMirror(int.class));
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
      SqlLocation location = node.getLocation();
      String sql = getSql(location);
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
      SqlLocation location = node.getLocation();
      String sql = getSql(location);
      throw new AptException(
          Message.DOMA4270,
          methodElement,
          new Object[] {path, sql, location.getLineNumber(), location.getPosition()});
    }
    Iterator<String> it = parameterTypeMap.keySet().iterator();
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
    for (SqlNode child : node.getChildren()) {
      child.accept(this, p);
    }
    return null;
  }

  protected TypeDeclaration validateExpressionVariable(SqlLocation location, String expression) {
    ExpressionNode expressionNode = parseExpression(location, expression);
    try {
      return expressionValidator.validate(expressionNode);
    } catch (AptIllegalStateException e) {
      throw e;
    } catch (AptException e) {
      String sql = getSql(location);
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
      ExpressionParser parser = new ExpressionParser(expression);
      return parser.parse();
    } catch (ExpressionException e) {
      String sql = getSql(location);
      throw new AptException(
          Message.DOMA4092,
          methodElement,
          new Object[] {
            path, sql, location.getLineNumber(), location.getPosition(), e.getMessage()
          });
    }
  }

  protected String getSql(SqlLocation location) {
    String sql = location.getSql();
    if (sql != null && sql.length() > SQL_MAX_LENGTH) {
      sql = sql.substring(0, SQL_MAX_LENGTH);
      sql += Message.DOMA4185.getSimpleMessage(SQL_MAX_LENGTH);
    }
    return sql;
  }
}
