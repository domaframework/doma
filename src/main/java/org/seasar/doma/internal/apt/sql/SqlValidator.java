package org.seasar.doma.internal.apt.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.HolderCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.decl.TypeDeclaration;
import org.seasar.doma.internal.apt.expr.ExpressionValidationException;
import org.seasar.doma.internal.apt.expr.ExpressionValidator;
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

public class SqlValidator extends SimpleSqlNodeVisitor<Void, Void> {

  protected final Context ctx;

  protected final ExecutableElement methodElement;

  protected final SqlTemplate sqlTemplate;

  protected final ExpressionValidator expressionValidator;

  protected final boolean expandable;

  protected final boolean populatable;

  public SqlValidator(
      Context ctx,
      ExecutableElement methodElement,
      SqlTemplate sqlTemplate,
      ExpressionValidator expressionValidator,
      boolean expandable,
      boolean populatable) {
    assertNotNull(ctx, methodElement, sqlTemplate, expressionValidator);
    this.ctx = ctx;
    this.methodElement = methodElement;
    this.sqlTemplate = sqlTemplate;
    this.expressionValidator = expressionValidator;
    this.expandable = expandable;
    this.populatable = populatable;
  }

  public void validate() {
    try {
      sqlTemplate.validate(this);
      expressionValidator.checkNonReferredParameters(
          methodElement,
          (parameterElement, parameterName) ->
              sqlTemplate.sendParameterNotReferredError(parameterElement, parameterName));
    } catch (SqlValidationException e) {
      e.run();
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
        throw new SqlValidationException(
            () ->
                sqlTemplate.sendNotScalarError(
                    location, variableName, typeDeclaration.getBinaryName()));
      }
    } else {
      if (!isScalarIterable(typeDeclaration)) {
        throw new SqlValidationException(
            () ->
                sqlTemplate.sendNotScalarIterableError(
                    location, variableName, typeDeclaration.getBinaryName()));
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
      throw new SqlValidationException(
          () ->
              sqlTemplate.sendNotBooleanInIfError(
                  location, expression, typeDeclaration.getBinaryName()));
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
      throw new SqlValidationException(
          () ->
              sqlTemplate.sendNotBooleanInElseifError(
                  location, expression, typeDeclaration.getBinaryName()));
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
      throw new SqlValidationException(
          () ->
              sqlTemplate.sendNotIterableInForError(
                  location, expression, typeDeclaration.getBinaryName()));
    }
    var declaredType = ctx.getTypes().toDeclaredType(typeMirror);
    var typeArgs = declaredType.getTypeArguments();
    if (typeArgs.isEmpty()) {
      throw new SqlValidationException(
          () ->
              sqlTemplate.sendTypeArgObscureInForError(
                  location, expression, typeDeclaration.getBinaryName()));
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
      throw new SqlValidationException(() -> sqlTemplate.sendExpansionError(location));
    }
    return visitNode(node, p);
  }

  @Override
  public Void visitPopulateNode(PopulateNode node, Void p) {
    if (!populatable) {
      var location = node.getLocation();
      throw new SqlValidationException(() -> sqlTemplate.sendPopulationError(location));
    }
    expressionValidator.recognizeFirstParameterValid();
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
    } catch (ExpressionValidationException e) {
      throw new SqlValidationException(
          () -> sqlTemplate.sendExpressionError(location, e.getMessage()));
    }
  }

  protected ExpressionNode parseExpression(SqlLocation location, String expression) {
    try {
      var parser = new ExpressionParser(expression);
      return parser.parse();
    } catch (ExpressionException e) {
      throw new SqlValidationException(
          () -> sqlTemplate.sendExpressionError(location, e.getMessage()));
    }
  }

  private static class SqlValidationException extends RuntimeException {
    private Runnable runnable;

    public SqlValidationException(Runnable runnable) {
      this.runnable = runnable;
    }

    public void run() {
      runnable.run();
    }
  }
}
