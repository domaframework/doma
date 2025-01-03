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
import org.seasar.doma.internal.apt.annot.SqlProcessorAnnot;
import org.seasar.doma.internal.apt.cttype.BiFunctionCtType;

public class SqlProcessorQueryMeta extends AbstractSqlFileQueryMeta {

  private SqlProcessorAnnot sqlProcessorAnnot;

  private String biFunctionParameterName;

  private BiFunctionCtType biFunctionCtType;

  SqlProcessorQueryMeta(TypeElement daoElement, ExecutableElement methodElement) {
    super(daoElement, methodElement);
  }

  @Override
  public <R> R accept(QueryMetaVisitor<R> visitor) {
    return visitor.visitSqlProcessorQueryMeta(this);
  }

  public SqlProcessorAnnot getSqlProcessorAnnot() {
    return sqlProcessorAnnot;
  }

  public void setSqlProcessorAnnot(SqlProcessorAnnot sqlProcessorAnnot) {
    this.sqlProcessorAnnot = sqlProcessorAnnot;
  }

  public String getBiFunctionParameterName() {
    return biFunctionParameterName;
  }

  public void setBiFunctionParameterName(String biFunctionParameterName) {
    this.biFunctionParameterName = biFunctionParameterName;
  }

  public BiFunctionCtType getBiFunctionCtType() {
    return biFunctionCtType;
  }

  public void setBiFunctionCtType(BiFunctionCtType biFunctionCtType) {
    this.biFunctionCtType = biFunctionCtType;
  }
}
