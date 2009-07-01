package org.seasar.doma.internal.jdbc.query;

import static org.seasar.doma.internal.util.Assertions.*;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;

import org.seasar.doma.domain.Domain;

/**
 * @author taedium
 * 
 */
public class BlobCreateQuery<R extends Domain<Blob, ?>> extends
        AbstractCreateQuery<Blob, R> {

    public void compile() {
        assertNotNull(config, callerClassName, callerMethodName, result);
    }

    @Override
    public R create(Connection connection) throws SQLException {
        Blob blob = connection.createBlob();
        result.set(blob);
        return result;
    }

}
