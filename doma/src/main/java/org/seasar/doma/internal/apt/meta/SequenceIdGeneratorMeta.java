package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.Assertions.*;

import org.seasar.doma.internal.jdbc.id.SequenceIdGenerator;

/**
 * @author taedium
 * 
 */
public class SequenceIdGeneratorMeta implements IdGeneratorMeta {

    protected final String qualifiedSequenceName;

    protected final int initialValue;

    protected final int allocationSize;

    public SequenceIdGeneratorMeta(String qualifiedSequenceName,
            int initialValue, int allocationSize) {
        assertNotNull(qualifiedSequenceName);
        this.qualifiedSequenceName = qualifiedSequenceName;
        this.initialValue = initialValue;
        this.allocationSize = allocationSize;
    }

    public String getQualifiedSequenceName() {
        return qualifiedSequenceName;
    }

    public int getInitialValue() {
        return initialValue;
    }

    public int getAllocationSize() {
        return allocationSize;
    }

    @Override
    public String getIdGeneratorClassName() {
        return SequenceIdGenerator.class.getName();
    }

    @Override
    public <R, P> R accept(IdGeneratorMetaVisitor<R, P> visitor, P p) {
        return visitor.visistSequenceIdGeneratorMeta(this, p);
    }
}
