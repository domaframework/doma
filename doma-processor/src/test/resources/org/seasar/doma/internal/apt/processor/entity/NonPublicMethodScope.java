package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Scope;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;

class NonPublicMethodScope {

    @Scope
    Consumer<WhereDeclaration> test(StaticMethodScopeEntity_ e) {
        return w -> {};
    }
}
