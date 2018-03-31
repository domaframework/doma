package org.seasar.doma.internal.apt.meta.query;

import java.util.List;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.internal.apt.annot.BatchModifyAnnot;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.jdbc.SqlLogType;

public class SqlFileBatchModifyQueryMeta extends AbstractSqlFileQueryMeta {

  private EntityCtType entityCtType;

  private CtType elementCtType;

  private String elementsParameterName;

  private BatchModifyAnnot batchModifyAnnot;

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

  public BatchModifyAnnot getBatchModifyAnnot() {
    return batchModifyAnnot;
  }

  public void setBatchModifyAnnot(BatchModifyAnnot batchModifyAnnot) {
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
