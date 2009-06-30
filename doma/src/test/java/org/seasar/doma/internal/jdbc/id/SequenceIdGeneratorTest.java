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
package org.seasar.doma.internal.jdbc.id;

import org.seasar.doma.internal.jdbc.id.IdGenerationConfig;
import org.seasar.doma.internal.jdbc.id.SequenceIdGenerator;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockResultSet;
import org.seasar.doma.internal.jdbc.mock.RowData;
import org.seasar.doma.jdbc.dialect.PostgresDialect;

import junit.framework.TestCase;
import example.entity.Emp_;

/**
 * @author taedium
 * 
 */
public class SequenceIdGeneratorTest extends TestCase {

    public void test() throws Exception {
        MockConfig config = new MockConfig();
        config.setDialect(new PostgresDialect());
        MockResultSet resultSet = config.dataSource.connection.preparedStatement.resultSet;
        resultSet.rows.add(new RowData(11L));

        SequenceIdGenerator idGenerator = new SequenceIdGenerator("aaa", 1, 1);
        IdGenerationConfig idGenerationConfig = new IdGenerationConfig(config,
                new Emp_(), "EMP", "ID");
        Long value = idGenerator.generatePreInsert(idGenerationConfig);
        assertEquals(new Long(11), value);
        assertEquals("select nextval('aaa')", config.dataSource.connection.preparedStatement.sql);
    }
}
