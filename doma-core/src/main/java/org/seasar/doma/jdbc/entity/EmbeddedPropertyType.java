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

import static java.util.stream.Collectors.toMap;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.jdbc.entity.PropertyField;
import org.seasar.doma.internal.jdbc.entity.PropertyPath;

/**
 * Represents a property in an entity class that is mapped to an embeddable object.
 *
 * <p>An embeddable property is a property whose type is annotated with {@link
 * org.seasar.doma.Embeddable} and contains multiple properties that are mapped to database columns.
 * This class manages the relationship between the entity property and its constituent embeddable
 * properties.
 *
 * <p>Implementations of this class are typically generated at compile time by the Doma annotation
 * processor.
 *
 * @param <ENTITY> the entity type containing this embeddable property
 * @param <EMBEDDABLE> the embeddable type
 * @see org.seasar.doma.Embeddable
 * @see org.seasar.doma.jdbc.entity.EmbeddableType
 */
public class EmbeddedPropertyType<ENTITY, EMBEDDABLE> {

  protected final PropertyPath path;

  protected final List<EntityPropertyType<ENTITY, ?>> embeddablePropertyTypes;

  protected final Map<String, EntityPropertyType<ENTITY, ?>> embeddablePropertyTypeMap;

  protected final boolean optional;

  protected final PropertyField<ENTITY> field;

  public EmbeddedPropertyType(
      String name,
      Class<ENTITY> entityClass,
      List<EntityPropertyType<ENTITY, ?>> embeddablePropertyType,
      boolean optional) {
    this(PropertyPath.of(name), entityClass, embeddablePropertyType, optional);
  }

  public EmbeddedPropertyType(
      PropertyPath path,
      Class<ENTITY> entityClass,
      List<EntityPropertyType<ENTITY, ?>> embeddablePropertyType,
      boolean optional) {
    if (path == null) {
      throw new DomaNullPointerException("path");
    }
    if (entityClass == null) {
      throw new DomaNullPointerException("entityClass");
    }
    if (embeddablePropertyType == null) {
      throw new DomaNullPointerException("embeddablePropertyType");
    }
    this.path = path;
    this.embeddablePropertyTypes = embeddablePropertyType;
    this.embeddablePropertyTypeMap =
        this.embeddablePropertyTypes.stream()
            .collect(
                toMap(
                    EntityPropertyType::getName,
                    Function.identity(),
                    (u, v) -> {
                      throw new IllegalStateException(String.format("Duplicate key %s", u));
                    },
                    LinkedHashMap::new));
    this.optional = optional;
    this.field = new PropertyField<>(path, entityClass);
  }

  /**
   * Returns the list of entity property types that are part of this embeddable property.
   *
   * <p>These property types represent the individual properties within the embeddable object that
   * are mapped to database columns.
   *
   * @return the list of entity property types in this embeddable property
   */
  public List<EntityPropertyType<ENTITY, ?>> getEmbeddablePropertyTypes() {
    return embeddablePropertyTypes;
  }

  /**
   * Returns a map of entity property types that are part of this embeddable property.
   *
   * <p>The map keys are the property names, and the values are the corresponding entity property
   * types. This provides a convenient way to look up properties by name within the embeddable
   * object.
   *
   * @return a map of property names to entity property types in this embeddable property
   */
  public Map<String, EntityPropertyType<ENTITY, ?>> getEmbeddablePropertyTypeMap() {
    return embeddablePropertyTypeMap;
  }

  /**
   * Sets the embeddable value in the entity.
   *
   * <p>This method is used to update the embeddable property in the entity with a new value,
   * typically during entity construction or when loading data from the database.
   *
   * @param entity the entity in which to set the embeddable value
   * @param value the embeddable value to set
   */
  public void save(ENTITY entity, EMBEDDABLE value) {
    Object v = optional ? Optional.ofNullable(value) : value;
    field.setValue(entity, v);
  }
}
