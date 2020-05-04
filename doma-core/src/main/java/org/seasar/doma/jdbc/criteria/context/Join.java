package org.seasar.doma.jdbc.criteria.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.criteria.def.EntityDef;

public class Join {
  public final EntityDef<?> entityDef;
  public final JoinKind kind;
  public final List<Criterion> on = new ArrayList<>();

  public Join(EntityDef<?> entityDef, JoinKind kind) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(kind);
    this.entityDef = entityDef;
    this.kind = kind;
  }
}
