package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.ScopeMethod;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;

class StaticMethodScope {

    @ScopeMethod
    static Consumer<WhereDeclaration> test(StaticMethodScopeEntity_ e) {
        return w -> {};
    }
}
