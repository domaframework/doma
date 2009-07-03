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
package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.Assertions.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.seasar.doma.domain.Domain;
import org.seasar.doma.internal.jdbc.query.Query;

/**
 * @author taedium
 * 
 */
public class DomainFetcher {

    protected final Query query;

    public DomainFetcher(Query query) throws SQLException {
        assertNotNull(query);
        this.query = query;
    }

    public void fetch(ResultSet resultSet, Domain<?, ?> domain)
            throws SQLException {
        ResultSetMetaData resultSetMeta = resultSet.getMetaData();
        if (resultSetMeta.getColumnCount() > 0) {
            GetValueFunction function = new GetValueFunction(query.getConfig(),
                    resultSet, 1);
            domain.accept(query.getConfig().jdbcMappingVisitor(), function);
        }
    }
}
