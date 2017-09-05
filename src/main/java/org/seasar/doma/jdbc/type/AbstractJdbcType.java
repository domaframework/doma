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
import java.sql.Types;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;

/**
 * A skeletal implementation for the {@link JdbcType} interface .
 * 
 * @param <T>
 *            the basic type
 */
public abstract class AbstractJdbcType<T> implements JdbcType<T> {

    /**
     * the SQL type
     * 
     * @see Types
     */
    protected final int type;

    /**
     * Creates an instance.
     * 
     * @param type
     *            the SQL type
     */
    protected AbstractJdbcType(int type) {
        this.type = type;
    }

    @Override
    public T getValue(ResultSet resultSet, int index) throws SQLException {
        if (resultSet == null) {
            throw new DomaNullPointerException("resultSet");
        }
        if (index < 1) {
            throw new DomaIllegalArgumentException("index", "index < 1");
        }
        T result = doGetValue(resultSet, index);
        if (resultSet.wasNull()) {
            return null;
        }
        return result;
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, T value)
            throws SQLException {
        if (preparedStatement == null) {
            throw new DomaNullPointerException("preparedStatement");
        }
        if (index < 1) {
            throw new DomaIllegalArgumentException("index", "index < 1");
        }
        if (value == null) {
            preparedStatement.setNull(index, type);
        } else {
            doSetValue(preparedStatement, index, value);
        }
    }

    @Override
    public void registerOutParameter(CallableStatement callableStatement, int index)
            throws SQLException {
        if (callableStatement == null) {
            throw new DomaNullPointerException("callableStatement");
        }
        if (index < 1) {
            throw new DomaIllegalArgumentException("index", "index < 1");
        }
        callableStatement.registerOutParameter(index, type);
    }

    @Override
    public T getValue(CallableStatement callableStatement, int index) throws SQLException {
        if (callableStatement == null) {
            throw new DomaNullPointerException("callableStatement");
        }
        if (index < 1) {
            throw new DomaIllegalArgumentException("index", "index < 1");
        }
        T result = doGetValue(callableStatement, index);
        if (callableStatement.wasNull()) {
            return null;
        }
        return result;
    }

    @Override
    public String convertToLogFormat(T value) {
        if (value == null) {
            return "null";
        }
        return doConvertToLogFormat(value);
    }

    protected abstract T doGetValue(ResultSet resultSet, int index) throws SQLException;

    protected abstract void doSetValue(PreparedStatement preparedStatement, int index, T value)
            throws SQLException;

    protected abstract T doGetValue(CallableStatement callableStatement, int index)
            throws SQLException;

    protected abstract String doConvertToLogFormat(T value);
}
