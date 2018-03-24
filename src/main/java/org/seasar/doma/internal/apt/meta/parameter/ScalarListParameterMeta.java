package org.seasar.doma.internal.apt.meta.parameter;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.internal.apt.cttype.ScalarCtType;

/**
 * @author taedium
 * 
 */
public class ScalarListParameterMeta implements CallableSqlParameterMeta {

    private final String name;

    private final ScalarCtType scalarCtType;

    private final boolean optional;

    public ScalarListParameterMeta(String name, ScalarCtType scalarCtType, boolean optional) {
        assertNotNull(name, scalarCtType);
        this.name = name;
        this.scalarCtType = scalarCtType;
        this.optional = optional;
    }

    public String getName() {
        return name;
    }

    public ScalarCtType getScalarCtType() {
        return scalarCtType;
    }

    public boolean isOptional() {
        return optional;
    }

    @Override
    public <P> void accept(CallableSqlParameterMetaVisitor<P> visitor, P p) {
        visitor.visitScalarListParameterMeta(this, p);
    }

}
