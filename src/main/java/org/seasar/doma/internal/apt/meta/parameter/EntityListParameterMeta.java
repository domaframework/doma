package org.seasar.doma.internal.apt.meta.parameter;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import org.seasar.doma.internal.apt.cttype.EntityCtType;

public class EntityListParameterMeta implements CallableSqlParameterMeta {

  protected final String name;

  protected final EntityCtType entityCtType;

  protected final boolean ensureResultMapping;

  public EntityListParameterMeta(
      String name, EntityCtType entityCtType, boolean ensureResultMapping) {
    assertNotNull(name, entityCtType);
    this.name = name;
    this.entityCtType = entityCtType;
    this.ensureResultMapping = ensureResultMapping;
  }

  public String getName() {
    return name;
  }

  public EntityCtType getEntityCtType() {
    return entityCtType;
  }

  public boolean getEnsureResultMapping() {
    return ensureResultMapping;
  }

  @Override
  public <R, P> R accept(CallableSqlParameterMetaVisitor<R, P> visitor, P p) {
    return visitor.visitEntityListParameterMeta(this, p);
  }
}
