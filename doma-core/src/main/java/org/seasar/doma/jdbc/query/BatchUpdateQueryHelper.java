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
package org.seasar.doma.jdbc.query;

import java.util.ArrayList;
import java.util.List;
import org.seasar.doma.internal.jdbc.sql.SqlContext;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.Property;

/**
 * A helper class for batch update queries. This class provides utility methods for handling entity
 * properties in batch update operations.
 *
 * @param <E> the entity type
 */
public class BatchUpdateQueryHelper<E> {

  /** The configuration. */
  protected final Config config;

  /** The entity type. */
  protected final EntityType<E> entityType;

  /** Whether the version property is ignored. */
  protected final boolean versionIgnored;

  /** Whether the optimistic lock exception is suppressed. */
  protected final boolean optimisticLockExceptionSuppressed;

  /** The included property names. */
  protected final String[] includedPropertyNames;

  /** The excluded property names. */
  protected final String[] excludedPropertyNames;

  /**
   * Constructs a new {@code BatchUpdateQueryHelper}.
   *
   * @param config the configuration
   * @param entityType the entity type
   * @param includedPropertyNames the included property names
   * @param excludedPropertyNames the excluded property names
   * @param versionIgnored whether the version property is ignored
   * @param optimisticLockExceptionSuppressed whether the optimistic lock exception is suppressed
   */
  public BatchUpdateQueryHelper(
      Config config,
      EntityType<E> entityType,
      String[] includedPropertyNames,
      String[] excludedPropertyNames,
      boolean versionIgnored,
      boolean optimisticLockExceptionSuppressed) {
    this.config = config;
    this.entityType = entityType;
    this.versionIgnored = versionIgnored;
    this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
    this.includedPropertyNames = includedPropertyNames;
    this.excludedPropertyNames = excludedPropertyNames;
  }

  /**
   * Returns the target property types for the update operation. This method filters the entity
   * property types based on various criteria.
   *
   * @return the list of target property types
   */
  public List<EntityPropertyType<E, ?>> getTargetPropertyTypes() {
    List<EntityPropertyType<E, ?>> targetPropertyTypes =
        new ArrayList<>(entityType.getEntityPropertyTypes().size());
    for (EntityPropertyType<E, ?> p : entityType.getEntityPropertyTypes()) {
      if (!p.isUpdatable()) {
        continue;
      }
      if (p.isId()) {
        continue;
      }
      if (p.isVersion()) {
        targetPropertyTypes.add(p);
        continue;
      }
      if (p.isTenantId()) {
        continue;
      }
      if (!isTargetPropertyName(p.getName())) {
        continue;
      }
      targetPropertyTypes.add(p);
    }
    return targetPropertyTypes;
  }

  /**
   * Determines if the property name is a target for the update operation. This method checks if the
   * property name is included and not excluded.
   *
   * @param name the property name
   * @return true if the property name is a target, false otherwise
   */
  protected boolean isTargetPropertyName(String name) {
    if (includedPropertyNames.length > 0) {
      for (String includedName : includedPropertyNames) {
        if (includedName.equals(name)) {
          for (String excludedName : excludedPropertyNames) {
            if (excludedName.equals(name)) {
              return false;
            }
          }
          return true;
        }
      }
      return false;
    }
    if (excludedPropertyNames.length > 0) {
      for (String excludedName : excludedPropertyNames) {
        if (excludedName.equals(name)) {
          return false;
        }
      }
      return true;
    }
    return true;
  }

  /**
   * Populates the SQL context with values from the entity. This method appends SQL for setting
   * column values in the update statement.
   *
   * @param entity the entity
   * @param targetPropertyTypes the target property types
   * @param versionPropertyType the version property type
   * @param context the SQL context
   */
  public void populateValues(
      E entity,
      List<EntityPropertyType<E, ?>> targetPropertyTypes,
      EntityPropertyType<E, ?> versionPropertyType,
      SqlContext context) {
    Dialect dialect = config.getDialect();
    Naming naming = config.getNaming();
    for (EntityPropertyType<E, ?> propertyType : targetPropertyTypes) {
      Property<E, ?> property = propertyType.createProperty();
      property.load(entity);
      context.appendSql(propertyType.getColumnName(naming::apply, dialect::applyQuote));
      context.appendSql(" = ");
      context.appendParameter(property.asInParameter());
      if (propertyType.isVersion() && !versionIgnored) {
        context.appendSql(" + 1");
      }
      context.appendSql(", ");
    }
    context.cutBackSql(2);
  }
}
