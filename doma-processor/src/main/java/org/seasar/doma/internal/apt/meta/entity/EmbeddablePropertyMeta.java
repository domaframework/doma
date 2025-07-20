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
package org.seasar.doma.internal.apt.meta.entity;

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.annot.ColumnAnnot;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;

public final class EmbeddablePropertyMeta implements EmbeddableFieldMeta {

  private final CtType ctType;

  private String name;

  private ColumnAnnot columnAnnot;

  public EmbeddablePropertyMeta(CtType ctType) {
    this.ctType = ctType;
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setColumnAnnot(ColumnAnnot columnAnnot) {
    this.columnAnnot = columnAnnot;
  }

  public String getColumnName() {
    return columnAnnot != null ? columnAnnot.getNameValue() : "";
  }

  public boolean isColumnInsertable() {
    return columnAnnot == null || columnAnnot.getInsertableValue();
  }

  public boolean isColumnUpdatable() {
    return columnAnnot == null || columnAnnot.getUpdatableValue();
  }

  public boolean isColumnQuoteRequired() {
    return columnAnnot != null && columnAnnot.getQuoteValue();
  }

  @Override
  public CtType getCtType() {
    return ctType;
  }

  public TypeMirror getType() {
    return ctType.getType();
  }

  public TypeMirror getBoxedType() {
    return ctType.accept(
        new SimpleCtTypeVisitor<TypeMirror, Void, RuntimeException>() {
          @Override
          public TypeMirror visitBasicCtType(BasicCtType ctType, Void o) {
            return ctType.getBoxedType();
          }

          @Override
          protected TypeMirror defaultAction(CtType ctType, Void o) {
            return ctType.getType();
          }
        },
        null);
  }

  public String getQualifiedName() {
    return ctType.getQualifiedName();
  }
}
