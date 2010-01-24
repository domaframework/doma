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

import java.sql.SQLException;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.wrapper.Wrapper;

/**
 * {@link Wrapper} と {@link JdbcType} をマッピングする処理を表します。
 * <p>
 * このインタフェースの実装はスレッドセーフであることを要求されません。
 * 
 * @author taedium
 * 
 */
public interface JdbcMappingFunction {

    /**
     * この処理を適用します。
     * 
     * @param <R>
     *            戻り値の型
     * @param <V>
     *            マッピング対象の値の型
     * @param wrapper
     *            ラッパー
     * @param jdbcType
     *            JDBC型
     * @return マッピング処理の結果
     * @throws DomaNullPointerException
     *             いずれかの引数が {@code null} の場合
     * @throws SQLException
     *             SQLに関する例外が発生した場合
     */
    <R, V> R apply(Wrapper<V> wrapper, JdbcType<V> jdbcType)
            throws SQLException;
}
