package org.seasar.doma.internal.apt.meta.parameter;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.internal.apt.cttype.MapCtType;

/**
 * @author taedium
 * 
 */
public class MapResultListParameterMeta implements ResultListParameterMeta {

    private final MapCtType mapCtType;

    public MapResultListParameterMeta(MapCtType mapCtType) {
        assertNotNull(mapCtType);
        this.mapCtType = mapCtType;
    }

    public MapCtType getMapCtType() {
        return mapCtType;
    }

    @Override
    public <P> void accept(CallableSqlParameterMetaVisitor<P> visitor, P p) {
        visitor.visitMapResultListParameterMeta(this, p);
    }

}
