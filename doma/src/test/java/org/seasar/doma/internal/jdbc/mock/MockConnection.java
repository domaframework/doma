package org.seasar.doma.internal.jdbc.mock;

import static org.seasar.doma.internal.util.Assertions.*;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

import org.seasar.doma.internal.util.Assertions;


/**
 * 
 * @author taedium
 * 
 */
public class MockConnection extends MockWrapper implements Connection {

    public MockPreparedStatement preparedStatement = new MockPreparedStatement();

    public MockCallableStatement callableStatement = new MockCallableStatement();

    public boolean closed;

    public MockConnection() {
    }

    public MockConnection(MockPreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    public MockConnection(MockCallableStatement callableStatement) {
        this.callableStatement = callableStatement;
        this.preparedStatement = callableStatement;
    }

    @Override
    public void clearWarnings() throws SQLException {
        Assertions.notYetImplemented();

    }

    @Override
    public void close() throws SQLException {
        closed = true;
    }

    @Override
    public void commit() throws SQLException {
        Assertions.notYetImplemented();

    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements)
            throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

    @Override
    public Blob createBlob() throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

    @Override
    public Clob createClob() throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

    @Override
    public NClob createNClob() throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

    @Override
    public Statement createStatement() throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

    @Override
    public Statement createStatement(int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency)
            throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes)
            throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        Assertions.notYetImplemented();
        return false;
    }

    @Override
    public String getCatalog() throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

    @Override
    public int getHoldability() throws SQLException {
        Assertions.notYetImplemented();
        return 0;
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        Assertions.notYetImplemented();
        return 0;
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return closed;
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        Assertions.notYetImplemented();
        return false;
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        Assertions.notYetImplemented();
        return false;
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType,
            int resultSetConcurrency) throws SQLException {
        callableStatement.sql = sql;
        return callableStatement;
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        callableStatement.sql = sql;
        return callableStatement;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency) throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
            throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
            throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames)
            throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        assertTrue(!closed);
        preparedStatement.sql = sql;
        return preparedStatement;
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        Assertions.notYetImplemented();

    }

    @Override
    public void rollback() throws SQLException {
        Assertions.notYetImplemented();

    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        Assertions.notYetImplemented();

    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        Assertions.notYetImplemented();

    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        Assertions.notYetImplemented();

    }

    @Override
    public void setClientInfo(Properties properties)
            throws SQLClientInfoException {
        Assertions.notYetImplemented();

    }

    @Override
    public void setClientInfo(String name, String value)
            throws SQLClientInfoException {
        Assertions.notYetImplemented();

    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        Assertions.notYetImplemented();

    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        Assertions.notYetImplemented();

    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        Assertions.notYetImplemented();

    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        Assertions.notYetImplemented();

    }

}
