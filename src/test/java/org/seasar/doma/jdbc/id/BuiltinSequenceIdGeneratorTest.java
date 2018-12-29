package org.seasar.doma.jdbc.id;

import example.entity._IdGeneratedEmp;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockResultSet;
import org.seasar.doma.internal.jdbc.mock.RowData;
import org.seasar.doma.jdbc.dialect.PostgresDialect;

public class BuiltinSequenceIdGeneratorTest extends TestCase {

  public void test() throws Exception {
    MockConfig config = new MockConfig();
    config.setDialect(new PostgresDialect());
    MockResultSet resultSet = config.dataSource.connection.preparedStatement.resultSet;
    resultSet.rows.add(new RowData(11L));

    BuiltinSequenceIdGenerator idGenerator = new BuiltinSequenceIdGenerator();
    idGenerator.setQualifiedSequenceName("aaa");
    idGenerator.setInitialValue(1);
    idGenerator.setAllocationSize(1);
    IdGenerationConfig idGenerationConfig =
        new IdGenerationConfig(config, _IdGeneratedEmp.getSingletonInternal());
    Long value = idGenerator.generatePreInsert(idGenerationConfig);
    assertEquals(new Long(11), value);
    assertEquals("select nextval('aaa')", config.dataSource.connection.preparedStatement.sql);
  }
}
