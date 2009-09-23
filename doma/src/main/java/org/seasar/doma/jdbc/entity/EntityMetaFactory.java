package org.seasar.doma.jdbc.entity;

public interface EntityMetaFactory<E> {

    EntityMeta<E> createEntityMeta();

    EntityMeta<E> createEntityMeta(E entity);
}
