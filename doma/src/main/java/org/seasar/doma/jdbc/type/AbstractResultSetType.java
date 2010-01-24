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

import org.seasar.doma.jdbc.JdbcUnsupportedOperationException;

/**
 * {@link ResultSet} 用の {@link JdbcType} のための骨格実装です。
 * 
 * @author taedium
 * 
 */
public abstract class AbstractResultSetType extends AbstractJdbcType<ResultSet> {

    /**
     * SQL型を指定してインスタンスを構築します。
     * 
     * @param type
     *            SQL型
     */
    protected AbstractResultSetType(int type) {
        super(type);
    }

    /**
     * サポートしません。
     */
    @Override
    public ResultSet getValue(ResultSet resultSet, int index)
            throws SQLException {
        throw new JdbcUnsupportedOperationException(getClass().getName(),
                "getValue");
    }

    /**
     * サポートしません。
     */
    @Override
    public void setValue(PreparedStatement preparedStatement, int index,
            ResultSet value) throws SQLException {
        throw new JdbcUnsupportedOperationException(getClass().getName(),
                "setValue");
    }

    /**
     * {@code null} を返します。
     */
    @Override
    protected ResultSet doGetValue(ResultSet resultSet, int index)
            throws SQLException {
        return null;
    }

    /**
     * 何も行いません。
     */
    @Override
    protected void doSetValue(PreparedStatement preparedStatement, int index,
            ResultSet value) throws SQLException {
    }

    @Override
    protected ResultSet doGetValue(CallableStatement callableStatement,
            int index) throws SQLException {
        Object resultSet = callableStatement.getObject(index);
        return (ResultSet) resultSet;
    }

    @Override
    protected String doConvertToLogFormat(ResultSet value) {
        return value.toString();
    }

}
