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
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.internal.apt.annot.ProcedureAnnot;
import org.seasar.doma.jdbc.SqlLogType;

public class AutoProcedureQueryMeta extends AutoModuleQueryMeta {

  private ProcedureAnnot procedureAnnot;

  public AutoProcedureQueryMeta(TypeElement daoElement, ExecutableElement methodElement) {
    super(daoElement, methodElement);
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
