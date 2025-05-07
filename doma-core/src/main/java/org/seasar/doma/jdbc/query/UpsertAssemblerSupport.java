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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;

/**
 * Support class for assembling UPSERT SQL statements. Provides utilities for handling table and
 * column names with different formatting options.
 */
public class UpsertAssemblerSupport {
  /** The naming convention used for converting Java names to database names. */
  private final Naming naming;

  /** The dialect that defines database-specific behaviors. */
  private final Dialect dialect;

  /** Constants for table aliases used in UPSERT statements. */
  private final UpsertAliasConstants aliasConstants;

  /**
   * Creates a new instance with default alias constants.
   *
   * @param naming the naming convention
   * @param dialect the SQL dialect
   */
  public UpsertAssemblerSupport(Naming naming, Dialect dialect) {
    this(naming, dialect, new DefaultUpsertAliasConstants());
  }

  /**
   * Creates a new instance with the specified alias constants.
   *
   * @param naming the naming convention
   * @param dialect the SQL dialect
   * @param aliasConstants the constants for table aliases
   */
  public UpsertAssemblerSupport(
      Naming naming, Dialect dialect, UpsertAliasConstants aliasConstants) {
    this.naming = Objects.requireNonNull(naming);
    this.dialect = Objects.requireNonNull(dialect);
    this.aliasConstants = Objects.requireNonNull(aliasConstants);
  }

  /** Enum representing different ways to format table names in SQL statements. */
  public enum TableNameType {
    /** Use only the table name. */
    NAME,
    /** Use only the alias. */
    ALIAS,
    /** Use "as alias" format. */
    AS_ALIAS,
    /** Use "name alias" format. */
    NAME_ALIAS,
    /** Use "name as alias" format. */
    NAME_AS_ALIAS,
  }

  /** Enum representing different ways to format column names in SQL statements. */
  public enum ColumnNameType {
    /** Use only the column name. */
    NAME,
    /** Use "alias.name" format. */
    NAME_ALIAS,
  }

  /** Default implementation of UpsertAliasConstants that provides standard alias names. */
  public static class DefaultUpsertAliasConstants implements UpsertAliasConstants {
    /**
     * {@inheritDoc}
     *
     * @return "target" as the default target alias
     */
    @Override
    public String getTargetAlias() {
      return "target";
    }

    /**
     * {@inheritDoc}
     *
     * @return "excluded" as the default excluded alias
     */
    @Override
    public String getExcludedAlias() {
      return "excluded";
    }
  }

  /** Interface defining constants for table aliases used in UPSERT statements. */
  public interface UpsertAliasConstants {
    /**
     * Gets the alias for the target table.
     *
     * @return the target table alias
     */
    String getTargetAlias();

    /**
     * Gets the alias for the excluded table.
     *
     * @return the excluded table alias
     */
    String getExcludedAlias();
  }

  /**
   * Formats a table name based on the specified entity type and table name type.
   *
   * @param entityType the entity type
   * @param tableNameType the table name formatting type
   * @return the formatted table name
   * @throws IllegalArgumentException if the table name type is unknown
   */
  public String targetTable(EntityType<?> entityType, TableNameType tableNameType) {
    switch (tableNameType) {
      case NAME:
        return entityType.getQualifiedTableName(naming::apply, dialect::applyQuote);
      case ALIAS:
        return aliasConstants.getTargetAlias();
      case AS_ALIAS:
        return " as " + aliasConstants.getTargetAlias();
      case NAME_ALIAS:
        return entityType.getQualifiedTableName(naming::apply, dialect::applyQuote)
            + " "
            + aliasConstants.getTargetAlias();
      case NAME_AS_ALIAS:
        return entityType.getQualifiedTableName(naming::apply, dialect::applyQuote)
            + " as "
            + aliasConstants.getTargetAlias();
      default:
        throw new IllegalArgumentException("Unknown table name type: " + tableNameType);
    }
  }

  /**
   * Formats a table name based on the specified entity property types and table name type.
   *
   * @param entityPropertyTypes the entity property types
   * @param tableNameType the table name formatting type
   * @return the formatted table name
   * @throws UnsupportedOperationException if the table name type is NAME or NAME_ALIAS
   * @throws IllegalArgumentException if the table name type is unknown
   */
  public String targetTable(
      List<EntityPropertyType<?, ?>> entityPropertyTypes, TableNameType tableNameType) {
    switch (tableNameType) {
      case NAME:
      case NAME_ALIAS:
        throw new UnsupportedOperationException();
      case ALIAS:
        return aliasConstants.getTargetAlias();
      case AS_ALIAS:
        return " as " + aliasConstants.getTargetAlias();
      default:
        throw new IllegalArgumentException("Unknown table name type: " + tableNameType);
    }
  }

  /**
   * Formats a property name for the target table based on the specified property type and column
   * name type.
   *
   * @param propertyType the entity property type
   * @param columnNameType the column name formatting type
   * @return the formatted property name
   * @throws IllegalArgumentException if the column name type is unknown
   */
  public String targetProp(EntityPropertyType<?, ?> propertyType, ColumnNameType columnNameType) {
    switch (columnNameType) {
      case NAME:
        return propertyType.getColumnName(naming::apply, dialect::applyQuote);
      case NAME_ALIAS:
        return aliasConstants.getTargetAlias()
            + "."
            + propertyType.getColumnName(naming::apply, dialect::applyQuote);
      default:
        throw new IllegalArgumentException("Unknown table name type: " + columnNameType);
    }
  }

  /**
   * Gets the alias for the excluded table.
   *
   * @return the excluded table alias
   */
  public String excludeAlias() {
    return aliasConstants.getExcludedAlias();
  }

  /**
   * Formats a property name for the excluded table based on the specified property type and column
   * name type.
   *
   * @param propertyType the entity property type
   * @param columnNameType the column name formatting type
   * @return the formatted property name
   * @throws IllegalArgumentException if the column name type is unknown
   */
  public String excludeProp(EntityPropertyType<?, ?> propertyType, ColumnNameType columnNameType) {
    switch (columnNameType) {
      case NAME:
        return propertyType.getColumnName(naming::apply, dialect::applyQuote);
      case NAME_ALIAS:
        return aliasConstants.getExcludedAlias()
            + "."
            + propertyType.getColumnName(naming::apply, dialect::applyQuote);
      default:
        throw new IllegalArgumentException("Unknown table name type: " + columnNameType);
    }
  }

  /**
   * Gets the column name for the specified property type.
   *
   * @param propertyType the entity property type
   * @return the column name
   */
  public String prop(EntityPropertyType<?, ?> propertyType) {
    return propertyType.getColumnName(naming::apply, dialect::applyQuote);
  }

  /**
   * Gets the input parameter for the specified property type from the values map.
   *
   * @param propertyType the entity property type
   * @param values the map of property types to input parameters
   * @return the input parameter for the property type, or null if not found
   */
  public InParameter<?> param(
      EntityPropertyType<?, ?> propertyType, Map<EntityPropertyType<?, ?>, InParameter<?>> values) {
    return values.get(propertyType);
  }
}
