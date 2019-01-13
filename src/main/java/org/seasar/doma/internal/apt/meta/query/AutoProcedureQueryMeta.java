package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.internal.apt.annot.ProcedureAnnot;
import org.seasar.doma.jdbc.SqlLogType;

public class AutoProcedureQueryMeta extends AutoModuleQueryMeta {

  private ProcedureAnnot procedureAnnot;

  public AutoProcedureQueryMeta(ExecutableElement method, TypeElement dao) {
    super(method, dao);
  }

  public ProcedureAnnot getProcedureAnnot() {
    return procedureAnnot;
  }

  void setProcedureAnnot(ProcedureAnnot procedureAnnot) {
    this.procedureAnnot = procedureAnnot;
  }

  public String getCatalogName() {
    return procedureAnnot.getCatalogValue();
  }

  public String getSchemaName() {
    return procedureAnnot.getSchemaValue();
  }

  public String getProcedureName() {
    return procedureAnnot.getNameValue();
  }

  public boolean isQuoteRequired() {
    return procedureAnnot.getQuoteValue();
  }

  public int getQueryTimeout() {
    return procedureAnnot.getQueryTimeoutValue();
  }

  @Override
  public MapKeyNamingType getMapKeyNamingType() {
    return procedureAnnot.getMapKeyNamingValue();
  }

  public SqlLogType getSqlLogType() {
    return procedureAnnot.getSqlLogValue();
  }

  @Override
  public <R> R accept(QueryMetaVisitor<R> visitor) {
    return visitor.visitAutoProcedureQueryMeta(this);
  }
}
