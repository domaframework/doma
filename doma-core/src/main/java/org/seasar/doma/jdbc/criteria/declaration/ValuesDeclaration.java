package org.seasar.doma.jdbc.criteria.declaration;

import java.util.Objects;
import org.seasar.doma.jdbc.criteria.context.InsertContext;
import org.seasar.doma.jdbc.criteria.context.Operand;
import org.seasar.doma.jdbc.criteria.def.PropertyDef;

public class ValuesDeclaration {

  private final InsertContext context;

  public ValuesDeclaration(InsertContext context) {
    this.context = Objects.requireNonNull(context);
  }

  public <PROPERTY> void value(PropertyDef<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    context.values.put(new Operand.Prop(left), new Operand.Param(left, right));
  }
}
