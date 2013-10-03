/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.it;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.seasar.extension.dbcp.ConnectionPool;
import org.seasar.extension.dbcp.ConnectionWrapper;

public class ItDataSource implements DataSource {

    protected final ConnectionPool connectionPool;

    public ItDataSource(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getConnectionInternal();
    }

    @Override
    public Connection getConnection(String username, String password)
            throws SQLException {
        return getConnectionInternal();
    }

    protected Connection getConnectionInternal() throws SQLException {
        Connection connection = connectionPool.checkOut();
        if (ConnectionWrapper.class.isInstance(connection)) {
            return new ItConnection(ConnectionWrapper.class.cast(connection));
        }
        return connection;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new UnsupportedOperationException();
    }

    
}
