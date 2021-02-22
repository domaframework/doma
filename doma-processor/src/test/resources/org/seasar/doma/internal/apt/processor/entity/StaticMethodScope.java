package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Scope;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;

class StaticMethodScope {

    @Scope
    static Consumer<WhereDeclaration> test(StaticMethodScopeEntity_ e) {
        return w -> {};
    }
}
