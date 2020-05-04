package org.seasar.doma.jdbc.criteria.declaration;

import java.util.Objects;
import org.seasar.doma.jdbc.criteria.context.InsertContext;
import org.seasar.doma.jdbc.criteria.def.PropertyDef;

public class ValuesDeclaration {

  private final InsertContext context;
  private final DeclarationSupport support;

  public ValuesDeclaration(InsertContext context) {
    Objects.requireNonNull(context);
    this.context = context;
    this.support = new DeclarationSupport();
  }

  public <PROPERTY> void value(PropertyDef<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    Objects.requireNonNull(right);
    context.values.put(support.toProp(left), support.toParam(left, right));
  }
}
