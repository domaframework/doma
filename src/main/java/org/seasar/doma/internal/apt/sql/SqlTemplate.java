package org.seasar.doma.internal.apt.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.jdbc.sql.node.SqlLocation;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.message.Message;
import org.seasar.doma.message.MessageResource;

public class SqlTemplate {

  protected static final int SQL_MAX_LENGTH = 5000;

  private final Context ctx;

  private final ExecutableElement methodElement;

  private final AnnotationMirror annotationMirror;

  private final AnnotationValue annotationValue;

  private final String sqlFilePath;

  private final SqlNode sqlNode;

  public SqlTemplate(
      Context ctx, ExecutableElement methodElement, String sqlFilePath, SqlNode sqlNode) {
    this(ctx, methodElement, null, null, sqlFilePath, sqlNode);
  }

  public SqlTemplate(
      Context ctx,
      ExecutableElement methodElement,
      AnnotationMirror annotationMirror,
      AnnotationValue annotationValue,
      String sqlFilePath,
      SqlNode sqlNode) {
    assertNotNull(ctx, methodElement, sqlNode);
    this.ctx = ctx;
    this.methodElement = methodElement;
    this.annotationMirror = annotationMirror;
    this.annotationValue = annotationValue;
    this.sqlFilePath = sqlFilePath;
    this.sqlNode = sqlNode;
  }

  public void sendParameterNotReferredError(
      VariableElement parameterElement, String parameterName) {
    ctx.getNotifier()
        .send(
            Diagnostic.Kind.ERROR,
            Message.DOMA4122,
            parameterElement,
            new Object[] {sqlFilePath, parameterName});
  }

  public void sendNotScalarError(SqlLocation location, String variableName, String typeName) {
    var sql = getSql(location);
    sendError(
        Message.DOMA4153,
        new Object[] {
          sqlFilePath, sql, location.getLineNumber(), location.getPosition(), variableName, typeName
        });
  }

  public void sendNotScalarIterableError(
      SqlLocation location, String variableName, String typeName) {
    var sql = getSql(location);
    sendError(
        Message.DOMA4161,
        new Object[] {
          sqlFilePath, sql, location.getLineNumber(), location.getPosition(), variableName, typeName
        });
  }

  public void sendNotBooleanInIfError(SqlLocation location, String expression, String typeName) {
    var sql = getSql(location);
    sendError(
        Message.DOMA4140,
        new Object[] {
          sqlFilePath, sql, location.getLineNumber(), location.getPosition(), expression, typeName
        });
  }

  public void sendNotBooleanInElseifError(
      SqlLocation location, String expression, String typeName) {
    var sql = getSql(location);
    sendError(
        Message.DOMA4141,
        new Object[] {
          sqlFilePath, sql, location.getLineNumber(), location.getPosition(), expression, typeName
        });
  }

  public void sendNotIterableInForError(SqlLocation location, String expression, String typeName) {
    var sql = getSql(location);
    sendError(
        Message.DOMA4149,
        new Object[] {
          sqlFilePath, sql, location.getLineNumber(), location.getPosition(), expression, typeName
        });
  }

  public void sendTypeArgObscureInForError(
      SqlLocation location, String expression, String typeName) {
    var sql = getSql(location);
    sendError(
        Message.DOMA4150,
        new Object[] {
          sqlFilePath, sql, location.getLineNumber(), location.getPosition(), expression, typeName
        });
  }

  public void sendExpansionError(SqlLocation location) {
    var sql = getSql(location);
    sendError(
        Message.DOMA4257,
        new Object[] {sqlFilePath, sql, location.getLineNumber(), location.getPosition()});
  }

  public void sendPopulationError(SqlLocation location) {
    var sql = getSql(location);
    sendError(
        Message.DOMA4270,
        new Object[] {sqlFilePath, sql, location.getLineNumber(), location.getPosition()});
  }

  public void sendExpressionError(SqlLocation location, String message) {
    var sql = getSql(location);
    sendError(
        Message.DOMA4092,
        new Object[] {sqlFilePath, sql, location.getLineNumber(), location.getPosition(), message});
  }

  public void sendEmbeddedVariableWarning() {
    sendWarning(Message.DOMA4181, new Object[] {sqlFilePath});
  }

  public void sendConditionWarning() {
    sendWarning(Message.DOMA4182, new Object[] {sqlFilePath});
  }

  public void sendIterationWarning() {
    sendWarning(Message.DOMA4183, new Object[] {sqlFilePath});
  }

  private void sendError(MessageResource messageResource, Object[] args) {
    ctx.getNotifier()
        .send(
            Diagnostic.Kind.ERROR,
            messageResource,
            methodElement,
            annotationMirror,
            annotationValue,
            args);
  }

  private void sendWarning(MessageResource messageResource, Object[] args) {
    ctx.getNotifier()
        .send(
            Diagnostic.Kind.WARNING,
            messageResource,
            methodElement,
            annotationMirror,
            annotationValue,
            args);
  }

  protected String getSql(SqlLocation location) {
    var sql = location.getSql();
    if (sql != null && sql.length() > SQL_MAX_LENGTH) {
      sql = sql.substring(0, SQL_MAX_LENGTH);
      sql += Message.DOMA4185.getSimpleMessage(SQL_MAX_LENGTH);
    }
    return sql;
  }

  public void validate(SqlValidator validator) {
    sqlNode.accept(validator, null);
  }
}
