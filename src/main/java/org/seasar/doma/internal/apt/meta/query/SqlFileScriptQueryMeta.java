package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.reflection.ScriptReflection;
import org.seasar.doma.jdbc.SqlLogType;

public class SqlFileScriptQueryMeta extends AbstractSqlFileQueryMeta {

  private ScriptReflection scriptReflection;

  public SqlFileScriptQueryMeta(ExecutableElement method) {
    super(method);
  }

  void setScriptReflection(ScriptReflection scriptReflection) {
    this.scriptReflection = scriptReflection;
  }

  ScriptReflection getScriptReflection() {
    return scriptReflection;
  }

  public boolean getHaltOnError() {
    return scriptReflection.getHaltOnErrorValue();
  }

  public String getBlockDelimiter() {
    return scriptReflection.getBlockDelimiterValue();
  }

  public SqlLogType getSqlLogType() {
    return scriptReflection.getSqlLogValue();
  }

  @Override
  public <P> void accept(QueryMetaVisitor<P> visitor, P p) {
    visitor.visitSqlFileScriptQueryMeta(this, p);
  }
}
