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
import org.seasar.doma.jdbc.entity.VersionPropertyType;
import org.seasar.doma.wrapper.Wrapper;

public class UpdateQueryHelper<E> {

  protected final Config config;

  protected final EntityType<E> entityType;

  protected final boolean nullExcluded;

  protected final boolean versionIgnored;

  protected final boolean optimisticLockExceptionSuppressed;

  protected final boolean unchangedPropertyIncluded;

  protected final String[] includedPropertyNames;

  protected final String[] excludedPropertyNames;

  public UpdateQueryHelper(
      Config config,
      EntityType<E> entityType,
      String[] includedPropertyNames,
      String[] excludedPropertyNames,
      boolean nullExcluded,
      boolean versionIgnored,
      boolean optimisticLockExceptionSuppressed,
      boolean unchangedPropertyIncluded) {
    this.config = config;
    this.entityType = entityType;
    this.nullExcluded = nullExcluded;
    this.versionIgnored = versionIgnored;
    this.optimisticLockExceptionSuppressed = optimisticLockExceptionSuppressed;
    this.unchangedPropertyIncluded = unchangedPropertyIncluded;
    this.includedPropertyNames = includedPropertyNames;
    this.excludedPropertyNames = excludedPropertyNames;
  }

  public List<EntityPropertyType<E, ?>> getTargetPropertyTypes(E entity) {
    int capacity = entityType.getEntityPropertyTypes().size();
    List<EntityPropertyType<E, ?>> results = new ArrayList<>(capacity);
    E originalStates = entityType.getOriginalStates(entity);
    for (EntityPropertyType<E, ?> propertyType : entityType.getEntityPropertyTypes()) {
      if (!propertyType.isUpdatable()) {
        continue;
      }
      if (propertyType.isId()) {
        continue;
      }
      if (!versionIgnored && propertyType.isVersion()) {
        continue;
      }
      if (propertyType.isTenantId()) {
        continue;
      }
      if (nullExcluded) {
        Property<E, ?> property = propertyType.createProperty();
        property.load(entity);
        if (property.getWrapper().get() == null) {
          continue;
        }
      }
      if (unchangedPropertyIncluded
          || originalStates == null
          || isChanged(entity, originalStates, propertyType)) {
        String name = propertyType.getName();
        if (!isTargetPropertyName(name)) {
          continue;
        }
        results.add(propertyType);
      }
    }
    return results;
  }

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

  protected boolean isChanged(E entity, E originalStates, EntityPropertyType<E, ?> propertyType) {
    Wrapper<?> wrapper = propertyType.createProperty().load(entity).getWrapper();
    Wrapper<?> originalWrapper = propertyType.createProperty().load(originalStates).getWrapper();
    return !wrapper.hasEqualValue(originalWrapper.get());
  }

  public void populateValues(
      E entity,
      List<EntityPropertyType<E, ?>> targetPropertyTypes,
      VersionPropertyType<E, ?, ?> versionPropertyType,
      SqlContext context) {
    Dialect dialect = config.getDialect();
    Naming naming = config.getNaming();
    for (EntityPropertyType<E, ?> propertyType : targetPropertyTypes) {
      Property<E, ?> property = propertyType.createProperty();
      property.load(entity);
      context.appendSql(propertyType.getColumnName(naming::apply, dialect::applyQuote));
      context.appendSql(" = ");
      context.appendParameter(property.asInParameter());
      context.appendSql(", ");
    }
    if (!versionIgnored && versionPropertyType != null) {
      Property<E, ?> property = versionPropertyType.createProperty();
      property.load(entity);
      context.appendSql(versionPropertyType.getColumnName(naming::apply, dialect::applyQuote));
      context.appendSql(" = ");
      context.appendParameter(property.asInParameter());
      context.appendSql(" + 1");
    } else {
      context.cutBackSql(2);
    }
  }
}
