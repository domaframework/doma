package org.seasar.doma.internal.apt.meta.parameter;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.internal.apt.cttype.EntityCtType;

/**
 * @author taedium
 * 
 */
public class EntityListParameterMeta implements CallableSqlParameterMeta {

    private final String name;

    private final EntityCtType entityCtType;

    private final boolean ensureResultMapping;

    public EntityListParameterMeta(String name, EntityCtType entityCtType,
            boolean ensureResultMapping) {
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
    public <P> void accept(CallableSqlParameterMetaVisitor<P> visitor, P p) {
        visitor.visitEntityListParameterMeta(this, p);
    }

}
