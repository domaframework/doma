package org.seasar.doma.jdbc.criteria.declaration;

import java.util.Objects;
import org.seasar.doma.jdbc.criteria.context.Operand;
import org.seasar.doma.jdbc.criteria.context.UpdateContext;
import org.seasar.doma.jdbc.criteria.def.PropertyDef;

public class SetDeclaration {

  private final UpdateContext context;

  public SetDeclaration(UpdateContext context) {
    Objects.requireNonNull(context);
    this.context = context;
  }

  public <PROPERTY> void value(PropertyDef<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    context.set.put(new Operand.Prop(left), new Operand.Param(left, right));
  }
}
