package org.seasar.doma.internal.apt.sql;

import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.SuppressAnnot;
import org.seasar.doma.internal.apt.expr.ExpressionValidator;
import org.seasar.doma.internal.jdbc.sql.node.EmbeddedVariableNode;
import org.seasar.doma.internal.jdbc.sql.node.ForNode;
import org.seasar.doma.internal.jdbc.sql.node.IfNode;
import org.seasar.doma.message.Message;

public class BatchSqlValidator extends SqlValidator {

  private boolean embeddedVariableWarningSent;

  private boolean conditionWarningSent;

  private boolean iterationWarningSent;

  private final SuppressAnnot suppressAnnot;

  public BatchSqlValidator(
      Context ctx,
      ExecutableElement methodElement,
      SqlTemplate sqlTemplate,
      ExpressionValidator expressionValidator,
      boolean expandable,
      boolean populatable) {
    super(ctx, methodElement, sqlTemplate, expressionValidator, expandable, populatable);
    suppressAnnot = ctx.getAnnots().newSuppressAnnot(methodElement);
  }

  @Override
  public Void visitEmbeddedVariableNode(EmbeddedVariableNode node, Void p) {
    if (!isSuppressed(Message.DOMA4181) && !embeddedVariableWarningSent) {
      sqlTemplate.sendEmbeddedVariableWarning();
      embeddedVariableWarningSent = true;
    }
    return super.visitEmbeddedVariableNode(node, p);
  }

  @Override
  public Void visitIfNode(IfNode node, Void p) {
    if (!isSuppressed(Message.DOMA4182) && !conditionWarningSent) {
      sqlTemplate.sendConditionWarning();
      conditionWarningSent = true;
    }
    return super.visitIfNode(node, p);
  }

  @Override
  public Void visitForNode(ForNode node, Void p) {
    if (!isSuppressed(Message.DOMA4183) && !iterationWarningSent) {
      sqlTemplate.sendIterationWarning();
      iterationWarningSent = true;
    }
    return super.visitForNode(node, p);
  }

  private boolean isSuppressed(Message message) {
    if (suppressAnnot != null) {
      return suppressAnnot.isSuppressed(message);
    }
    return false;
  }
}
