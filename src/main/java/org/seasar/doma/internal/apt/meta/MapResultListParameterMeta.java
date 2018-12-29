package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import org.seasar.doma.internal.apt.cttype.MapCtType;

public class MapResultListParameterMeta implements ResultListParameterMeta {

  protected final MapCtType mapCtType;

  public MapResultListParameterMeta(MapCtType mapCtType) {
    assertNotNull(mapCtType);
    this.mapCtType = mapCtType;
  }

  public MapCtType getMapCtType() {
    return mapCtType;
  }

  @Override
  public <R, P> R accept(CallableSqlParameterMetaVisitor<R, P> visitor, P p) {
    return visitor.visitMapResultListParameterMeta(this, p);
  }
}
