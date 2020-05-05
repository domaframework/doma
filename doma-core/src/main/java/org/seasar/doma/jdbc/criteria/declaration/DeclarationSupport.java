package org.seasar.doma.jdbc.criteria.declaration;

import org.seasar.doma.jdbc.criteria.context.Operand;
import org.seasar.doma.jdbc.criteria.def.PropertyDef;

public class DeclarationSupport {

  public <PROPERTY> Operand.Prop toProp(final PropertyDef<PROPERTY> propertyDef) {
    return new Operand.Prop(propertyDef);
  }

  public <PROPERTY> Operand.Param toParam(
      final PropertyDef<PROPERTY> propertyDef, final PROPERTY value) {
    return new Operand.Param(propertyDef, value);
  }
}
