package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.Assertions.*;

/**
 * @author taedium
 * 
 */
public class DomainListParameterMeta extends AbstractParameterMeta {

    protected final String domainTypeName;

    public DomainListParameterMeta(String domainTypeName) {
        assertNotNull(domainTypeName);
        this.domainTypeName = domainTypeName;
    }

    public String getDomainTypeName() {
        return domainTypeName;
    }

    @Override
    public <R, P> R accept(CallableStatementParameterMetaVisitor<R, P> visitor, P p) {
        return visitor.visistDomainListParameterMeta(this, p);
    }

}
