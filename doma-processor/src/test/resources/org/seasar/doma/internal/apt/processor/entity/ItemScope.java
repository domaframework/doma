package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

import java.time.LocalDateTime;
import java.util.function.Consumer;

class ItemScope implements Publishable<ItemEntity_>, GenericIdQueries<ItemEntity_, Long> {

    public PropertyMetamodel<LocalDateTime> publishedAt(ItemEntity_ e) {
        return e.publishedAt;
    }

    public PropertyMetamodel<Long> id(ItemEntity_ e) {
        return e.id;
    }
}