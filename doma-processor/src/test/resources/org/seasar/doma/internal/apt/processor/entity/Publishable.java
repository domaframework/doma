package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.ScopeMethod;
import org.seasar.doma.jdbc.criteria.declaration.OrderByNameDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

interface Publishable<E extends EntityMetamodel<?>> {

    PropertyMetamodel<LocalDateTime> publishedAt(E e);

    @ScopeMethod
    default Consumer<WhereDeclaration> published(E e) {
        return w -> {
            w.le(publishedAt(e), LocalDateTime.now());
        };
    }

    @ScopeMethod
    default Consumer<OrderByNameDeclaration> newest(E e) {
        return o -> o.desc(publishedAt(e));
    }
}