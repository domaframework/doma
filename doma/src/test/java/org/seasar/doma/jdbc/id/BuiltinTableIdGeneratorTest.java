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
package org.seasar.doma.jdbc.id;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockConnection;
import org.seasar.doma.internal.jdbc.mock.MockDataSource;
import org.seasar.doma.internal.jdbc.mock.MockResultSet;
import org.seasar.doma.internal.jdbc.mock.RowData;
import org.seasar.doma.jdbc.dialect.PostgresDialect;

import example.entity._Emp;

/**
 * @author taedium
 * 
 */
public class BuiltinTableIdGeneratorTest extends TestCase {

    public void test() throws Exception {
        MockConfig config = new MockConfig();
        config.setDialect(new PostgresDialect());
        MockConnection connection = new MockConnection();
        MockConnection connection2 = new MockConnection();
        MockResultSet resultSet2 = connection2.preparedStatement.resultSet;
        resultSet2.rows.add(new RowData(11L));
        final LinkedList<MockConnection> connections = new LinkedList<MockConnection>();
        connections.add(connection);
        connections.add(connection2);
        config.dataSource = new MockDataSource() {

            @Override
            public Connection getConnection() throws SQLException {
                return connections.pop();
            }
        };

        BuiltinTableIdGenerator idGenerator = new BuiltinTableIdGenerator();
        idGenerator.setQualifiedTableName("aaa");
        idGenerator.setPkColumnName("PK");
        idGenerator.setPkColumnValue("EMP_ID");
        idGenerator.setValueColumnName("VALUE");
        idGenerator.setInitialValue(1);
        idGenerator.setAllocationSize(1);
        idGenerator.initialize();
        IdGenerationConfig idGenerationConfig = new IdGenerationConfig(config,
                _Emp.getSingletonInternal(), "EMP", "ID");
        Long value = idGenerator.generatePreInsert(idGenerationConfig);
        assertEquals(new Long(10), value);
        assertEquals("update aaa set VALUE = VALUE + ? where PK = ?",
                connection.preparedStatement.sql);
        assertEquals(2, connection.preparedStatement.bindValues.size());
        assertEquals("select VALUE from aaa where PK = ?",
                connection2.preparedStatement.sql);
        assertEquals(1, connection2.preparedStatement.bindValues.size());
    }

}
