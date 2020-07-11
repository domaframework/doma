package org.seasar.doma.jdbc.criteria.declaration;

import java.util.Objects;
import org.seasar.doma.jdbc.criteria.context.SelectContext;

/** The having declaration. */
public class HavingDeclaration extends ComparisonDeclaration {

  public HavingDeclaration(SelectContext context) {
    super(() -> context.having, h -> context.having = h);
    Objects.requireNonNull(context);
  }
}
