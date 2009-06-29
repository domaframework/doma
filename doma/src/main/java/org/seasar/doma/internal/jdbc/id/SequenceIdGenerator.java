package org.seasar.doma.internal.jdbc.id;

import org.seasar.doma.GenerationType;
import org.seasar.doma.jdbc.Sql;

/**
 * @author taedium
 * 
 */
public class SequenceIdGenerator extends AbstractPreAllocateIdGenerator {

    protected final String qualifiedSequenceName;

    public SequenceIdGenerator(String qualifiedSequenceName, int initialValue,
            int allocationSize) {
        super(initialValue, allocationSize);
        this.qualifiedSequenceName = qualifiedSequenceName;
    }

    @Override
    protected long getNewInitialValue(IdGenerationConfig config) {
        Sql<?> sql = config.getDialect()
                .getSequenceNextValSql(qualifiedSequenceName, allocationSize);
        return getGeneratedValue(config, sql);
    }

    @Override
    public GenerationType getGenerationType() {
        return GenerationType.SEQUENCE;
    }
}
