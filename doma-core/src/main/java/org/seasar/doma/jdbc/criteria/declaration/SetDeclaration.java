package org.seasar.doma.jdbc.criteria.declaration;

import java.util.Objects;
import org.seasar.doma.def.PropertyDef;
import org.seasar.doma.jdbc.criteria.context.UpdateContext;

public class SetDeclaration {

  private final UpdateContext context;
  private final DeclarationSupport support;

  public SetDeclaration(UpdateContext context) {
    Objects.requireNonNull(context);
    this.context = context;
    this.support = new DeclarationSupport();
  }

  public <PROPERTY> void value(PropertyDef<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    context.set.put(support.toProp(left), support.toParam(left, right));
  }
}
