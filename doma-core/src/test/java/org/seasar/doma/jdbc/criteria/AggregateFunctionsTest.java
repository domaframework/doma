package org.seasar.doma.jdbc.criteria;

import static org.junit.jupiter.api.Assertions.*;
import static org.seasar.doma.jdbc.criteria.AggregateFunctions.avg;
import static org.seasar.doma.jdbc.criteria.AggregateFunctions.count;
import static org.seasar.doma.jdbc.criteria.AggregateFunctions.max;
import static org.seasar.doma.jdbc.criteria.AggregateFunctions.min;
import static org.seasar.doma.jdbc.criteria.AggregateFunctions.sum;

import org.junit.jupiter.api.Test;
import org.seasar.doma.jdbc.criteria.entity.Emp_;

class AggregateFunctionsTest {

  private final Emp_ e = new Emp_();

  @Test
  void testAvg() {
    assertEquals(avg(e.id), avg(e.id));
    assertNotEquals(avg(e.id), avg(e.name));
    assertNotEquals(avg(e.id), max(e.id));

    assertEquals("avg", avg(e.id).getName());
    assertEquals(e.id, count(e.id).argument());
  }

  @Test
  void testCount() {
    assertEquals(count(e.id), count(e.id));
    assertNotEquals(count(e.id), count(e.name));
    assertNotEquals(count(e.id), max(e.id));

    assertEquals("count", count(e.id).getName());
    assertEquals(e.id, count(e.id).argument());
  }

  @Test
  void testCount_noArg() {
    assertEquals(count(), count());
    assertNotEquals(count(), max(e.id));

    assertEquals("count", count().getName());
    assertEquals("*", count().argument().getName());
  }

  @Test
  void testMax() {
    assertEquals(max(e.id), max(e.id));
    assertNotEquals(max(e.id), max(e.name));
    assertNotEquals(max(e.id), min(e.id));

    assertEquals("max", max(e.id).getName());
    assertEquals(e.id, max(e.id).argument());
  }

  @Test
  void testMin() {
    assertEquals(min(e.id), min(e.id));
    assertNotEquals(min(e.id), min(e.name));
    assertNotEquals(min(e.id), avg(e.id));

    assertEquals("min", min(e.id).getName());
    assertEquals(e.id, min(e.id).argument());
  }

  @Test
  void testSum() {
    assertEquals(sum(e.id), sum(e.id));
    assertNotEquals(sum(e.id), sum(e.name));
    assertNotEquals(sum(e.id), min(e.id));

    assertEquals("sum", sum(e.id).getName());
    assertEquals(e.id, sum(e.id).argument());
  }
}
