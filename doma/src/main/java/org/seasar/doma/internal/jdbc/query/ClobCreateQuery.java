package org.seasar.doma.internal.jdbc.query;

import static org.seasar.doma.internal.util.Assertions.*;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;

import org.seasar.doma.domain.Domain;

/**
 * @author taedium
 * 
 */
public class ClobCreateQuery<R extends Domain<Clob, ?>> extends
        AbstractCreateQuery<Clob, R> {

    public void compile() {
        assertNotNull(config, callerClassName, callerMethodName, result);
    }

    @Override
    public R create(Connection connection) throws SQLException {
        Clob clob = connection.createClob();
        result.set(clob);
        return result;
    }
}
