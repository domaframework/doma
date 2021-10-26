package org.seasar.doma.it.embeddable;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.dao.StaffDao;
import org.seasar.doma.it.dao.StaffDaoImpl;
import org.seasar.doma.it.entity.Staff;
import org.seasar.doma.jdbc.Config;

@ExtendWith(IntegrationTestEnvironment.class)
public class StaffInfoTest {

  @Test
  public void testPrimitiveType(Config config) throws Exception {
    StaffDao dao = new StaffDaoImpl(config);
    Staff staff = dao.selectById(9);
    assertEquals(0, staff.staffInfo.managerId);
  }
}
