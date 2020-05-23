package org.seasar.doma.jdbc.criteria.expression;

import java.util.Objects;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.criteria.context.Operand;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

public class Expressions {

  public static PropertyMetamodel<String> literal(String value) {
    Objects.requireNonNull(value);
    return new LiteralExpression.StringLiteral(value);
  }

  public static PropertyMetamodel<Integer> literal(int value) {
    Objects.requireNonNull(value);
    return new LiteralExpression.IntLiteral(value);
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
}
