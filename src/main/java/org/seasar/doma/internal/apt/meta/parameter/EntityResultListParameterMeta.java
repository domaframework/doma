package org.seasar.doma.internal.apt.meta.parameter;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.internal.apt.cttype.EntityCtType;

/**
 * @author taedium
 * 
 */
public class EntityResultListParameterMeta implements ResultListParameterMeta {

    private final EntityCtType entityCtType;

    private final boolean ensureResultMapping;

    public EntityResultListParameterMeta(EntityCtType entityCtType, boolean ensureResultMapping) {
        assertNotNull(entityCtType);
        this.entityCtType = entityCtType;
        this.ensureResultMapping = ensureResultMapping;
    }

    public EntityCtType getEntityCtType() {
        return entityCtType;
    }

    public boolean getEnsureResultMapping() {
        return ensureResultMapping;
    }

    @Override
    public <R, P> R accept(CallableSqlParameterMetaVisitor<R, P> visitor, P p) {
        return visitor.visitEntityResultListParameterMeta(this, p);
    }

}
