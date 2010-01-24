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
import java.sql.Time;
import java.sql.Types;

/**
 * {@link Time} 用の {@link JdbcType} の実装です。
 * 
 * @author taedium
 * 
 */
public class TimeType extends AbstractJdbcType<Time> {

    public TimeType() {
        super(Types.TIME);
    }

    @Override
    protected Time doGetValue(ResultSet resultSet, int index)
            throws SQLException {
        return resultSet.getTime(index);
    }

    @Override
    protected void doSetValue(PreparedStatement preparedStatement, int index,
            Time value) throws SQLException {
        preparedStatement.setTime(index, value);
    }

    @Override
    protected Time doGetValue(CallableStatement callableStatement, int index)
            throws SQLException {
        return callableStatement.getTime(index);
    }

    @Override
    protected String doConvertToLogFormat(Time value) {
        return "'" + value + "'";
    }

}
