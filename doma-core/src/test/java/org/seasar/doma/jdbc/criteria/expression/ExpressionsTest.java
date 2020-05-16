package org.seasar.doma.jdbc.criteria.expression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.add;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.avg;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.concat;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.count;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.div;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.max;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.min;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.mod;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.mul;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.sub;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.sum;

import org.junit.jupiter.api.Test;
import org.seasar.doma.jdbc.criteria.entity.Emp_;

class ExpressionsTest {

  private final Emp_ e = new Emp_();

  @Test
  void testAdd() {
    assertEquals(add(e.id, 1), add(e.id, 1));
    assertNotEquals(add(e.id, 1), add(e.id, 2));
  }

  @Test
  void testSub() {
    assertEquals(sub(e.id, 1), sub(e.id, 1));
    assertNotEquals(sub(e.id, 1), sub(e.id, 2));
  }

  @Test
  void testMul() {
    assertEquals(mul(e.id, 1), mul(e.id, 1));
    assertNotEquals(mul(e.id, 1), mul(e.id, 2));
  }

  @Test
  void testDiv() {
    assertEquals(div(e.id, 1), div(e.id, 1));
    assertNotEquals(div(e.id, 1), div(e.id, 2));
  }

  @Test
  void testMod() {
    assertEquals(mod(e.id, 1), mod(e.id, 1));
  }

  @Test
  void testConcat() {
    assertEquals(concat(e.name, "a"), concat(e.name, "a"));
    assertNotEquals(concat(e.name, "a"), concat(e.name, "b"));
  }

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
