package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.Assertions.*;

import java.sql.ResultSet;
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
public class GetValueFunction implements JdbcMappingFunction {

    protected final ResultSet resultSet;

    protected final int index;

    public GetValueFunction(ResultSet resultSet, int index) {
        assertNotNull(resultSet);
        assertTrue(index > 0, index);
        this.resultSet = resultSet;
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
        V value = jdbcType.getValue(resultSet, index);
        domain.set(value);
        domain.setChanged(false);
    }
}
