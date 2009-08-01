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
package org.seasar.doma.jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

/**
 * {@link DriverManager#getConnection(String, Properties)}を使用して
 * {@link Connection} を返す単純なデータソースです。
 * <p>
 * ごく単純なアプリケーションやサンプルプログラムで使用されることを想定しています。 通常は、{@literal JTA}トランザクションと連携する
 * {@link DataSource} の実装を使用してください。
 * 
 * @author taedium
 * 
 */
public class SimpleDataSource implements DataSource {

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
        return DriverManager.getConnection(url, info);
    }

    @Override
    public Connection getConnection(String username, String password)
            throws SQLException {
        Properties info = new Properties();
        info.putAll(properties);
        if (user != null) {
            info.setProperty("user", user);
        }
        if (password != null) {
            info.setProperty("password", password);
        }
        return DriverManager.getConnection(url, info);
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

}
