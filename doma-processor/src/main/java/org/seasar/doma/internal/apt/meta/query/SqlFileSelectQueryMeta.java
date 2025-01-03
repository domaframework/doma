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

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.FetchType;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.SelectType;
import org.seasar.doma.internal.apt.annot.SelectAnnot;
import org.seasar.doma.internal.apt.cttype.CollectorCtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.FunctionCtType;
import org.seasar.doma.internal.apt.cttype.SelectOptionsCtType;
import org.seasar.doma.jdbc.SqlLogType;

public class SqlFileSelectQueryMeta extends AbstractSqlFileQueryMeta {

  private SelectAnnot selectAnnot;

  private String functionParameterName;

  private FunctionCtType functionCtType;

  private String collectorParameterName;

  private CollectorCtType collectorCtType;

  private String selectOptionsParameterName;

  private SelectOptionsCtType selectOptionsCtType;

  private EntityCtType entityCtType;

  private boolean resultStream;

  public SqlFileSelectQueryMeta(TypeElement daoElement, ExecutableElement methodElement) {
    super(daoElement, methodElement);
  }

  public String getFunctionParameterName() {
    return functionParameterName;
  }

  public void setFunctionParameterName(String functionParameterName) {
    this.functionParameterName = functionParameterName;
  }

  public FunctionCtType getFunctionCtType() {
    return functionCtType;
  }

  public void setFunctionCtType(FunctionCtType functionCtType) {
    this.functionCtType = functionCtType;
  }

  public String getCollectorParameterName() {
    return collectorParameterName;
  }

  public void setCollectorParameterName(String collectorParameterName) {
    this.collectorParameterName = collectorParameterName;
  }

  public CollectorCtType getCollectorCtType() {
    return collectorCtType;
  }

  public void setCollectorCtType(CollectorCtType collectorCtType) {
    this.collectorCtType = collectorCtType;
  }

  public String getSelectOptionsParameterName() {
    return selectOptionsParameterName;
  }

  public void setSelectOptionsParameterName(String selectOptionsParameterName) {
    this.selectOptionsParameterName = selectOptionsParameterName;
  }

  public SelectOptionsCtType getSelectOptionsCtType() {
    return selectOptionsCtType;
  }

  public void setSelectOptionsCtType(SelectOptionsCtType selectOptionsCtType) {
    this.selectOptionsCtType = selectOptionsCtType;
  }

  public EntityCtType getEntityCtType() {
    return entityCtType;
  }

  public void setEntityCtType(EntityCtType entityCtType) {
    this.entityCtType = entityCtType;
  }

  void setSelectAnnot(SelectAnnot selectAnnot) {
    this.selectAnnot = selectAnnot;
  }

  SelectAnnot getSelectAnnot() {
    return selectAnnot;
  }

  public int getFetchSize() {
    return selectAnnot.getFetchSizeValue();
  }

  public int getMaxRows() {
    return selectAnnot.getMaxRowsValue();
  }

  public int getQueryTimeout() {
    return selectAnnot.getQueryTimeoutValue();
  }

  public SelectType getSelectStrategyType() {
    return selectAnnot.getStrategyValue();
  }

  public FetchType getFetchType() {
    return selectAnnot.getFetchValue();
  }

  public boolean getEnsureResult() {
    return selectAnnot.getEnsureResultValue();
  }

  public boolean getEnsureResultMapping() {
    return selectAnnot.getEnsureResultMappingValue();
  }

  public MapKeyNamingType getMapKeyNamingType() {
    return selectAnnot.getMapKeyNamingValue();
  }

  public SqlLogType getSqlLogType() {
    return selectAnnot.getSqlLogValue();
  }

  public boolean isExpandable() {
    return entityCtType != null;
  }

  public boolean isResultStream() {
    return resultStream;
  }

  public void setResultStream(boolean resultStream) {
    this.resultStream = resultStream;
  }

  @Override
  public <R> R accept(QueryMetaVisitor<R> visitor) {
    return visitor.visitSqlFileSelectQueryMeta(this);
  }
}
