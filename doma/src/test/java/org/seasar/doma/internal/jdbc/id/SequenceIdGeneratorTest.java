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
