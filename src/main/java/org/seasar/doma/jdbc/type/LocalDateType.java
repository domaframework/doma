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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;

/**
 * A JDBC type for {@link Types#DATE} and {@link LocalDate}.
 */
public class LocalDateType extends AbstractJdbcType<LocalDate> {

    public LocalDateType() {
        super(Types.DATE);
    }

    @Override
    public LocalDate doGetValue(ResultSet resultSet, int index) throws SQLException {
        Date date = resultSet.getDate(index);
        return date != null ? date.toLocalDate() : null;
    }

    @Override
    protected void doSetValue(PreparedStatement preparedStatement, int index, LocalDate value)
            throws SQLException {
        preparedStatement.setDate(index, Date.valueOf(value));
    }

    @Override
    protected LocalDate doGetValue(CallableStatement callableStatement, int index)
            throws SQLException {
        Date date = callableStatement.getDate(index);
        return date != null ? date.toLocalDate() : null;
    }

    @Override
    protected String doConvertToLogFormat(LocalDate value) {
        return "'" + Date.valueOf(value) + "'";
    }

}
