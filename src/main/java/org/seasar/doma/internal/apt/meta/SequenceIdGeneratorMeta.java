package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import org.seasar.doma.internal.apt.mirror.SequenceGeneratorMirror;

/** @author taedium */
public class SequenceIdGeneratorMeta implements IdGeneratorMeta {

  protected final SequenceGeneratorMirror sequenceGeneratorMirror;

  public SequenceIdGeneratorMeta(SequenceGeneratorMirror sequenceGeneratorMirror) {
    assertNotNull(sequenceGeneratorMirror);
    this.sequenceGeneratorMirror = sequenceGeneratorMirror;
  }

  public String getQualifiedSequenceName() {
    StringBuilder buf = new StringBuilder();
    String catalogName = sequenceGeneratorMirror.getCatalogValue();
    if (!catalogName.isEmpty()) {
      buf.append(catalogName);
      buf.append(".");
    }
    String schemaName = sequenceGeneratorMirror.getCatalogValue();
    if (!schemaName.isEmpty()) {
      buf.append(schemaName);
      buf.append(".");
    }
    buf.append(sequenceGeneratorMirror.getSequenceValue());
    return buf.toString();
  }

  public long getInitialValue() {
    return sequenceGeneratorMirror.getInitialValueValue();
  }

  public long getAllocationSize() {
    return sequenceGeneratorMirror.getAllocationSizeValue();
  }

  @Override
  public String getIdGeneratorClassName() {
    return sequenceGeneratorMirror.getImplementerValue().toString();
  }

  @Override
  public <R, P> R accept(IdGeneratorMetaVisitor<R, P> visitor, P p) {
    return visitor.visistSequenceIdGeneratorMeta(this, p);
  }
}
