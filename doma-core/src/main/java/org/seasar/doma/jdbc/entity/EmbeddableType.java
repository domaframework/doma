/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.jdbc.entity;

import java.util.List;
import java.util.Map;

/**
 * Represents the metadata for a class annotated with {@link org.seasar.doma.Embeddable}.
 *
 * <p>An embeddable type is a component class that can be embedded in an entity class and contains
 * multiple properties that are mapped to database columns. This interface provides methods to
 * create instances of embeddable objects and access their property types.
 *
 * <p>Implementations of this interface are typically generated at compile time by the Doma
 * annotation processor.
 *
 * @param <EMBEDDABLE> the embeddable type
 * @see org.seasar.doma.Embeddable
 * @see org.seasar.doma.jdbc.entity.EmbeddedPropertyType
 */
public interface EmbeddableType<EMBEDDABLE> {

  /**
   * Returns a list of entity property types for the embeddable properties.
   *
   * <p>This method is used to obtain metadata about the properties within an embeddable object when
   * it is embedded in an entity. The property types are used for mapping between the embeddable
   * object's properties and database columns.
   *
   * @param <ENTITY> the entity type that contains the embeddable
   * @param embeddedPropertyName the name of the property in the entity that holds the embeddable
   * @param entityClass the entity class
   * @param namingType the naming convention used for column names
   * @return a list of entity property types for the embeddable properties
   */
  @Deprecated
  default <ENTITY> List<EntityPropertyType<ENTITY, ?>> getEmbeddablePropertyTypes(
      String embeddedPropertyName, Class<ENTITY> entityClass, NamingType namingType) {
    return getEmbeddablePropertyTypes(embeddedPropertyName, entityClass, namingType, "");
  }

  /**
   * Returns a list of entity property types for the embeddable properties with a column name
   * prefix.
   *
   * <p>This method is used to obtain metadata about the properties within an embeddable object when
   * it is embedded in an entity. The property types are used for mapping between the embeddable
   * object's properties and database columns. The column name prefix is applied to all column names
   * of the embeddable properties, which is useful when the same embeddable type is used multiple
   * times within an entity.
   *
   * @param <ENTITY> the entity type that contains the embeddable
   * @param embeddedPropertyName the name of the property in the entity that holds the embeddable
   * @param entityClass the entity class
   * @param namingType the naming convention used for column names
   * @param columNamePrefix the prefix to be prepended to column names of all embeddable properties
   * @return a list of entity property types for the embeddable properties
   */
  <ENTITY> List<EntityPropertyType<ENTITY, ?>> getEmbeddablePropertyTypes(
      String embeddedPropertyName,
      Class<ENTITY> entityClass,
      NamingType namingType,
      String columNamePrefix);

  /**
   * Creates a new instance of the embeddable object.
   *
   * <p>This method is used to create a new embeddable object instance with the provided property
   * values. It is typically called when constructing an entity from database query results.
   *
   * @param <ENTITY> the entity type that contains the embeddable
   * @param embeddedPropertyName the name of the property in the entity that holds the embeddable
   * @param __args a map of property names to property values for initializing the embeddable
   * @return a new instance of the embeddable object
   */
  <ENTITY> EMBEDDABLE newEmbeddable(
      String embeddedPropertyName, Map<String, Property<ENTITY, ?>> __args);
}
