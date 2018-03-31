package org.seasar.doma.internal.apt.meta.parameter;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

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
  public <P> void accept(CallableSqlParameterMetaVisitor<P> visitor, P p) {
    visitor.visitMapListParameterMeta(this, p);
  }
}
