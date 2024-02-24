package org.seasar.doma.jdbc.criteria.expression;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.criteria.context.Operand;
import org.seasar.doma.jdbc.criteria.context.SubSelectContext;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

public class Expressions {

  public static PropertyMetamodel<BigDecimal> literal(BigDecimal value) {
    Objects.requireNonNull(value);
    return new LiteralExpression<>(value, BigDecimalPropertyType::new);
  }

  public static PropertyMetamodel<BigInteger> literal(BigInteger value) {
    Objects.requireNonNull(value);
    return new LiteralExpression<>(value, BigIntegerPropertyType::new);
  }

  public static PropertyMetamodel<Boolean> literal(boolean value) {
    return new LiteralExpression<>(value, BooleanPropertyType::new);
  }

  public static PropertyMetamodel<Byte> literal(byte value) {
    return new LiteralExpression<>(value, BytePropertyType::new);
  }

  public static PropertyMetamodel<Double> literal(double value) {
    return new LiteralExpression<>(value, DoublePropertyType::new);
  }

  public static PropertyMetamodel<Float> literal(float value) {
    return new LiteralExpression<>(value, FloatPropertyType::new);
  }

  public static PropertyMetamodel<Integer> literal(int value) {
    return new LiteralExpression<>(value, IntegerPropertyType::new);
  }

  public static PropertyMetamodel<LocalDate> literal(LocalDate value) {
    Objects.requireNonNull(value);
    return new LiteralExpression<>(value, LocalDatePropertyType::new);
  }

  public static PropertyMetamodel<LocalDateTime> literal(LocalDateTime value) {
    Objects.requireNonNull(value);
    return new LiteralExpression<>(value, LocalDateTimePropertyType::new);
  }

  public static PropertyMetamodel<LocalTime> literal(LocalTime value) {
    Objects.requireNonNull(value);
    return new LiteralExpression<>(value, LocalTimePropertyType::new);
  }

  public static PropertyMetamodel<Long> literal(long value) {
    return new LiteralExpression<>(value, LongPropertyType::new);
  }

  public static PropertyMetamodel<Short> literal(short value) {
    return new LiteralExpression<>(value, ShortPropertyType::new);
  }

  public static PropertyMetamodel<String> literal(String value) {
    Objects.requireNonNull(value);
    if (value.indexOf('\'') > -1) {
      throw new DomaIllegalArgumentException(
          "value", "The value must not contain the single quotation.");
    }
    return new LiteralExpression<>(value, StringPropertyType::new);
  }

  public static <PROPERTY> ArithmeticExpression.Add<PROPERTY> add(
      PropertyMetamodel<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    return new ArithmeticExpression.Add<>(
        left, new Operand.Prop(left), new Operand.Param(left, right));
  }

  public static <PROPERTY> ArithmeticExpression.Add<PROPERTY> add(
      PROPERTY left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(right);
    return new ArithmeticExpression.Add<>(
        right, new Operand.Param(right, left), new Operand.Prop(right));
  }

  public static <PROPERTY> ArithmeticExpression.Add<PROPERTY> add(
      PropertyMetamodel<PROPERTY> left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    return new ArithmeticExpression.Add<>(left, new Operand.Prop(left), new Operand.Prop(right));
  }

  public static <PROPERTY> ArithmeticExpression.Sub<PROPERTY> sub(
      PropertyMetamodel<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    return new ArithmeticExpression.Sub<>(
        left, new Operand.Prop(left), new Operand.Param(left, right));
  }

  public static <PROPERTY> ArithmeticExpression.Sub<PROPERTY> sub(
      PROPERTY left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(right);
    return new ArithmeticExpression.Sub<>(
        right, new Operand.Param(right, left), new Operand.Prop(right));
  }

  public static <PROPERTY> ArithmeticExpression.Sub<PROPERTY> sub(
      PropertyMetamodel<PROPERTY> left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    return new ArithmeticExpression.Sub<>(left, new Operand.Prop(left), new Operand.Prop(right));
  }

  public static <PROPERTY> ArithmeticExpression.Mul<PROPERTY> mul(
      PropertyMetamodel<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    return new ArithmeticExpression.Mul<>(
        left, new Operand.Prop(left), new Operand.Param(left, right));
  }

  public static <PROPERTY> ArithmeticExpression.Mul<PROPERTY> mul(
      PROPERTY left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(right);
    return new ArithmeticExpression.Mul<>(
        right, new Operand.Param(right, left), new Operand.Prop(right));
  }

  public static <PROPERTY> ArithmeticExpression.Mul<PROPERTY> mul(
      PropertyMetamodel<PROPERTY> left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    return new ArithmeticExpression.Mul<>(left, new Operand.Prop(left), new Operand.Prop(right));
  }

  public static <PROPERTY> ArithmeticExpression.Div<PROPERTY> div(
      PropertyMetamodel<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    return new ArithmeticExpression.Div<>(
        left, new Operand.Prop(left), new Operand.Param(left, right));
  }

  public static <PROPERTY> ArithmeticExpression.Div<PROPERTY> div(
      PROPERTY left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(right);
    return new ArithmeticExpression.Div<>(
        right, new Operand.Param(right, left), new Operand.Prop(right));
  }

  public static <PROPERTY> ArithmeticExpression.Div<PROPERTY> div(
      PropertyMetamodel<PROPERTY> left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    return new ArithmeticExpression.Div<>(left, new Operand.Prop(left), new Operand.Prop(right));
  }

  public static <PROPERTY> ArithmeticExpression.Mod<PROPERTY> mod(
      PropertyMetamodel<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    return new ArithmeticExpression.Mod<>(
        left, new Operand.Prop(left), new Operand.Param(left, right));
  }

