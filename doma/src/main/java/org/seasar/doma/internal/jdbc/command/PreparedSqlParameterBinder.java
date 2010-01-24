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
package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.seasar.doma.internal.jdbc.query.Query;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlParameter;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.wrapper.Wrapper;

/**
 * 
 * @author taedium
 * 
 */
public class PreparedSqlParameterBinder implements
        ParameterBinder<PreparedStatement, PreparedSqlParameter> {

    protected final Query query;

    public PreparedSqlParameterBinder(Query query) {
        assertNotNull(query);
        this.query = query;
    }

    @Override
    public void bind(PreparedStatement preparedStatement,
            List<? extends PreparedSqlParameter> paramters) throws SQLException {
        assertNotNull(preparedStatement, paramters);
        int index = 1;
        JdbcMappingVisitor jdbcMappingVisitor = query.getConfig().getDialect()
                .getJdbcMappingVisitor();
        for (PreparedSqlParameter parameter : paramters) {
            SetValueFunction function = new SetValueFunction(preparedStatement,
                    index);
            Wrapper<?> wrapper = parameter.getWrapper();
            wrapper.accept(jdbcMappingVisitor, function);
            index++;
        }
    }

}
