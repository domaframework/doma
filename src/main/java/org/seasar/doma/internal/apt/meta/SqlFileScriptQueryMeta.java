package org.seasar.doma.internal.apt.meta;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.mirror.ScriptMirror;
import org.seasar.doma.jdbc.SqlLogType;

public class SqlFileScriptQueryMeta extends AbstractSqlFileQueryMeta {

  protected ScriptMirror scriptMirror;

  public SqlFileScriptQueryMeta(ExecutableElement method, TypeElement dao) {
    super(method, dao);
  }

  void setScriptMirror(ScriptMirror scriptMirror) {
    this.scriptMirror = scriptMirror;
  }

  ScriptMirror getScriptMirror() {
    return scriptMirror;
  }

  public boolean getHaltOnError() {
    return scriptMirror.getHaltOnErrorValue();
  }

  public String getBlockDelimiter() {
    return scriptMirror.getBlockDelimiterValue();
  }

  public SqlLogType getSqlLogType() {
    return scriptMirror.getSqlLogValue();
  }

  @Override
  public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
    return visitor.visitSqlFileScriptQueryMeta(this, p);
  }
}
