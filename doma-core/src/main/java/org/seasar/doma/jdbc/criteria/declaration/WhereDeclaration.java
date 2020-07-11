package org.seasar.doma.jdbc.criteria.declaration;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.criteria.context.Criterion;
import org.seasar.doma.jdbc.criteria.context.DeleteContext;
import org.seasar.doma.jdbc.criteria.context.Operand;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.context.SubSelectContext;
import org.seasar.doma.jdbc.criteria.context.UpdateContext;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.criteria.option.LikeOption;
import org.seasar.doma.jdbc.criteria.tuple.Tuple2;

public class WhereDeclaration extends ComparisonDeclaration {

  public WhereDeclaration(SelectContext context) {
    super(() -> context.where, w -> context.where = w);
    Objects.requireNonNull(context);
  }

  public WhereDeclaration(DeleteContext context) {
    super(() -> context.where, w -> context.where = w);
    Objects.requireNonNull(context);
  }

  public WhereDeclaration(UpdateContext context) {
    super(() -> context.where, w -> context.where = w);
    Objects.requireNonNull(context);
  }

  public <PROPERTY> void isNull(PropertyMetamodel<PROPERTY> propertyMetamodel) {
    Objects.requireNonNull(propertyMetamodel);
    add(new Criterion.IsNull(new Operand.Prop(propertyMetamodel)));
  }

  public <PROPERTY> void isNotNull(PropertyMetamodel<PROPERTY> propertyMetamodel) {
    Objects.requireNonNull(propertyMetamodel);
    add(new Criterion.IsNotNull(new Operand.Prop(propertyMetamodel)));
  }

  public void like(PropertyMetamodel<?> left, CharSequence right) {
    Objects.requireNonNull(left);
    add(new Criterion.Like(new Operand.Prop(left), right, LikeOption.none()));
  }

  public void like(PropertyMetamodel<?> left, CharSequence right, LikeOption option) {
    Objects.requireNonNull(left);
    add(new Criterion.Like(new Operand.Prop(left), right, option));
  }

  public void notLike(PropertyMetamodel<?> left, CharSequence right) {
    Objects.requireNonNull(left);
    add(new Criterion.NotLike(new Operand.Prop(left), right, LikeOption.none()));
  }

  public void notLike(PropertyMetamodel<?> left, CharSequence right, LikeOption option) {
    Objects.requireNonNull(left);
    add(new Criterion.NotLike(new Operand.Prop(left), right, option));
  }

  public <PROPERTY> void between(
      PropertyMetamodel<PROPERTY> propertyMetamodel, PROPERTY start, PROPERTY end) {
    Objects.requireNonNull(propertyMetamodel);
    add(
        new Criterion.Between(
            new Operand.Prop(propertyMetamodel),
            new Operand.Param(propertyMetamodel, start),
            new Operand.Param(propertyMetamodel, end)));
  }

  public <PROPERTY> void in(PropertyMetamodel<PROPERTY> left, List<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(
        new Criterion.In(
            new Operand.Prop(left),
            right.stream().map(p -> new Operand.Param(left, p)).collect(toList())));
  }

  public <PROPERTY> void notIn(PropertyMetamodel<PROPERTY> left, List<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(
        new Criterion.NotIn(
            new Operand.Prop(left),
            right.stream().map(p -> new Operand.Param(left, p)).collect(toList())));
  }

  public <PROPERTY> void in(
      PropertyMetamodel<PROPERTY> left, SubSelectContext<PropertyMetamodel<PROPERTY>> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.InSubQuery(new Operand.Prop(left), right.get()));
  }

  public <PROPERTY> void notIn(
      PropertyMetamodel<PROPERTY> left, SubSelectContext<PropertyMetamodel<PROPERTY>> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.NotInSubQuery(new Operand.Prop(left), right.get()));
  }

  public <PROPERTY1, PROPERTY2> void in(
      Tuple2<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>> left,
      List<Tuple2<PROPERTY1, PROPERTY2>> right) {
    Operand.Prop prop1 = new Operand.Prop(left.getItem1());
    Operand.Prop prop2 = new Operand.Prop(left.getItem2());
    List<Tuple2<Operand.Param, Operand.Param>> params =
        right.stream()
            .map(
                pair -> {
                  Operand.Param param1 = new Operand.Param(left.getItem1(), pair.getItem1());
                  Operand.Param param2 = new Operand.Param(left.getItem2(), pair.getItem2());
                  return new Tuple2<>(param1, param2);
                })
            .collect(toList());
    add(new Criterion.InTuple2(new Tuple2<>(prop1, prop2), params));
  }

  public <PROPERTY1, PROPERTY2> void notIn(
      Tuple2<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>> left,
      List<Tuple2<PROPERTY1, PROPERTY2>> right) {
    Operand.Prop prop1 = new Operand.Prop(left.getItem1());
    Operand.Prop prop2 = new Operand.Prop(left.getItem2());
    List<Tuple2<Operand.Param, Operand.Param>> params =
        right.stream()
            .map(
                pair -> {
                  Operand.Param param1 = new Operand.Param(left.getItem1(), pair.getItem1());
                  Operand.Param param2 = new Operand.Param(left.getItem2(), pair.getItem2());
                  return new Tuple2<>(param1, param2);
                })
            .collect(toList());
    add(new Criterion.NotInTuple2(new Tuple2<>(prop1, prop2), params));
  }

  public <PROPERTY1, PROPERTY2> void in(
      Tuple2<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>> left,
      SubSelectContext<Tuple2<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>>> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    Operand.Prop prop1 = new Operand.Prop(left.getItem1());
    Operand.Prop prop2 = new Operand.Prop(left.getItem2());
    add(new Criterion.InTuple2SubQuery(new Tuple2<>(prop1, prop2), right.get()));
  }

  public <PROPERTY1, PROPERTY2> void notIn(
      Tuple2<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>> left,
      SubSelectContext<Tuple2<PropertyMetamodel<PROPERTY1>, PropertyMetamodel<PROPERTY2>>> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    Operand.Prop prop1 = new Operand.Prop(left.getItem1());
    Operand.Prop prop2 = new Operand.Prop(left.getItem2());
    add(new Criterion.NotInTuple2SubQuery(new Tuple2<>(prop1, prop2), right.get()));
  }

  public void exists(SubSelectContext<?> subSelectContext) {
    Objects.requireNonNull(subSelectContext);
    add(new Criterion.Exists(subSelectContext.get()));
  }

  public void notExists(SubSelectContext<?> subSelectContext) {
    Objects.requireNonNull(subSelectContext);
    add(new Criterion.NotExists(subSelectContext.get()));
  }

  public <ENTITY> SubSelectFromDeclaration<ENTITY> from(EntityMetamodel<ENTITY> entityMetamodel) {
    return new SubSelectFromDeclaration<>(entityMetamodel);
  }
}
