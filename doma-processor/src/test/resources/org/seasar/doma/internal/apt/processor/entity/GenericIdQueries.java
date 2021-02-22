package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Scope;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;

import java.util.List;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

interface GenericIdQueries<E extends EntityMetamodel<?>, ID> {

    PropertyMetamodel<ID> id(E e);

    @Scope
    default Consumer<WhereDeclaration> idEq(E e, ID id) {
        return w -> w.eq(id(e), id);
    }

    @Scope
    default Consumer<WhereDeclaration> idEq(E e, List<ID> id) {
        return w -> w.in(id(e), id);
    }
}