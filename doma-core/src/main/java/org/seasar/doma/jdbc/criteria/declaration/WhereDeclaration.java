package org.seasar.doma.jdbc.criteria.declaration;

import java.util.Objects;
import org.seasar.doma.jdbc.criteria.context.DeleteContext;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.context.UpdateContext;

/** The where declaration. */
public class WhereDeclaration extends ComparisonDeclaration {

  public WhereDeclaration(SelectContext context) {
    super(() -> context.where, w -> context.where = w);
    Objects.requireNonNull(context);
  }

  public WhereDeclaration(DeleteContext context) {
    super(() -> context.where, w -> context.where = w);
    Objects.requireNonNull(context);
  }

  public WhereDeclaration(UpdateContext context) {
    super(() -> context.where, w -> context.where = w);
    Objects.requireNonNull(context);
  }
}
