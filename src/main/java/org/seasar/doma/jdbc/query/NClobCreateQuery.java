package org.seasar.doma.jdbc.query;

import java.sql.Connection;
import java.sql.NClob;
import java.sql.SQLException;

public class NClobCreateQuery extends AbstractCreateQuery<NClob> {

    @Override
    public NClob create(Connection connection) throws SQLException {
        return connection.createNClob();
    }

}
