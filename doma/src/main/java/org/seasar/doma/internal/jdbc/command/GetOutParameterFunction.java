package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.Assertions.*;

import java.sql.CallableStatement;
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
public class GetOutParameterFunction implements JdbcMappingFunction {

    protected final CallableStatement callableStatement;

    protected final int index;

    public GetOutParameterFunction(CallableStatement callableStatement,
            int index) {
        assertNotNull(callableStatement);
        assertTrue(index > 0, index);
        this.callableStatement = callableStatement;
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
        V value = jdbcType.getValue(callableStatement, index);
        domain.set(value);
        domain.setChanged(false);
    }
}