  public static <PROPERTY> ArithmeticExpression.Mod<PROPERTY> mod(
      PROPERTY left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(right);
    return new ArithmeticExpression.Mod<>(
        right, new Operand.Param(right, left), new Operand.Prop(right));
  }

  public static <PROPERTY> ArithmeticExpression.Mod<PROPERTY> mod(
      PropertyMetamodel<PROPERTY> left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    return new ArithmeticExpression.Mod<>(left, new Operand.Prop(left), new Operand.Prop(right));
  }

  public static <PROPERTY> StringExpression.Concat<PROPERTY> concat(
      PropertyMetamodel<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    return new StringExpression.Concat<>(
        left, new Operand.Prop(left), new Operand.Param(left, right));
  }

  public static <PROPERTY> StringExpression.Concat<PROPERTY> concat(
      PROPERTY left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(right);
    return new StringExpression.Concat<>(
        right, new Operand.Param(right, left), new Operand.Prop(right));
  }

  public static <PROPERTY> StringExpression.Concat<PROPERTY> concat(
      PropertyMetamodel<PROPERTY> left, PropertyMetamodel<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    return new StringExpression.Concat<>(left, new Operand.Prop(left), new Operand.Prop(right));
  }

  public static <PROPERTY> StringExpression.Lower<PROPERTY> lower(
      PropertyMetamodel<PROPERTY> propertyMetamodel) {
    Objects.requireNonNull(propertyMetamodel);
    return new StringExpression.Lower<>(propertyMetamodel);
  }

  public static <PROPERTY> StringExpression.Ltrim<PROPERTY> ltrim(
      PropertyMetamodel<PROPERTY> propertyMetamodel) {
    Objects.requireNonNull(propertyMetamodel);
    return new StringExpression.Ltrim<>(propertyMetamodel);
  }

  public static <PROPERTY> StringExpression.Rtrim<PROPERTY> rtrim(
      PropertyMetamodel<PROPERTY> propertyMetamodel) {
    Objects.requireNonNull(propertyMetamodel);
    return new StringExpression.Rtrim<>(propertyMetamodel);
  }

  public static <PROPERTY> StringExpression.Trim<PROPERTY> trim(
      PropertyMetamodel<PROPERTY> propertyMetamodel) {
    Objects.requireNonNull(propertyMetamodel);
    return new StringExpression.Trim<>(propertyMetamodel);
  }

  public static <PROPERTY> StringExpression.Upper<PROPERTY> upper(
      PropertyMetamodel<PROPERTY> propertyMetamodel) {
    Objects.requireNonNull(propertyMetamodel);
    return new StringExpression.Upper<>(propertyMetamodel);
  }

  public static <PROPERTY> AggregateFunction.Avg<PROPERTY> avg(
      PropertyMetamodel<PROPERTY> propertyMetamodel) {
    Objects.requireNonNull(propertyMetamodel);
    return new AggregateFunction.Avg<>(propertyMetamodel);
  }

  public static AggregateFunction.AvgAsDouble avgAsDouble(PropertyMetamodel<?> propertyMetamodel) {
    Objects.requireNonNull(propertyMetamodel);
    return new AggregateFunction.AvgAsDouble(propertyMetamodel);
  }

  public static AggregateFunction.Count count() {
    return new AggregateFunction.Count(AggregateFunction.Asterisk);
  }

  public static AggregateFunction.Count count(PropertyMetamodel<?> propertyMetamodel) {
    Objects.requireNonNull(propertyMetamodel);
    return new AggregateFunction.Count(propertyMetamodel);
  }

  public static AggregateFunction.Count countDistinct(PropertyMetamodel<?> propertyMetamodel) {
    Objects.requireNonNull(propertyMetamodel);
    return new AggregateFunction.Count(propertyMetamodel, true);
  }

  public static <PROPERTY> AggregateFunction.Max<PROPERTY> max(
      PropertyMetamodel<PROPERTY> propertyMetamodel) {
    Objects.requireNonNull(propertyMetamodel);
    return new AggregateFunction.Max<>(propertyMetamodel);
  }

  public static <PROPERTY> AggregateFunction.Min<PROPERTY> min(
      PropertyMetamodel<PROPERTY> propertyMetamodel) {
    Objects.requireNonNull(propertyMetamodel);
    return new AggregateFunction.Min<>(propertyMetamodel);
  }

  public static <PROPERTY> AggregateFunction.Sum<PROPERTY> sum(
      PropertyMetamodel<PROPERTY> propertyMetamodel) {
    Objects.requireNonNull(propertyMetamodel);
    return new AggregateFunction.Sum<>(propertyMetamodel);
  }

  public static <PROPERTY> CaseExpression<PROPERTY> when(
      Consumer<CaseExpression<PROPERTY>.Declaration> block, PropertyMetamodel<PROPERTY> otherwise) {
    Objects.requireNonNull(block);
    Objects.requireNonNull(otherwise);
    CaseExpression<PROPERTY> caseExpression = new CaseExpression<>(otherwise);
    block.accept(caseExpression.new Declaration());
    return caseExpression;
  }

  public static <PROPERTY> SelectExpression<PROPERTY> select(
      Function<SelectExpression.Declaration, SubSelectContext.Single<PROPERTY>> block) {
    Objects.requireNonNull(block);
    SubSelectContext.Single<PROPERTY> subSelectContext =
        block.apply(new SelectExpression.Declaration());
    return new SelectExpression<>(subSelectContext);
  }

  public static <PROPERTY> AliasExpression<PROPERTY> alias(
      PropertyMetamodel<PROPERTY> propertyMetamodel, String alias) {
    Objects.requireNonNull(propertyMetamodel);
    Objects.requireNonNull(alias);
    return new AliasExpression<>(propertyMetamodel, alias);
  }
}
