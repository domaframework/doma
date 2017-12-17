package org.seasar.doma.internal.apt.meta.id;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.internal.apt.reflection.SequenceGeneratorReflection;

/**
 * @author taedium
 * 
 */
public class SequenceIdGeneratorMeta implements IdGeneratorMeta {

    private final SequenceGeneratorReflection sequenceGeneratorReflection;

    public SequenceIdGeneratorMeta(SequenceGeneratorReflection sequenceGeneratorReflection) {
        assertNotNull(sequenceGeneratorReflection);
        this.sequenceGeneratorReflection = sequenceGeneratorReflection;
    }

    public String getQualifiedSequenceName() {
        StringBuilder buf = new StringBuilder();
        String catalogName = sequenceGeneratorReflection.getCatalogValue();
        if (!catalogName.isEmpty()) {
            buf.append(catalogName);
            buf.append(".");
        }
        String schemaName = sequenceGeneratorReflection.getCatalogValue();
        if (!schemaName.isEmpty()) {
            buf.append(schemaName);
            buf.append(".");
        }
        buf.append(sequenceGeneratorReflection.getSequenceValue());
        return buf.toString();
    }

    public long getInitialValue() {
        return sequenceGeneratorReflection.getInitialValueValue();
    }

    public long getAllocationSize() {
        return sequenceGeneratorReflection.getAllocationSizeValue();
    }

    @Override
    public String getIdGeneratorClassName() {
        return sequenceGeneratorReflection.getImplementerValue().toString();
    }

    @Override
    public <R, P> R accept(IdGeneratorMetaVisitor<R, P> visitor, P p) {
        return visitor.visistSequenceIdGeneratorMeta(this, p);
    }
}
