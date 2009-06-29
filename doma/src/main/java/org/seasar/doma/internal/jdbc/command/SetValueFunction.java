package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.Assertions.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.domain.Domain;
import org.seasar.doma.jdbc.JdbcMappingFunction;
import org.seasar.doma.jdbc.JdbcType;


/**
 * 
 * @author taedium
 * 
 */
public class SetValueFunction implements JdbcMappingFunction {

    protected final PreparedStatement preparedStatement;

    protected final int index;

    public SetValueFunction(PreparedStatement preparedStatement, int index) {
        assertNotNull(preparedStatement);
        assertTrue(index > 0, index);
        this.preparedStatement = preparedStatement;
        this.index = index;
    }

    @Override
    public <V> void apply(Domain<V, ?> domain, JdbcType<V> jdbcType)
            throws SQLException {
        if (domain == null) {
            throw new DomaIllegalArgumentException("domain", domain);
        }
        if (jdbcType == null) {
            throw new DomaIllegalArgumentException("jdbcType", jdbcType);
        }
        jdbcType.setValue(preparedStatement, index, domain.get());
    }
}
