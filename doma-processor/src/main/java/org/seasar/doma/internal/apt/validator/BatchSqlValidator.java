package org.seasar.doma.internal.apt.validator;

import java.util.LinkedHashMap;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;
import org.seasar.doma.Suppress;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.jdbc.sql.node.EmbeddedVariableNode;
import org.seasar.doma.internal.jdbc.sql.node.ForNode;
import org.seasar.doma.internal.jdbc.sql.node.IfNode;
import org.seasar.doma.message.Message;

public class BatchSqlValidator extends SqlValidator {

  private boolean embeddedVariableWarningNotified;

  private boolean ifWarningNotified;

  private boolean forWarningNotified;

  private final Suppress suppress;

  public BatchSqlValidator(
      Context ctx,
      ExecutableElement methodElement,
      LinkedHashMap<String, TypeMirror> parameterTypeMap,
      String path,
      boolean expandable,
      boolean populatable) {
    super(ctx, methodElement, parameterTypeMap, path, expandable, populatable);
    suppress = methodElement.getAnnotation(Suppress.class);
  }

  @Override
  public Void visitEmbeddedVariableNode(EmbeddedVariableNode node, Void p) {
    if (!isSuppressed(Message.DOMA4181) && !embeddedVariableWarningNotified) {
      ctx.getReporter().report(Kind.WARNING, Message.DOMA4181, methodElement, new Object[] {path});
      embeddedVariableWarningNotified = true;
    }
    return super.visitEmbeddedVariableNode(node, p);
  }

  @Override
  public Void visitIfNode(IfNode node, Void p) {
    if (!isSuppressed(Message.DOMA4182) && !ifWarningNotified) {
      ctx.getReporter().report(Kind.WARNING, Message.DOMA4182, methodElement, new Object[] {path});
      ifWarningNotified = true;
    }
    return super.visitIfNode(node, p);
  }

  @Override
  public Void visitForNode(ForNode node, Void p) {
    if (!isSuppressed(Message.DOMA4183) && !forWarningNotified) {
      ctx.getReporter().report(Kind.WARNING, Message.DOMA4183, methodElement, new Object[] {path});
      forWarningNotified = true;
    }
    return super.visitForNode(node, p);
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private boolean isSuppressed(Message message) {
    if (suppress != null) {
      for (Message suppressMessage : suppress.messages()) {
        if (suppressMessage == message) {
          return true;
        }
      }
    }
    return false;
  }
}
