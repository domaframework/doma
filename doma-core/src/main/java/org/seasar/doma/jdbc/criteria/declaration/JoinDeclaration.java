package org.seasar.doma.jdbc.criteria.declaration;

import java.util.Objects;
import org.seasar.doma.def.PropertyDef;
import org.seasar.doma.jdbc.criteria.context.Criterion;
import org.seasar.doma.jdbc.criteria.context.Join;

public class JoinDeclaration {

  private final Join join;
  private final DeclarationSupport support;

  public JoinDeclaration(Join join) {
    Objects.requireNonNull(join);
    this.join = join;
    this.support = new DeclarationSupport();
  }

  private void add(Criterion criterion) {
    join.on.add(criterion);
  }

  public <PROPERTY> void eq(PropertyDef<PROPERTY> left, PropertyDef<PROPERTY> right) {
    add(new Criterion.Eq(support.toProp(left), support.toProp(right)));
  }

  public <PROPERTY> void ne(PropertyDef<PROPERTY> left, PropertyDef<PROPERTY> right) {
    add(new Criterion.Ne(support.toProp(left), support.toProp(right)));
  }

  public <PROPERTY> void ge(PropertyDef<PROPERTY> left, PropertyDef<PROPERTY> right) {
    add(new Criterion.Ge(support.toProp(left), support.toProp(right)));
  }

  public <PROPERTY> void gt(PropertyDef<PROPERTY> left, PropertyDef<PROPERTY> right) {
    add(new Criterion.Gt(support.toProp(left), support.toProp(right)));
  }

  public <PROPERTY> void le(PropertyDef<PROPERTY> left, PropertyDef<PROPERTY> right) {
    add(new Criterion.Le(support.toProp(left), support.toProp(right)));
  }

  public <PROPERTY> void lt(PropertyDef<PROPERTY> left, PropertyDef<PROPERTY> right) {
    add(new Criterion.Lt(support.toProp(left), support.toProp(right)));
  }
}
