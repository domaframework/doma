package org.seasar.doma.jdbc.entity;

import java.lang.reflect.Method;
import java.util.Optional;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.Insert;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

/**
 * A context for a post process of an insert.
 *
 * @param <E> the entity type
 */
public interface PostInsertContext<E> {

  /**
   * Returns the entity description.
   *
   * @return the entity description
   */
  EntityType<E> getEntityType();

  /**
   * The method that is annotated with {@link Insert}.
   *
   * @return the method
   */
  Method getMethod();

  /**
   * Returns the configuration.
   *
   * @return the configuration
   */
  Config getConfig();

  /**
   * Returns the new entity.
   *
   * @return the new entity
   */
  E getNewEntity();

  /**
   * Sets the new entity.
   *
   * <p>This method is available, when the entity is immutable.
   *
   * @param newEntity the entity
   * @throws DomaNullPointerException if {@code newEntity} is {@code null}
   */
  void setNewEntity(E newEntity);

  /**
   * Retrieves the type of the duplicate key when inserting a new entity.
   *
   * @return an Optional containing the DuplicateKeyType if the entity is a duplicate, or an empty
   *     Optional if it is not a duplicate
   */
  default Optional<DuplicateKeyType> getDuplicateKeyType() {
    return Optional.empty();
  }
}
