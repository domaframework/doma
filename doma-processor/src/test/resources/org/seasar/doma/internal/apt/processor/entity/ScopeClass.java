package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;
import java.util.function.Consumer;

class ScopeClass {

    public Consumer<WhereDeclaration> startWithHoge(ScopedEntity_ e) {
        return w -> {
            w.like(e.name, "hoge%");
        };
    }
}