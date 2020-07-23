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
   * <p>This method always returns {@code true}, when {@link Update#sqlFile()} returns {@code true}.
   *
   * @return {@code true} if the entity is changed
   */
  public boolean isEntityChanged();

  /**
   * Whether the entity property is changed.
   *
   * <p>This method always returns {@code true}, when {@link Update#sqlFile()} returns {@code true}.
   *
   * @param propertyName the name of property
   * @return {@code true} if the property is changed
   * @exception EntityPropertyNotDefinedException if the property is not defined in the entity
   * @see OriginalStates
   */
  public boolean isPropertyChanged(String propertyName);

  /**
   * Returns the entity description.
   *
   * @return the entity description
   */
  public EntityType<?> getEntityType();

  /**
   * The method that is annotated with {@link Update}.
   *
   * @return the method
   */
  public Method getMethod();

  /**
   * Returns the configuration.
   *
   * @return the configuration
   */
  public Config getConfig();

  /**
   * Returns the new entity.
   *
   * @return the new entity
   */
  public E getNewEntity();

  /**
   * Sets the new entity.
   *
   * <p>This method is available, when the entity is immutable.
   *
   * @param newEntity the entity
   * @throws DomaNullPointerException if {@code newEntity} is {@code null}
   */
  public void setNewEntity(E newEntity);
}
