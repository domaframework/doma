package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.ScopeMethod;
import org.seasar.doma.jdbc.criteria.declaration.OrderByNameDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;

import java.util.List;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

interface GenericIdQueries<E extends EntityMetamodel<?>, ID> {

    PropertyMetamodel<ID> id(E e);

    @ScopeMethod
    default Consumer<WhereDeclaration> idEq(E e, ID id) {
        return w -> w.eq(id(e), id);
    }

    @ScopeMethod
    default Consumer<WhereDeclaration> idEq(E e, List<ID> id) {
        return w -> w.in(id(e), id);
    }
}