package org.seasar.doma.jdbc.entity;

import java.lang.reflect.Method;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.OriginalStates;
import org.seasar.doma.Update;
import org.seasar.doma.jdbc.Config;

/**
 * A context for a pre process of an update.
 *
 * @param <E> the entity type
 */
public interface PreUpdateContext<E> {

  /**
   * Whether the entity is changed.
   *
   * <p>This method always returns {@code true}, when {@link Update} is used in combination with
   * {@link org.seasar.doma.Sql}.
   *
   * @return {@code true} if the entity is changed
   */
  boolean isEntityChanged();

  /**
   * Whether the entity property is changed.
   *
   * <p>This method always returns {@code true}, when {@link Update} is used in combination with
   * {@link org.seasar.doma.Sql}.
   *
   * @param propertyName the name of property
   * @return {@code true} if the property is changed
   * @exception EntityPropertyNotDefinedException if the property is not defined in the entity
   * @see OriginalStates
   */
  boolean isPropertyChanged(String propertyName);

  /**
   * Returns the entity description.
   *
   * @return the entity description
   */
  EntityDesc<?> getEntityDesc();

  /**
   * The method that is annotated with {@link Update}.
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
}
