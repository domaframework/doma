package org.seasar.doma.it.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.DelegatingConfig;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.doma.it.dao.EmployeeDaoImpl;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlBuilderSettings;

@ExtendWith(IntegrationTestEnvironment.class)
public class SqlSelectTest {

  @Test
  public void inList(Config config) {
    EmployeeDao dao = new EmployeeDaoImpl(config);
    List<Employee> list = dao.selectByNames(List.of("SMITH", "KING", "WARD"));
    assertEquals(3, list.size());
  }

  @Test
  public void inList_padding(Config config) {
    Config newConfig =
        new DelegatingConfig(config) {

          @Override
          public SqlBuilderSettings getSqlBuilderSettings() {
            return new SqlBuilderSettings() {

              @Override
              public boolean requiresInListPadding() {
                return true;
              }
            };
          }
        };
    EmployeeDao dao = new EmployeeDaoImpl(newConfig);
    List<Employee> list = dao.selectByNames(List.of("SMITH", "KING", "WARD"));
    assertEquals(3, list.size());
  }
}
