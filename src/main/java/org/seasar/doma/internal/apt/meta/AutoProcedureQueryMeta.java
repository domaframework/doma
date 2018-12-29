package org.seasar.doma.internal.apt.meta;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.internal.apt.mirror.ProcedureMirror;
import org.seasar.doma.jdbc.SqlLogType;

/** @author taedium */
public class AutoProcedureQueryMeta extends AutoModuleQueryMeta {

  protected ProcedureMirror procedureMirror;

  public AutoProcedureQueryMeta(ExecutableElement method, TypeElement dao) {
    super(method, dao);
  }

  ProcedureMirror getProcedureMirror() {
    return procedureMirror;
  }

  void setProcedureMirror(ProcedureMirror procedureMirror) {
    this.procedureMirror = procedureMirror;
  }

  public String getCatalogName() {
    return procedureMirror.getCatalogValue();
  }

  public String getSchemaName() {
    return procedureMirror.getSchemaValue();
  }

  public String getProcedureName() {
    return procedureMirror.getNameValue();
  }

  public boolean isQuoteRequired() {
    return procedureMirror.getQuoteValue();
  }

  public int getQueryTimeout() {
    return procedureMirror.getQueryTimeoutValue();
  }

  @Override
  public MapKeyNamingType getMapKeyNamingType() {
    return procedureMirror.getMapKeyNamingValue();
  }

  public SqlLogType getSqlLogType() {
    return procedureMirror.getSqlLogValue();
  }

  @Override
  public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
    return visitor.visitAutoProcedureQueryMeta(this, p);
  }
}
