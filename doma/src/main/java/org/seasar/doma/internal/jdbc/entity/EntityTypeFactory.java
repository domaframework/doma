package org.seasar.doma.internal.jdbc.entity;

public interface EntityTypeFactory<E> {

    EntityType<E> createEntityType();

    EntityType<E> createEntityType(E entity);
}
