package org.seasar.doma.internal.apt.meta.query;

import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.annot.ModifyAnnot;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.jdbc.SqlLogType;

public class SqlFileModifyQueryMeta extends AbstractSqlFileQueryMeta {

  protected EntityCtType entityCtType;

  protected String entityParameterName;

  protected ModifyAnnot modifyAnnot;

  public SqlFileModifyQueryMeta(ExecutableElement method, TypeElement dao) {
    super(method, dao);
  }

  public EntityCtType getEntityCtType() {
    return entityCtType;
  }

  public void setEntityCtType(EntityCtType entityCtType) {
    this.entityCtType = entityCtType;
  }

  public String getEntityParameterName() {
    return entityParameterName;
  }

  public void setEntityParameterName(String entityParameterName) {
    this.entityParameterName = entityParameterName;
  }

  public ModifyAnnot getModifyAnnot() {
    return modifyAnnot;
  }

  public void setModifyAnnot(ModifyAnnot modifyAnnot) {
    this.modifyAnnot = modifyAnnot;
  }

  public int getQueryTimeout() {
    return modifyAnnot.getQueryTimeoutValue();
  }

  public Boolean getIgnoreVersion() {
    return modifyAnnot.getIgnoreVersionValue();
  }

  public Boolean getExcludeNull() {
    return modifyAnnot.getExcludeNullValue();
  }

  public Boolean getSuppressOptimisticLockException() {
    return modifyAnnot.getSuppressOptimisticLockExceptionValue();
  }

  public Boolean getIncludeUnchanged() {
    return modifyAnnot.getIncludeUnchangedValue();
  }

  public List<String> getInclude() {
    return modifyAnnot.getIncludeValue();
  }

  public List<String> getExclude() {
    return modifyAnnot.getExcludeValue();
  }

  public SqlLogType getSqlLogType() {
    return modifyAnnot.getSqlLogValue();
  }

  public boolean isPopulatable() {
    return entityCtType != null && queryKind == QueryKind.SQLFILE_UPDATE;
  }

  @Override
  public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
    return visitor.visitSqlFileModifyQueryMeta(this, p);
  }
}
