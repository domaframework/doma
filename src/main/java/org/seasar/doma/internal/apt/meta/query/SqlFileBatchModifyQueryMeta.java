package org.seasar.doma.internal.apt.meta.query;

import java.util.List;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.reflection.BatchModifyReflection;
import org.seasar.doma.jdbc.SqlLogType;

/** @author taedium */
public class SqlFileBatchModifyQueryMeta extends AbstractSqlFileQueryMeta {

  private EntityCtType entityCtType;

  private CtType elementCtType;

  private String elementsParameterName;

  private BatchModifyReflection batchModifyReflection;

  public SqlFileBatchModifyQueryMeta(ExecutableElement method) {
    super(method);
  }

  public EntityCtType getEntityCtType() {
    return entityCtType;
  }

  public void setEntityCtType(EntityCtType entityCtType) {
    this.entityCtType = entityCtType;
  }

  public CtType getElementCtType() {
    return elementCtType;
  }

  public void setElementCtType(CtType elementCtType) {
    this.elementCtType = elementCtType;
  }

  public String getElementsParameterName() {
    return elementsParameterName;
  }

  public void setElementsParameterName(String entitiesParameterName) {
    this.elementsParameterName = entitiesParameterName;
  }

  public BatchModifyReflection getBatchModifyReflection() {
    return batchModifyReflection;
  }

  public void setBatchModifyReflection(BatchModifyReflection batchModifyReflection) {
    this.batchModifyReflection = batchModifyReflection;
  }

  public int getQueryTimeout() {
    return batchModifyReflection.getQueryTimeoutValue();
  }

  public int getBatchSize() {
    return batchModifyReflection.getBatchSizeValue();
  }

  public Boolean getIgnoreVersion() {
    return batchModifyReflection.getIgnoreVersionValue();
  }

  public Boolean getSuppressOptimisticLockException() {
    return batchModifyReflection.getSuppressOptimisticLockExceptionValue();
  }

  public List<String> getInclude() {
    return batchModifyReflection.getIncludeValue();
  }

  public List<String> getExclude() {
    return batchModifyReflection.getExcludeValue();
  }

  public SqlLogType getSqlLogType() {
    return batchModifyReflection.getSqlLogValue();
  }

  public boolean isPopulatable() {
    return entityCtType != null && queryKind == QueryKind.SQLFILE_BATCH_UPDATE;
  }

  @Override
  public void addBindableParameterCtType(
      final String parameterName, CtType bindableParameterCtType) {
    bindableParameterCtType.accept(
        new BindableParameterCtTypeVisitor(parameterName) {

          @Override
          public Void visitIterableCtType(IterableCtType ctType, Void p) throws RuntimeException {
            return ctType.getElementCtType().accept(this, p);
          }
        },
        null);
  }

  @Override
  public <P> void accept(QueryMetaVisitor<P> visitor, P p) {
    visitor.visitSqlFileBatchModifyQueryMeta(this, p);
  }
}
