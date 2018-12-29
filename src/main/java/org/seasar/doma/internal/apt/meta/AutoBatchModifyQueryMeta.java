package org.seasar.doma.internal.apt.meta;

import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.mirror.BatchModifyMirror;
import org.seasar.doma.jdbc.SqlLogType;

public class AutoBatchModifyQueryMeta extends AbstractQueryMeta {

  protected EntityCtType entityCtType;

  protected String entitiesParameterName;

  protected BatchModifyMirror batchModifyMirror;

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

  BatchModifyMirror getBatchModifyMirror() {
    return batchModifyMirror;
  }

  void setBatchModifyMirror(BatchModifyMirror batchModifyMirror) {
    this.batchModifyMirror = batchModifyMirror;
  }

  public int getQueryTimeout() {
    return batchModifyMirror.getQueryTimeoutValue();
  }

  public int getBatchSize() {
    return batchModifyMirror.getBatchSizeValue();
  }

  public Boolean getIgnoreVersion() {
    return batchModifyMirror.getIgnoreVersionValue();
  }

  public Boolean getSuppressOptimisticLockException() {
    return batchModifyMirror.getSuppressOptimisticLockExceptionValue();
  }

  public List<String> getInclude() {
    return batchModifyMirror.getIncludeValue();
  }

  public List<String> getExclude() {
    return batchModifyMirror.getExcludeValue();
  }

  public SqlLogType getSqlLogType() {
    return batchModifyMirror.getSqlLogValue();
  }

  @Override
  public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
    return visitor.visitAutoBatchModifyQueryMeta(this, p);
  }
}
