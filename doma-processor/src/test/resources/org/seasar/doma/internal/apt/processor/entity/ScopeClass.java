package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.ScopeMethod;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

class ScopeClass {

    @ScopeMethod
    public Consumer<WhereDeclaration> startWithHoge(ScopedEntity_ e) {
        return w -> {
            w.like(e.name, "hoge%");
        };
    }

    @ScopeMethod
    public Consumer<WhereDeclaration> ids(ScopedEntity_ e, long[] ids) {
        return w -> {
            w.in(e.id, Arrays.stream(ids).boxed().collect(Collectors.toList()));
        };
    }

    @ScopeMethod
    public Consumer<WhereDeclaration> names(ScopedEntity_ e, String... names) {
        return w -> {
            w.in(e.name, Arrays.asList(names));
        };
    }

    @ScopeMethod
    public Consumer<WhereDeclaration> idAndName(ScopedEntity_ e, long id, String name) {
        return w -> {
            w.eq(e.id, id);
            w.eq(e.name, name);
        };
    }
}