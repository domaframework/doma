package org.seasar.doma.jdbc.query;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLXML;

public class SQLXMLCreateQuery extends AbstractCreateQuery<SQLXML> {

    @Override
    public SQLXML create(Connection connection) throws SQLException {
        return connection.createSQLXML();
    }

}
