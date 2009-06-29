package org.seasar.doma.internal.jdbc.id;

import org.seasar.doma.internal.jdbc.id.IdGenerationConfig;
import org.seasar.doma.internal.jdbc.id.IdentityIdGenerator;
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
public class IdentityIdGeneratorTest extends TestCase {

    public void test_identitySelectSql() throws Exception {
        MockConfig config = new MockConfig();
        config.setDialect(new PostgresDialect());
        MockResultSet resultSet = config.dataSource.connection.preparedStatement.resultSet;
        resultSet.rows.add(new RowData(11L));

        IdentityIdGenerator identityIdGenerator = new IdentityIdGenerator();
        IdGenerationConfig idGenerationConfig = new IdGenerationConfig(config,
                new Emp_(), "EMP", "ID");
        Long value = identityIdGenerator
                .generatePostInsert(idGenerationConfig, config.dataSource.connection.preparedStatement);
        assertEquals(new Long(11), value);
        assertEquals("select currval('EMP_ID_seq')", config.dataSource.connection.preparedStatement.sql);
    }
}
