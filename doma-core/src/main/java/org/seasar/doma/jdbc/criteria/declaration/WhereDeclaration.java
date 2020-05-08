package org.seasar.doma.jdbc.criteria.declaration;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.seasar.doma.jdbc.criteria.LikeOption;
import org.seasar.doma.jdbc.criteria.context.Context;
import org.seasar.doma.jdbc.criteria.context.Criterion;
import org.seasar.doma.jdbc.criteria.context.Operand;
import org.seasar.doma.jdbc.criteria.context.SubSelectContext;
import org.seasar.doma.jdbc.criteria.def.EntityDef;
import org.seasar.doma.jdbc.criteria.def.PropertyDef;
import org.seasar.doma.jdbc.criteria.tuple.Tuple2;

public class WhereDeclaration extends ComparisonDeclaration<Context> {

  public WhereDeclaration(Context context) {
    super(context);
  }

  public <PROPERTY> void isNull(PropertyDef<PROPERTY> propertyDef) {
    Objects.requireNonNull(propertyDef);
    add(new Criterion.IsNull(new Operand.Prop(propertyDef)));
  }

  public <PROPERTY> void isNotNull(PropertyDef<PROPERTY> propertyDef) {
    Objects.requireNonNull(propertyDef);
    add(new Criterion.IsNotNull(new Operand.Prop(propertyDef)));
  }

  public <PROPERTY> void like(PropertyDef<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    add(
        new Criterion.Like(
            new Operand.Prop(left), new Operand.Param(left, right), LikeOption.NONE));
  }

  public <PROPERTY> void like(PropertyDef<PROPERTY> left, PROPERTY right, LikeOption option) {
    Objects.requireNonNull(left);
    add(new Criterion.Like(new Operand.Prop(left), new Operand.Param(left, right), option));
  }

  public <PROPERTY> void notLike(PropertyDef<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    add(
        new Criterion.NotLike(
            new Operand.Prop(left), new Operand.Param(left, right), LikeOption.NONE));
  }

  public <PROPERTY> void notLike(PropertyDef<PROPERTY> left, PROPERTY right, LikeOption option) {
    Objects.requireNonNull(left);
    add(new Criterion.NotLike(new Operand.Prop(left), new Operand.Param(left, right), option));
  }

  public <PROPERTY> void between(PropertyDef<PROPERTY> propertyDef, PROPERTY start, PROPERTY end) {
    Objects.requireNonNull(propertyDef);
    add(
        new Criterion.Between(
            new Operand.Prop(propertyDef),
            new Operand.Param(propertyDef, start),
            new Operand.Param(propertyDef, end)));
  }

  public <PROPERTY> void in(PropertyDef<PROPERTY> left, List<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(
        new Criterion.In(
            new Operand.Prop(left),
            right.stream().map(p -> new Operand.Param(left, p)).collect(toList())));
  }

  public <PROPERTY> void notIn(PropertyDef<PROPERTY> left, List<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(
        new Criterion.NotIn(
            new Operand.Prop(left),
            right.stream().map(p -> new Operand.Param(left, p)).collect(toList())));
  }

  public <PROPERTY> void in(PropertyDef<PROPERTY> left, SubSelectContext<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.InSubQuery(new Operand.Prop(left), right.context));
  }

  public <PROPERTY> void notIn(PropertyDef<PROPERTY> left, SubSelectContext<PROPERTY> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    add(new Criterion.NotInSubQuery(new Operand.Prop(left), right.context));
  }

  public <PROPERTY1, PROPERTY2> void in(
      Tuple2<PropertyDef<PROPERTY1>, PropertyDef<PROPERTY2>> left,
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
      Tuple2<PropertyDef<PROPERTY1>, PropertyDef<PROPERTY2>> left,
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
      Tuple2<PropertyDef<PROPERTY1>, PropertyDef<PROPERTY2>> left,
      SubSelectContext<Tuple2<PROPERTY1, PROPERTY2>> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    Operand.Prop prop1 = new Operand.Prop(left.getItem1());
    Operand.Prop prop2 = new Operand.Prop(left.getItem2());
    add(new Criterion.InTuple2SubQuery(new Tuple2<>(prop1, prop2), right.context));
  }

  public <PROPERTY1, PROPERTY2> void notIn(
      Tuple2<PropertyDef<PROPERTY1>, PropertyDef<PROPERTY2>> left,
      SubSelectContext<Tuple2<PROPERTY1, PROPERTY2>> right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    Operand.Prop prop1 = new Operand.Prop(left.getItem1());
    Operand.Prop prop2 = new Operand.Prop(left.getItem2());
    add(new Criterion.NotInTuple2SubQuery(new Tuple2<>(prop1, prop2), right.context));
  }

  public void exists(SubSelectContext<?> subSelectContext) {
    Objects.requireNonNull(subSelectContext);
    add(new Criterion.Exists(subSelectContext.context));
  }

  public void notExists(SubSelectContext<?> subSelectContext) {
    Objects.requireNonNull(subSelectContext);
    add(new Criterion.NotExists(subSelectContext.context));
  }

  public SubSelectFromDeclaration from(EntityDef<?> entityDef) {
    return new SubSelectFromDeclaration(entityDef);
  }

  public void and(Runnable block) {
    runBlock(block, Criterion.And::new);
  }

  public void or(Runnable block) {
    runBlock(block, Criterion.Or::new);
  }

  public void not(Runnable block) {
    runBlock(block, Criterion.Not::new);
  }

  @Override
  protected void runBlock(Runnable block, Function<List<Criterion>, Criterion> newCriterion) {
    List<Criterion> preservedWhere = context.getWhere();
    List<Criterion> newWhere = new ArrayList<>();
    context.setWhere(newWhere);
    block.run();
    context.setWhere(preservedWhere);
    if (!newWhere.isEmpty()) {
      add(newCriterion.apply(newWhere));
    }
  }

  @Override
  protected void add(Criterion criterion) {
    context.getWhere().add(criterion);
  }
}
