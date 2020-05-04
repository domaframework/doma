package org.seasar.doma.jdbc.criteria.declaration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.seasar.doma.jdbc.criteria.context.Criterion;
import org.seasar.doma.jdbc.criteria.context.SelectContext;

public class HavingDeclaration extends ComparisonDeclaration<SelectContext> {

  public HavingDeclaration(SelectContext context) {
    super(context);
  }

  @Override
  protected void runBlock(Runnable block, Function<List<Criterion>, Criterion> newCriterion) {
    List<Criterion> preservedWhere = context.having;
    List<Criterion> newWhere = new ArrayList<>();
    context.having = newWhere;
    block.run();
    context.having = preservedWhere;
    if (!newWhere.isEmpty()) {
      add(newCriterion.apply(newWhere));
    }
  }

  @Override
  protected void add(Criterion criterion) {
    context.having.add(criterion);
  }
}
