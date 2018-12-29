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
 * @author nakamura-to
 * @since 2.3.0
 */
public class BatchUpdateQueryHelper<E> {

  protected final Config config;

  protected final EntityType<E> entityType;

  protected final boolean versionIgnored;

  protected final boolean optimisticLockExceptionSuppressed;

  protected final String[] includedPropertyNames;

  protected final String[] excludedPropertyNames;

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

  public List<EntityPropertyType<E, ?>> getTargetPropertyTypes() {
    List<EntityPropertyType<E, ?>> targetPropertyTypes =
        new ArrayList<EntityPropertyType<E, ?>>(entityType.getEntityPropertyTypes().size());
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
