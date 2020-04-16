package org.seasar.doma.jdbc.entity;

import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.JdbcMappable;

/**
 * An entity property.
 *
 * @param <ENTITY> the entity type
 * @param <BASIC> the basic type
 */
public interface Property<ENTITY, BASIC> extends JdbcMappable<BASIC> {

  /**
   * Returns the value of this property.
   *
   * @return the value of this property
   */
  Object get();

  /**
   * Loads the value from the entity to this property.
   *
   * @param entity the entity
   * @return this instance
   */
  Property<ENTITY, BASIC> load(ENTITY entity);

  /**
   * Saves the value from this property to the entity.
   *
   * @param entity the entity
   * @return this instance
   */
  Property<ENTITY, BASIC> save(ENTITY entity);

  /**
   * Returns this property as {@link InParameter}.
   *
   * @return the input parameter
   */
  InParameter<BASIC> asInParameter();
}
