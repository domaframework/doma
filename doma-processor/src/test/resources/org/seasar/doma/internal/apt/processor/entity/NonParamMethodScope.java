package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Scope;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;

class NonParamMethodScope {

    @Scope
    public Consumer<WhereDeclaration> test() {
        return w -> {};
    }
}
