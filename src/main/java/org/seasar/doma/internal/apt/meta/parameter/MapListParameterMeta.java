package org.seasar.doma.internal.apt.meta.parameter;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import org.seasar.doma.internal.apt.cttype.MapCtType;

public class MapListParameterMeta implements CallableSqlParameterMeta {

  private final String name;

  private final MapCtType mapCtType;

  public MapListParameterMeta(String name, MapCtType mapCtType) {
    assertNotNull(name, mapCtType);
    this.name = name;
    this.mapCtType = mapCtType;
  }

  public String getName() {
    return name;
  }

  public MapCtType getMapCtType() {
    return mapCtType;
  }

  @Override
  public <R, P> R accept(CallableSqlParameterMetaVisitor<R, P> visitor, P p) {
    return visitor.visitMapListParameterMeta(this, p);
  }
}
