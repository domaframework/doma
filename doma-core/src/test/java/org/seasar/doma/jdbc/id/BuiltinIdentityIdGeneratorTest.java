package org.seasar.doma.jdbc.id;

import static org.junit.jupiter.api.Assertions.assertEquals;

import example.entity._IdGeneratedEmp;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockResultSet;
import org.seasar.doma.internal.jdbc.mock.RowData;
import org.seasar.doma.jdbc.dialect.PostgresDialect;

public class BuiltinIdentityIdGeneratorTest {

  @Test
  public void test_identitySelectSql() {
    MockConfig config = new MockConfig();
    config.setDialect(
        new PostgresDialect() {
          @Override
          public boolean supportsAutoGeneratedKeys() {
            return false;
          }
        });
    MockResultSet resultSet = config.dataSource.connection.preparedStatement.resultSet;
    resultSet.rows.add(new RowData(11L));

    BuiltinIdentityIdGenerator identityIdGenerator = new BuiltinIdentityIdGenerator();
    IdGenerationConfig idGenerationConfig =
        new IdGenerationConfig(config, _IdGeneratedEmp.getSingletonInternal());
    Long value =
        identityIdGenerator.generatePostInsert(
            idGenerationConfig, config.dataSource.connection.preparedStatement);
    assertEquals(11L, value);
    assertEquals(
        "select currval(pg_catalog.pg_get_serial_sequence('\"CATA\".\"EMP\"', 'id'))",
        config.dataSource.connection.preparedStatement.sql);
  }
}
