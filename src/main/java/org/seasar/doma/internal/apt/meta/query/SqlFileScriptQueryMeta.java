package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.annot.ScriptAnnot;
import org.seasar.doma.jdbc.SqlLogType;

public class SqlFileScriptQueryMeta extends AbstractSqlFileQueryMeta {

  private ScriptAnnot scriptAnnot;

  public SqlFileScriptQueryMeta(ExecutableElement method, TypeElement dao) {
    super(method, dao);
  }

  void setScriptAnnot(ScriptAnnot scriptAnnot) {
    this.scriptAnnot = scriptAnnot;
  }

  ScriptAnnot getScriptAnnot() {
    return scriptAnnot;
  }

  public boolean getHaltOnError() {
    return scriptAnnot.getHaltOnErrorValue();
  }

  public String getBlockDelimiter() {
    return scriptAnnot.getBlockDelimiterValue();
  }

  public SqlLogType getSqlLogType() {
    return scriptAnnot.getSqlLogValue();
  }

  @Override
  public <R> R accept(QueryMetaVisitor<R> visitor) {
    return visitor.visitSqlFileScriptQueryMeta(this);
  }
}
