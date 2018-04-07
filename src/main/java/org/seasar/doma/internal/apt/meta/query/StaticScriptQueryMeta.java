package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.annot.ScriptAnnot;
import org.seasar.doma.jdbc.SqlLogType;

public class StaticScriptQueryMeta extends AbstractSqlTemplateQueryMeta {

  private ScriptAnnot scriptAnnot;

  public StaticScriptQueryMeta(ExecutableElement method) {
    super(method);
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
  public <P> void accept(QueryMetaVisitor<P> visitor, P p) {
    visitor.visitStaticScriptQueryMeta(this, p);
  }
}
