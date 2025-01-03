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

import org.seasar.doma.internal.apt.annot.SequenceGeneratorAnnot;

public class SequenceIdGeneratorMeta implements IdGeneratorMeta {

  private final SequenceGeneratorAnnot sequenceGeneratorAnnot;

  public SequenceIdGeneratorMeta(SequenceGeneratorAnnot sequenceGeneratorAnnot) {
    assertNotNull(sequenceGeneratorAnnot);
    this.sequenceGeneratorAnnot = sequenceGeneratorAnnot;
  }

  public String getQualifiedSequenceName() {
    StringBuilder buf = new StringBuilder();
    String catalogName = sequenceGeneratorAnnot.getCatalogValue();
    if (!catalogName.isEmpty()) {
      buf.append(catalogName);
      buf.append(".");
    }
    String schemaName = sequenceGeneratorAnnot.getSchemaValue();
    if (!schemaName.isEmpty()) {
      buf.append(schemaName);
      buf.append(".");
    }
    buf.append(sequenceGeneratorAnnot.getSequenceValue());
    return buf.toString();
  }

  public long getInitialValue() {
    return sequenceGeneratorAnnot.getInitialValueValue();
  }

  public long getAllocationSize() {
    return sequenceGeneratorAnnot.getAllocationSizeValue();
  }

  @Override
  public String getIdGeneratorClassName() {
    return sequenceGeneratorAnnot.getImplementerValue().toString();
  }

  @Override
  public <R, P> R accept(IdGeneratorMetaVisitor<R, P> visitor, P p) {
    return visitor.visitSequenceIdGeneratorMeta(this, p);
  }
}
