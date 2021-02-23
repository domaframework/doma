package org.seasar.doma.internal.apt.processor.metamodel;

import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

import java.time.LocalDateTime;
import java.util.function.Consumer;

class ItemScope implements Publishable<Item_>, GenericIdQueries<Item_, Long> {

    public PropertyMetamodel<LocalDateTime> publishedAt(Item_ e) {
        return e.publishedAt;
    }

    public PropertyMetamodel<Long> id(Item_ e) {
        return e.id;
    }
}