package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;
import java.util.function.Consumer;

class ScopeClass {

    public Consumer<WhereDeclaration> searchByName(ScopedEntity_ e, String text) {
        return w -> {
            w.like(e.name, "%" + text + "%");
        };
    }
}