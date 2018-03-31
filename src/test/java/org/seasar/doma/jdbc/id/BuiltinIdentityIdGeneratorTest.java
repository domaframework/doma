package org.seasar.doma.jdbc.id;

import example.entity.IdGeneratedEmp;
import example.entity._IdGeneratedEmp;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockResultSet;
import org.seasar.doma.internal.jdbc.mock.RowData;
import org.seasar.doma.jdbc.dialect.PostgresDialect;
import org.seasar.doma.jdbc.entity.EntityDesc;

/** @author taedium */
public class BuiltinIdentityIdGeneratorTest extends TestCase {

  public void test_identitySelectSql() throws Exception {
    MockConfig config = new MockConfig();
    config.setDialect(new PostgresDialect());
    MockResultSet resultSet = config.dataSource.connection.preparedStatement.resultSet;
    resultSet.rows.add(new RowData(11L));

    BuiltinIdentityIdGenerator identityIdGenerator = new BuiltinIdentityIdGenerator();
    IdGenerationConfig idGenerationConfig =
        new IdGenerationConfig(config, _IdGeneratedEmp.getSingletonInternal());
    Long value =
        identityIdGenerator.generatePostInsert(
            idGenerationConfig, config.dataSource.connection.preparedStatement);
    assertEquals(Long.valueOf(11), value);
    assertEquals(
        "select currval(pg_catalog.pg_get_serial_sequence('\"CATA\".\"EMP\"', 'id'))",
        config.dataSource.connection.preparedStatement.sql);
  }

  public void test_identityReservationSql() throws Exception {
    MockConfig config = new MockConfig();
    config.setDialect(new PostgresDialect());
    MockResultSet resultSet = config.dataSource.connection.preparedStatement.resultSet;
    resultSet.rows.add(new RowData(11L));
    resultSet.rows.add(new RowData(12L));
    resultSet.rows.add(new RowData(13L));

    EntityDesc<IdGeneratedEmp> entityDesc = _IdGeneratedEmp.getSingletonInternal();
    BuiltinIdentityIdGenerator identityIdGenerator = new BuiltinIdentityIdGenerator();
    IdGenerationConfig idGenerationConfig =
        new IdGenerationConfig(config, entityDesc, new ReservedIdProvider(config, entityDesc, 3));
    Long value = identityIdGenerator.generatePreInsert(idGenerationConfig);
    assertEquals(Long.valueOf(11), value);
    assertEquals(
        "select nextval(pg_catalog.pg_get_serial_sequence('\"CATA\".\"EMP\"', 'id')) from generate_series(1, 3)",
        config.dataSource.connection.preparedStatement.sql);
    value = identityIdGenerator.generatePreInsert(idGenerationConfig);
    assertEquals(Long.valueOf(12), value);
    value = identityIdGenerator.generatePreInsert(idGenerationConfig);
    assertEquals(Long.valueOf(13), value);

    try {
      identityIdGenerator.generatePreInsert(idGenerationConfig);
      fail();
    } catch (IllegalStateException ignored) {
      System.out.println(ignored);
    }
  }
}
