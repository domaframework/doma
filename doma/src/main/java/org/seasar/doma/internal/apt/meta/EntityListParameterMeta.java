package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.Assertions.*;

/**
 * @author taedium
 * 
 */
public class EntityListParameterMeta extends AbstractParameterMeta {

    protected final String entityTypeName;

    public EntityListParameterMeta(String entityTypeName) {
        assertNotNull(entityTypeName);
        this.entityTypeName = entityTypeName;
    }

    public String getEntityTypeName() {
        return entityTypeName;
    }

    @Override
    public <R, P> R accept(CallableStatementParameterMetaVisitor<R, P> visitor, P p) {
        return visitor.visistEntityListParameterMeta(this, p);
    }

}
