package org.seasar.doma.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertTrue;

import java.sql.CallableStatement;
import java.sql.SQLException;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.JdbcMappingFunction;
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.wrapper.Wrapper;

/**
 * A registrar of an OUT parameter.
 */
public class JdbcOutParameterRegistrar implements JdbcMappingFunction {

    protected final CallableStatement callableStatement;

    protected final int index;

    public JdbcOutParameterRegistrar(CallableStatement callableStatement, int index) {
        assertNotNull(callableStatement);
        assertTrue(index > 0, index);
        this.callableStatement = callableStatement;
        this.index = index;
    }

    @Override
    public <R, V> R apply(Wrapper<V> wrapper, JdbcType<V> jdbcType) throws SQLException {
        if (jdbcType == null) {
            throw new DomaNullPointerException("jdbcType");
        }
        jdbcType.registerOutParameter(callableStatement, index);
        return null;
    }
}
