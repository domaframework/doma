package org.seasar.doma.jdbc.criteria.declaration;

import org.seasar.doma.jdbc.criteria.context.Join;

public class JoinDeclaration extends ComparisonDeclaration {

  public JoinDeclaration(Join join) {
    super(() -> join.on, on -> join.on = on);
  }
}
