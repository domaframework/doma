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
package org.seasar.doma.internal.apt.meta.id;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import org.seasar.doma.internal.apt.annot.TableGeneratorAnnot;

public class TableIdGeneratorMeta implements IdGeneratorMeta {

  private final TableGeneratorAnnot tableGeneratorAnnot;

  public TableIdGeneratorMeta(TableGeneratorAnnot tableGeneratorAnnot) {
    assertNotNull(tableGeneratorAnnot);
    this.tableGeneratorAnnot = tableGeneratorAnnot;
  }

  public String getQualifiedTableName() {
    StringBuilder buf = new StringBuilder();
    String catalogName = tableGeneratorAnnot.getCatalogValue();
    if (!catalogName.isEmpty()) {
      buf.append(catalogName);
      buf.append(".");
    }
    String schemaName = tableGeneratorAnnot.getCatalogValue();
    if (!schemaName.isEmpty()) {
      buf.append(schemaName);
      buf.append(".");
    }
    buf.append(tableGeneratorAnnot.getTableValue());
    return buf.toString();
  }

  public String getPkColumnName() {
    return tableGeneratorAnnot.getPkColumnNameValue();
  }

  public String getValueColumnName() {
    return tableGeneratorAnnot.getValueColumnNameValue();
  }

  public String getPkColumnValue() {
    return tableGeneratorAnnot.getPkColumnValueValue();
  }

  public long getInitialValue() {
    return tableGeneratorAnnot.getInitialValueValue();
  }

  public long getAllocationSize() {
    return tableGeneratorAnnot.getAllocationSizeValue();
  }

  @Override
  public String getIdGeneratorClassName() {
    return tableGeneratorAnnot.getImplementerValue().toString();
  }

  @Override
  public <R, P> R accept(IdGeneratorMetaVisitor<R, P> visitor, P p) {
    return visitor.visitTableIdGeneratorMeta(this, p);
  }
}
