package org.seasar.doma.internal.jdbc.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.wrapper.IntegerWrapper;

class ScalarResultListParameterTest {

  @Test
  void getResult() {
    ScalarResultListParameter<Integer, Integer> parameter =
        new ScalarResultListParameter<>(() -> new BasicScalar<>(IntegerWrapper::new));
    parameter.add(1);
    parameter.add(2);
    List<Integer> result = parameter.getResult();
    assertEquals(2, result.size());
    assertEquals(1, result.get(0));
    assertEquals(2, result.get(1));
  }
}
