package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.ScopeMethod;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;

class NonParamMethodScope {

    @ScopeMethod
    public Consumer<WhereDeclaration> test() {
        return w -> {};
    }
}
