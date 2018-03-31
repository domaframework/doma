package org.seasar.doma.jdbc.id;

import example.entity._IdGeneratedEmp;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.RowData;
import org.seasar.doma.jdbc.dialect.PostgresDialect;

public class BuiltinSequenceIdGeneratorTest extends TestCase {

  public void test() throws Exception {
    var config = new MockConfig();
    config.setDialect(new PostgresDialect());
    var resultSet = config.dataSource.connection.preparedStatement.resultSet;
    resultSet.rows.add(new RowData(11L));

    var idGenerator = new BuiltinSequenceIdGenerator();
    idGenerator.setQualifiedSequenceName("aaa");
    idGenerator.setInitialValue(1);
    idGenerator.setAllocationSize(1);
    var idGenerationConfig = new IdGenerationConfig(config, _IdGeneratedEmp.getSingletonInternal());
    var value = idGenerator.generatePreInsert(idGenerationConfig);
    assertEquals(Long.valueOf(11), value);
    assertEquals("select nextval('aaa')", config.dataSource.connection.preparedStatement.sql);
  }
}
