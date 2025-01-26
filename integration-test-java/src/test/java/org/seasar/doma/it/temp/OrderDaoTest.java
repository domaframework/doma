package org.seasar.doma.it.temp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.jdbc.Config;

@ExtendWith(IntegrationTestEnvironment.class)
public class OrderDaoTest {

  @Test
  public void test(Config config) {
    OrderDao dao = new OrderDaoImpl(config);

    Order o1 = dao.findById(1);
    System.out.println(o1.getId());

    Order o2 = dao.findById(2);
    System.out.println(o2.getId());
  }
}
