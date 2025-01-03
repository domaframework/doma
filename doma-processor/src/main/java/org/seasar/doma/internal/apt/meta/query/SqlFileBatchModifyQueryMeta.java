/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.apt.meta.query;

import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
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

  public SqlFileBatchModifyQueryMeta(TypeElement daoElement, ExecutableElement methodElement) {
    super(daoElement, methodElement);
  }

  public EntityCtType getEntityType() {
    return entityCtType;
  }

  public void setEntityType(EntityCtType entityCtType) {
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
  public <R> R accept(QueryMetaVisitor<R> visitor) {
    return visitor.visitSqlFileBatchModifyQueryMeta(this);
  }
}
