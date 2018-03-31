package org.seasar.doma.jdbc.entity;

import java.util.List;
import java.util.Map;

/**
 * A description for an embeddable class.
 *
 * @param <EMBEDDABLE> the embeddable type
 */
public interface EmbeddableDesc<EMBEDDABLE> {

  /**
   * Returns the properties that belongs to the embeddable class.
   *
   * @param embeddedPropertyName the name of the embedded property in the entity class
   * @param entityClass the entity class
   * @param namingType naming convention
   * @return the properties that belongs to the embeddable class
   */
  <ENTITY> List<EntityPropertyDesc<ENTITY, ?>> getEmbeddablePropertyDescs(
      String embeddedPropertyName, Class<ENTITY> entityClass, NamingType namingType);

  /**
   * Creates a new instance.
   *
   * @param embeddedPropertyName the name of the embedded property in the entity class
   * @param __args arguments
   * @return the embeddable instance
   */
  <ENTITY> EMBEDDABLE newEmbeddable(
      String embeddedPropertyName, Map<String, Property<ENTITY, ?>> __args);
}
