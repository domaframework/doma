package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.ScopeMethod;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;

import java.io.Serializable;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;

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

    @ScopeMethod
    public Consumer<WhereDeclaration> stringList(ScopedEntity_ e, List<String> names) {
        return w -> {
            w.in(e.name, names);
        };
    }

    @ScopeMethod
    public Consumer<WhereDeclaration> numberList(ScopedEntity_ e, List<? extends Number> numbers) {
        return w -> {
            w.in(e.id, numbers.stream().map(Number::longValue).collect(Collectors.toList()));
        };
    }

    @ScopeMethod
    public <T> T genericMethod(ScopedEntity_ e, T t) {
        return t;
    }

    @ScopeMethod
    public <T extends Number> T boundedGenericMethod(ScopedEntity_ e, T t) {
        return t;
    }

    @ScopeMethod
    public <A extends Number, B> List<A> multipleGenericTypeMethod(ScopedEntity_ e, A a, B b) {
        return new ArrayList<>(Arrays.asList(a));
    }

    @ScopeMethod
    public <A extends Number, B extends List<A> & Serializable> List<A> boundedGenericTypeMethod(ScopedEntity_ e, A a, B b) {
        return new ArrayList<>(Arrays.asList(a));
    }
}