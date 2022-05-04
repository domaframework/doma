package org.seasar.doma.it.jep355;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.jdbc.Config;

@ExtendWith(IntegrationTestEnvironment.class)
public class TextBlockTest {

  @Test
  void selectAll(Config config) {
    var dao = new TextBlockDaoImpl(config);
    var list = dao.selectAll();
    assertEquals(14, list.size());
  }

  @Test
  void selectById(Config config) {
    var dao = new TextBlockDaoImpl(config);
    var employee = dao.selectById(1);
    assertNotNull(employee);
  }
}
