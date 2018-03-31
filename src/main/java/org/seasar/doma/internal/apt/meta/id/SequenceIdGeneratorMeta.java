package org.seasar.doma.internal.apt.meta.id;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.internal.apt.annot.SequenceGeneratorAnnot;

public class SequenceIdGeneratorMeta implements IdGeneratorMeta {

  private final SequenceGeneratorAnnot sequenceGeneratorAnnot;

  public SequenceIdGeneratorMeta(SequenceGeneratorAnnot sequenceGeneratorAnnot) {
    assertNotNull(sequenceGeneratorAnnot);
    this.sequenceGeneratorAnnot = sequenceGeneratorAnnot;
  }

  public String getQualifiedSequenceName() {
    var buf = new StringBuilder();
    var catalogName = sequenceGeneratorAnnot.getCatalogValue();
    if (!catalogName.isEmpty()) {
      buf.append(catalogName);
      buf.append(".");
    }
    var schemaName = sequenceGeneratorAnnot.getCatalogValue();
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
  public <P> void accept(IdGeneratorMetaVisitor<P> visitor, P p) {
    visitor.visitSequenceIdGeneratorMeta(this, p);
  }
}
