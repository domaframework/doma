package org.seasar.doma.internal.apt.meta;

import org.seasar.doma.internal.jdbc.id.IdentityIdGenerator;

/**
 * @author taedium
 * 
 */
public class IdentityIdGeneratorMeta implements IdGeneratorMeta {

    @Override
    public String getIdGeneratorClassName() {
        return IdentityIdGenerator.class.getName();
    }

    @Override
    public <R, P> R accept(IdGeneratorMetaVisitor<R, P> visitor, P p) {
        return visitor.visistIdentityIdGeneratorMeta(this, p);
    }

}
