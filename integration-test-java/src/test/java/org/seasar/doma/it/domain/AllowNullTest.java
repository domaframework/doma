package org.seasar.doma.it.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.dao.FulltimeEmployeeDao;
import org.seasar.doma.it.dao.FulltimeEmployeeDaoImpl;
import org.seasar.doma.it.entity.FulltimeEmployee;
import org.seasar.doma.jdbc.Config;

@ExtendWith(IntegrationTestEnvironment.class)
public class AllowNullTest {

  @Test
  void selectDomain(Config config) throws Exception {
    FulltimeEmployeeDao dao = new FulltimeEmployeeDaoImpl(config);
    FulltimeEmployee e = new FulltimeEmployee();
    e.setEmployeeId(99);
    e.setEmployeeNo(9999);
    dao.insert(e);

    Money salary = dao.selectSalaryById(99);
    assertNotNull(salary);
    assertNull(salary.getValue());
  }

  @Test
  void selectOptinalDomain(Config config) throws Exception {
    FulltimeEmployeeDao dao = new FulltimeEmployeeDaoImpl(config);
    FulltimeEmployee e = new FulltimeEmployee();
    e.setEmployeeId(99);
    e.setEmployeeNo(9999);
    dao.insert(e);

    Optional<Money> salary = dao.selectOptionalSalaryById(99);
    assertNotNull(salary);
    assertFalse(salary.isPresent());
  }
}
