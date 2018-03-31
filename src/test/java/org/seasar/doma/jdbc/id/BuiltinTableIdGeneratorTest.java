package org.seasar.doma.jdbc.id;

import example.entity._IdGeneratedEmp;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.*;
import org.seasar.doma.jdbc.dialect.PostgresDialect;

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
    config.dataSource =
        new MockDataSource() {

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
    IdGenerationConfig idGenerationConfig =
        new IdGenerationConfig(config, _IdGeneratedEmp.getSingletonInternal());
    Long value = idGenerator.generatePreInsert(idGenerationConfig);
    assertEquals(Long.valueOf(10), value);
    assertEquals("update aaa set VALUE = VALUE + ? where PK = ?", connection.preparedStatement.sql);
    assertEquals(2, connection.preparedStatement.bindValues.size());
    assertEquals("select VALUE from aaa where PK = ?", connection2.preparedStatement.sql);
    assertEquals(1, connection2.preparedStatement.bindValues.size());
  }
}
