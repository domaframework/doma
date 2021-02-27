package org.seasar.doma.internal.apt.processor.metamodel;

import org.seasar.doma.Scope;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;

class RoomScope {

    @Scope
    public Consumer<WhereDeclaration> startWithHoge(Room_ e) {
        return w -> w.like(e.name, "hoge%");
    }

    @Scope
    public Consumer<WhereDeclaration> ids(Room_ e, long[] ids) {
        return w -> w.in(e.id, Arrays.stream(ids).boxed().collect(Collectors.toList()));
    }

    @Scope
    public Consumer<WhereDeclaration> names(Room_ e, String... names) {
        return w -> w.in(e.name, Arrays.asList(names));
    }

    @Scope
    public Consumer<WhereDeclaration> idAndName(Room_ e, long id, String name) {
        return w -> {
            w.eq(e.id, id);
            w.eq(e.name, name);
        };
    }

    @Scope
    public Consumer<WhereDeclaration> stringList(Room_ e, List<String> names) {
        return w -> w.in(e.name, names);
    }

    @Scope
    public Consumer<WhereDeclaration> numberList(Room_ e, List<? extends Number> numbers) {
        return w -> w.in(e.id, numbers.stream().map(Number::longValue).collect(Collectors.toList()));
    }

    @Scope
    public <T> T genericMethod(Room_ e, T t) {
        return t;
    }

    @Scope
    public <T extends Number> T boundedGenericMethod(Room_ e, T t) {
        return t;
    }

    @Scope
    public <A extends Number, B> List<A> multipleGenericTypeMethod(Room_ e, A a, B b) {
        return new ArrayList<>(Collections.singletonList(a));
    }

    @Scope
    public <A extends Number, B extends List<A> & Serializable> List<A> boundedGenericTypeMethod(Room_ e, A a, B b) {
        return new ArrayList<>(Collections.singletonList(a));
    }
}