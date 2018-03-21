package org.seasar.doma.jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.seasar.doma.message.Message;

/**
 * A simple data source that creates {@link Connection} by using
 * {@link DriverManager#getConnection(String, Properties)}.
 * <p>
 * It is assumed that this object is used only in simple applications. In
 * production environment, you should use the {@link DataSource} implementation
 * that provides connection pooling.
 */
public class SimpleDataSource implements DataSource {

    /**
     * the {@code SQLState} code to indicate that the this data source is unable
     * to establish the connection
     */
    protected static final String UNABLE_TO_ESTABLISH_CONNECTION = "08001";

    protected String url;

    protected String user;

    protected String password;

    protected final Properties properties = new Properties();

    /**
     * Returns the database url.
     * 
     * @return the database url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the database url of the form
     * <code>jdbc:<em>subprotocol</em>:<em>subname</em></code>.
     * 
     * @param url
     *            the database url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Returns the database user.
     * 
     * @return the database user
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the database user.
     * 
     * @param user
     *            the database user
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Returns the database password
     * 
     * @return the database password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the database password
     * 
     * @param password
     *            the database password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Add a JDBC property.
     * 
     * @param key
     *            the key of the property
     * @param value
     *            the value of the property
     */
    public void addProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    @Override
    public int getLoginTimeout() {
        return DriverManager.getLoginTimeout();
    }

    @Override
    public void setLoginTimeout(int seconds) {
        DriverManager.setLoginTimeout(seconds);
    }

    @Override
    public Connection getConnection() throws SQLException {
        Properties info = new Properties();
        info.putAll(properties);
        if (user != null) {
            info.setProperty("user", user);
        }
        if (password != null) {
            info.setProperty("password", password);
        }
        return getConnectionInternal(info);
    }

    @Override
    public Connection getConnection(String user, String password) throws SQLException {
        Properties info = new Properties();
        info.putAll(properties);
        if (user != null) {
            info.setProperty("user", user);
        }
        if (password != null) {
            info.setProperty("password", password);
        }
        return getConnectionInternal(info);
    }

    protected Connection getConnectionInternal(Properties info) throws SQLException {
        if (url == null) {
            throw new SQLException(Message.DOMA5002.getMessage());
        }
        try {
            return DriverManager.getConnection(url, info);
        } catch (SQLException e) {
            if (UNABLE_TO_ESTABLISH_CONNECTION.equals(e.getSQLState())) {
                throw new SQLException(Message.DOMA5001.getMessage(),
                        UNABLE_TO_ESTABLISH_CONNECTION, e);
            }
            throw e;
        }
    }

    @Override
    public PrintWriter getLogWriter() {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) {
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        return iface != null && iface.isAssignableFrom(getClass());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface == null) {
            throw new SQLException("iface must not be null");
        }
        if (iface.isAssignableFrom(getClass())) {
            return (T) this;
        }
        throw new SQLException("cannot unwrap to " + iface.getName());
    }

    @SuppressWarnings("all")
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }
}
