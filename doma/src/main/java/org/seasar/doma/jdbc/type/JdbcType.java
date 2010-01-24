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
package org.seasar.doma.jdbc.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.SqlLogFormatter;

/**
 * {@literal JDBC} の型を表現します。 型ごとに異なる処理を抽象化します。
 * <p>
 * このインタフェースの実装はスレッドセーフでなければいけません。
 * <p>
 * 
 * @author taedium
 * 
 * @param <T>
 *            JDBCで扱う型
 */
public interface JdbcType<T> extends SqlLogFormatter<T> {

    /**
     * {@link ResultSet} から値を取得します。
     * 
     * @param resultSet
     * @param index
     * @return 値
     * @throws DomaNullPointerException
     *             {@code resultSet} が {@code null} の場合
     * @throws DomaIllegalArgumentException
     *             {@code index} が {@literal 1} 以下の場合
     * @throws SQLException
     */
    T getValue(ResultSet resultSet, int index) throws DomaNullPointerException,
            SQLException;

    /**
     * {@link PreparedStatement} に値を設定します。
     * 
     * @param preparedStatement
     * @param index
     * @param value
     * @throws DomaNullPointerException
     *             {@code preparedStatement} が {@code null} の場合
     * @throws DomaIllegalArgumentException
     *             {@code index} が {@literal 1} 以下の場合
     * @throws SQLException
     */
    void setValue(PreparedStatement preparedStatement, int index, T value)
            throws SQLException;

    /**
     * {@link CallableStatement} にOUTパラメータを登録します。
     * 
     * @param callableStatement
     * @param index
     * @throws DomaNullPointerException
     *             {@code callableStatement} が {@code null} の場合
     * @throws DomaIllegalArgumentException
     *             {@code index} が {@literal 1} 以下の場合
     * @throws SQLException
     */
    void registerOutParameter(CallableStatement callableStatement, int index)
            throws SQLException;

    /**
     * {@link CallableStatement} から値を取得します。
     * 
     * @param callableStatement
     * @param index
     * @return 値
     * @throws DomaNullPointerException
     *             {@code callableStatement} が {@code null} の場合
     * @throws DomaIllegalArgumentException
     *             {@code index} が {@literal 1} 以下の場合
     * @throws SQLException
     */
    T getValue(CallableStatement callableStatement, int index)
            throws SQLException;

}
