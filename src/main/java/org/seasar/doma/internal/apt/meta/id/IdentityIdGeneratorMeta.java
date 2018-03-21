package org.seasar.doma.internal.apt.meta.id;

import org.seasar.doma.jdbc.id.BuiltinIdentityIdGenerator;

/**
 * @author taedium
 * 
 */
public class IdentityIdGeneratorMeta implements IdGeneratorMeta {

    @Override
    public String getIdGeneratorClassName() {
        return BuiltinIdentityIdGenerator.class.getName();
    }

    @Override
    public <R, P> R accept(IdGeneratorMetaVisitor<R, P> visitor, P p) {
        return visitor.visitIdentityIdGeneratorMeta(this, p);
    }

}
