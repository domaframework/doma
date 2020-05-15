package org.seasar.doma.jdbc.criteria.declaration;

import java.util.Objects;
import org.seasar.doma.jdbc.criteria.context.Operand;
import org.seasar.doma.jdbc.criteria.context.UpdateContext;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

public class SetDeclaration {

  private final UpdateContext context;

  public SetDeclaration(UpdateContext context) {
    this.context = Objects.requireNonNull(context);
  }

  public <PROPERTY> void value(PropertyMetamodel<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    context.set.put(new Operand.Prop(left), new Operand.Param(left, right));
  }
}
