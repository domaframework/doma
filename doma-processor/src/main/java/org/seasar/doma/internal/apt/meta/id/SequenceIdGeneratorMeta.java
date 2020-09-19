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
