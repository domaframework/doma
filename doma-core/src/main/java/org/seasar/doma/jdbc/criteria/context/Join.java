package org.seasar.doma.jdbc.criteria.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;

public class Join {
  public final EntityMetamodel<?> entityMetamodel;
  public final JoinKind kind;
  public final List<Criterion> on = new ArrayList<>();

  public Join(EntityMetamodel<?> entityMetamodel, JoinKind kind) {
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.kind = Objects.requireNonNull(kind);
  }
}
