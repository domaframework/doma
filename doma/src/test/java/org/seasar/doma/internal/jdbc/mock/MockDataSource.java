package org.seasar.doma.internal.jdbc.mock;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.seasar.doma.internal.util.Assertions;


/**
 * 
 * @author taedium
 * 
 */
public class MockDataSource extends MockWrapper implements DataSource {

    public MockConnection connection = new MockConnection();

    public MockDataSource() {
    }

    public MockDataSource(MockConnection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connection;
    }

    @Override
    public Connection getConnection(String username, String password)
            throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        Assertions.notYetImplemented();
        return 0;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        Assertions.notYetImplemented();

    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        Assertions.notYetImplemented();

    }

}
