package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.JdbcMappingFunction;
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.wrapper.Wrapper;

/**
 * 
 * @author taedium
 * 
 */
public class JdbcValueSetter implements JdbcMappingFunction {

    protected final PreparedStatement preparedStatement;

    protected final int index;

    public JdbcValueSetter(PreparedStatement preparedStatement, int index) {
        assertNotNull(preparedStatement);
        assertTrue(index > 0, index);
        this.preparedStatement = preparedStatement;
        this.index = index;
    }

    @Override
    public <R, V> R apply(Wrapper<V> wrapper, JdbcType<V> jdbcType) throws SQLException {
        if (wrapper == null) {
            throw new DomaNullPointerException("wrapper");
        }
        if (jdbcType == null) {
            throw new DomaNullPointerException("jdbcType");
        }
        jdbcType.setValue(preparedStatement, index, wrapper.get());
        return null;
    }
}
