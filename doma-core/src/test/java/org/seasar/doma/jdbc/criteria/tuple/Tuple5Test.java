package org.seasar.doma.jdbc.criteria.tuple;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class Tuple5Test {

  @Test
  void test() {
    Tuple5<Integer, String, Boolean, Long, BigDecimal> tuple =
        new Tuple5<>(1, "a", true, 1L, BigDecimal.ONE);
    assertEquals(new Tuple5<>(1, "a", true, 1L, BigDecimal.ONE), tuple);
    assertEquals(1, tuple.getItem1());
    assertEquals("a", tuple.getItem2());
    assertEquals(true, tuple.getItem3());
    assertEquals(1L, tuple.getItem4());
    assertEquals(BigDecimal.ONE, tuple.getItem5());
    assertEquals(tuple.getItem1(), tuple.component1());
    assertEquals(tuple.getItem2(), tuple.component2());
    assertEquals(tuple.getItem3(), tuple.component3());
    assertEquals(tuple.getItem4(), tuple.component4());
    assertEquals(tuple.getItem5(), tuple.component5());
  }
}
