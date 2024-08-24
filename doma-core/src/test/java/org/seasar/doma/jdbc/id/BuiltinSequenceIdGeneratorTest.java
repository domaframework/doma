package org.seasar.doma.jdbc.id;

import static org.junit.jupiter.api.Assertions.assertEquals;

import example.entity._IdGeneratedEmp;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockResultSet;
import org.seasar.doma.internal.jdbc.mock.RowData;
import org.seasar.doma.jdbc.dialect.PostgresDialect;

public class BuiltinSequenceIdGeneratorTest {

  @Test
  public void test() {
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
    assertEquals(11L, value);
    assertEquals("select nextval('aaa')", config.dataSource.connection.preparedStatement.sql);
  }
}
