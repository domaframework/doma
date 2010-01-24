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

/**
 * {@link Long} 用の {@link JdbcType} の実装です。
 * 
 * @author taedium
 * 
 */
public class LongType extends AbstractJdbcType<Long> {

    public LongType() {
        super(Types.BIGINT);
    }

    @Override
    protected Long doGetValue(ResultSet resultSet, int index)
            throws SQLException {
        return resultSet.getLong(index);
    }

    @Override
    protected void doSetValue(PreparedStatement preparedStatement, int index,
            Long value) throws SQLException {
        preparedStatement.setLong(index, value);
    }

    @Override
    protected Long doGetValue(CallableStatement callableStatement, int index)
            throws SQLException {
        return callableStatement.getLong(index);
    }

    @Override
    protected String doConvertToLogFormat(Long value) {
        return String.valueOf(value);
    }

}
