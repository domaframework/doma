/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
 * {@link DriverManager#getConnection(String, Properties)}を使用して
 * {@link Connection} を返す単純なデータソースです。
 * <p>
 * ごく単純なアプリケーションやサンプルプログラムで使用されることを想定しています。 通常は、{@literal JTA}トランザクションと連携する
 * 実装やコネクションプーリングを提供する {@link DataSource} の実装を使用してください。
 * 
 * @author taedium
 * 
 */
public class SimpleDataSource implements DataSource {

    /**
     * コネクションが確立できない場合の {@code SQLState} コードです。
     */
    protected static final String UNABLE_TO_ESTABLISH_CONNECTION = "08001";

    /**
     * {@code jdbc:subprotocol:subname}という形式のデータベースへの接続URLです。
     */
    protected String url;

    /**
     * データベースの接続ユーザーです。
     */
    protected String user;

    /**
     * データベースの接続パスワードです。
     */
    protected String password;

    /**
     * JDBCドライバへのプロパティです。
     */
    protected final Properties properties = new Properties();

    /**
     * データベースへの接続URLを返します。
     * 
     * @return データベースへの接続URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * データベースの接続URLを設定します。
     * 
     * @param url
     *            {@code jdbc:subprotocol:subname}という形式のデータベースへの接続URL
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * データベースの接続ユーザーを返します。
     * 
     * @return データベースの接続ユーザー
     */
    public String getUser() {
        return user;
    }

    /**
     * データベースの接続ユーザーを設定します。
     * 
     * @param user
     *            データベースの接続ユーザー
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * データベースの接続パスワードを返します。
     * 
     * @return データベースの接続パスワード
     */
    public String getPassword() {
        return password;
    }

    /**
     * データベースの接続パスワードを設定します。
     * 
     * @param password
     *            データベースの接続パスワード
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * JDBCドライバへのプロパティを追加します。
     * 
     * @param key
     *            プロパティのキー
     * @param value
     *            プロパティの値
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
    public Connection getConnection(String user, String password)
            throws SQLException {
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

    /**
     * 内部的にコネクションを返します。
     * 
     * @param info
     *            JDBCドライバへのプロパティ
     * @return コネクション
     * @throws SQLException
     *             SQLに関する例外が発生した場合
     */
    protected Connection getConnectionInternal(Properties info)
            throws SQLException {
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
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException("unwrap method is unsupported.");
    }

    @SuppressWarnings("all")
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }
}
