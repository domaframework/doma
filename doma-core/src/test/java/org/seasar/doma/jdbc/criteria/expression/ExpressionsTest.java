package org.seasar.doma.jdbc.criteria.expression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.add;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.avg;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.avgAsDouble;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.concat;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.count;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.div;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.literal;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.max;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.min;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.mod;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.mul;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.sub;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.sum;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.criteria.entity.Emp_;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

class ExpressionsTest {

  private final Emp_ e = new Emp_();

  @Test
  void testBigDecimalLiteral() {
    assertEquals(literal(new BigDecimal(100)), literal(new BigDecimal(100)));
    assertNotEquals(literal(new BigDecimal(100)), literal(new BigDecimal(200)));
  }

  @Test
  void testBigIntegerLiteral() {
    assertEquals(literal(new BigInteger("100")), literal(new BigInteger("100")));
    assertNotEquals(literal(new BigInteger("100")), literal(new BigInteger("200")));
  }

  @Test
  void testBooleanLiteral() {
    assertEquals(literal(true), literal(true));
    assertNotEquals(literal(true), literal(false));
  }

  @Test
  void testByteLiteral() {
    assertEquals(literal((byte) 1), literal((byte) 1));
    assertNotEquals(literal((byte) 1), literal((byte) 2));
  }

  @Test
  void testDoubleLiteral() {
    assertEquals(literal(1D), literal(1D));
    assertNotEquals(literal(1D), literal(2D));
  }

  @Test
  void testFloatLiteral() {
    assertEquals(literal(1F), literal(1F));
    assertNotEquals(literal(1F), literal(2F));
  }

  @Test
  void testIntegerLiteral() {
    assertEquals(literal(1), literal(1));
    assertNotEquals(literal(1), literal(2));
  }

  @Test
  void testLocalDateLiteral() {
    assertEquals(literal(LocalDate.of(2020, 5, 23)), literal(LocalDate.of(2020, 5, 23)));
    assertNotEquals(literal(LocalDate.of(2020, 5, 23)), literal(LocalDate.of(2021, 5, 23)));
  }

  @Test
  void testLocalDateTimeLiteral() {
    assertEquals(
        literal(LocalDateTime.of(2020, 5, 23, 10, 11, 12)),
        literal(LocalDateTime.of(2020, 5, 23, 10, 11, 12)));
    assertNotEquals(
        literal(LocalDateTime.of(2020, 5, 23, 10, 11, 12)),
        literal(LocalDateTime.of(2021, 5, 23, 10, 11, 12)));
  }

  @Test
  void testLocalTimeLiteral() {
    assertEquals(literal(LocalTime.of(10, 11, 12)), literal(LocalTime.of(10, 11, 12)));
    assertNotEquals(literal(LocalTime.of(10, 11, 12)), literal(LocalTime.of(5, 11, 12)));
  }

  @Test
  void testLongLiteral() {
    assertEquals(literal(1L), literal(1L));
    assertNotEquals(literal(1L), literal(2L));
  }

  @Test
  void testStringLiteral() {
    assertEquals(literal("abc"), literal("abc"));
    assertNotEquals(literal("abc"), literal("def"));
  }

  @Test
  void testStringLiteral_contains_single_quotation() {
    DomaIllegalArgumentException ex =
        assertThrows(DomaIllegalArgumentException.class, () -> literal("ab'c"));
    System.out.println(ex.getMessage());
  }

  @Test
  void testShortLiteral() {
    assertEquals(literal((short) 1), literal((short) 1));
    assertNotEquals(literal((short) 1), literal((short) 2));
  }

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

  @SuppressWarnings("AssertBetweenInconvertibleTypes")
  @Test
  void testAvg() {
    assertEquals(avg(e.id), avg(e.id));
    assertNotEquals(avg(e.id), avg(e.name));
    assertNotEquals(avg(e.id), max(e.id));

    assertEquals("avg", avg(e.id).getName());
    assertEquals(e.id, avg(e.id).argument());
  }

  @SuppressWarnings("AssertBetweenInconvertibleTypes")
  @Test
  void testAvgAsDouble() {
    assertEquals(avgAsDouble(e.id), avgAsDouble(e.id));
    assertNotEquals(avgAsDouble(e.id), avgAsDouble(e.name));
    assertNotEquals(avgAsDouble(e.id), max(e.id));

    assertEquals("avg", avgAsDouble(e.id).getName());
    assertEquals(e.id, avgAsDouble(e.id).argument());
  }

  @SuppressWarnings("AssertBetweenInconvertibleTypes")
  @Test
  void testCount() {
    assertEquals(count(e.id), count(e.id));
    assertNotEquals(count(e.id), count(e.name));
    assertNotEquals(count(e.id), max(e.id));

    assertEquals("count", count(e.id).getName());
    assertEquals(e.id, count(e.id).argument());
  }

  @SuppressWarnings("AssertBetweenInconvertibleTypes")
  @Test
  void testCount_noArg() {
    assertEquals(count(), count());
    assertNotEquals(count(), max(e.id));

    assertEquals("count", count().getName());
    assertEquals("*", count().argument().getName());
  }

  @SuppressWarnings("AssertBetweenInconvertibleTypes")
  @Test
  void testMax() {
    assertEquals(max(e.id), max(e.id));
    assertNotEquals(max(e.id), max(e.name));
    assertNotEquals(max(e.id), min(e.id));

    assertEquals("max", max(e.id).getName());
    assertEquals(e.id, max(e.id).argument());
  }

  @SuppressWarnings("AssertBetweenInconvertibleTypes")
  @Test
  void testMin() {
    assertEquals(min(e.id), min(e.id));
    assertNotEquals(min(e.id), min(e.name));
    assertNotEquals(min(e.id), avg(e.id));

    assertEquals("min", min(e.id).getName());
    assertEquals(e.id, min(e.id).argument());
  }

  @SuppressWarnings("AssertBetweenInconvertibleTypes")
  @Test
  void testSum() {
    assertEquals(sum(e.id), sum(e.id));
    assertNotEquals(sum(e.id), sum(e.name));
    assertNotEquals(sum(e.id), min(e.id));

    assertEquals("sum", sum(e.id).getName());
    assertEquals(e.id, sum(e.id).argument());
  }

  @Test
  void testEquality() {
    PropertyMetamodel<?> sum = sum(e.id);
    PropertyMetamodel<?> div = div(sum(e.id), 1);
    PropertyMetamodel<?> mod = mod(sum(e.id), 1);
    PropertyMetamodel<?> mul = mul(sum(e.id), 1);
    PropertyMetamodel<?> sub = sub(sum(e.id), 1);
    PropertyMetamodel<?> add = add(sum(e.id), 1);

    assertEquals(sum, sum(e.id));
    assertEquals(div, div(sum(e.id), 1));
    assertEquals(mod, mod(sum(e.id), 1));
    assertEquals(mul, mul(sum(e.id), 1));
    assertEquals(sub, sub(sum(e.id), 1));
    assertEquals(add, add(sum(e.id), 1));

    assertNotEquals(sum, div);
    assertNotEquals(sum, mod);
    assertNotEquals(sum, mul);
    assertNotEquals(sum, sub);
    assertNotEquals(sum, add);

    assertNotEquals(div, mod);
    assertNotEquals(div, mul);
    assertNotEquals(div, sub);
    assertNotEquals(div, add);

    assertNotEquals(mod, mul);
    assertNotEquals(mod, sub);
    assertNotEquals(mod, add);

    assertNotEquals(mul, sub);
    assertNotEquals(mul, add);

    assertNotEquals(sub, add);
  }
}
