package org.seasar.doma.internal.jdbc.query;

import static org.seasar.doma.internal.util.Assertions.*;

import java.sql.Connection;
import java.sql.NClob;
import java.sql.SQLException;

import org.seasar.doma.domain.Domain;

/**
 * @author taedium
 * 
 */
public class NClobCreateQuery<R extends Domain<NClob, ?>> extends
        AbstractCreateQuery<NClob, R> {

    public void compile() {
        assertNotNull(config, callerClassName, callerMethodName, result);
    }

    @Override
    public R create(Connection connection) throws SQLException {
        NClob nclob = connection.createNClob();
        result.set(nclob);
        return result;
    }

}
