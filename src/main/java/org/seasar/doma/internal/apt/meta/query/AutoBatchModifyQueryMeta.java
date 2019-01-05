package org.seasar.doma.internal.apt.meta.query;

import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.annot.BatchModifyAnnot;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.jdbc.SqlLogType;

public class AutoBatchModifyQueryMeta extends AbstractQueryMeta {

  private EntityCtType entityCtType;

  private String entitiesParameterName;

  private BatchModifyAnnot batchModifyAnnot;

  public AutoBatchModifyQueryMeta(ExecutableElement method, TypeElement dao) {
    super(method, dao);
  }

  public EntityCtType getEntityCtType() {
    return entityCtType;
  }

  public void setEntityCtType(EntityCtType entityCtType) {
    this.entityCtType = entityCtType;
  }

  public String getEntitiesParameterName() {
    return entitiesParameterName;
  }

  public void setEntitiesParameterName(String entitiesParameterName) {
    this.entitiesParameterName = entitiesParameterName;
  }

  BatchModifyAnnot getBatchModifyAnnot() {
    return batchModifyAnnot;
  }

  void setBatchModifyAnnot(BatchModifyAnnot batchModifyAnnot) {
    this.batchModifyAnnot = batchModifyAnnot;
  }

  public int getQueryTimeout() {
    return batchModifyAnnot.getQueryTimeoutValue();
  }

  public int getBatchSize() {
    return batchModifyAnnot.getBatchSizeValue();
  }

  public Boolean getIgnoreVersion() {
    return batchModifyAnnot.getIgnoreVersionValue();
  }

  public Boolean getSuppressOptimisticLockException() {
    return batchModifyAnnot.getSuppressOptimisticLockExceptionValue();
  }

  public List<String> getInclude() {
    return batchModifyAnnot.getIncludeValue();
  }

  public List<String> getExclude() {
    return batchModifyAnnot.getExcludeValue();
  }

  public SqlLogType getSqlLogType() {
    return batchModifyAnnot.getSqlLogValue();
  }

  @Override
  public <R> R accept(QueryMetaVisitor<R> visitor) {
    return visitor.visitAutoBatchModifyQueryMeta(this);
  }
}
