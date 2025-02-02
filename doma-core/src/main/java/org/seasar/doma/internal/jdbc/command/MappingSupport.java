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
package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.Constants.ROWNUMBER_COLUMN_NAME;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import org.seasar.doma.jdbc.DuplicateColumnHandler;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.ResultMappingException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.UnknownColumnHandler;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.jdbc.query.Query;
import org.seasar.doma.wrapper.Wrapper;

/**
 * Provides support for handling and mapping database result sets to entity properties. It ensures
 * proper alignment between query results and entity fields, including handling unknown columns,
 * duplicate columns, and unmapped entity properties.
 */
public class MappingSupport {

  private final EntityType<?> entityType;
  private final Query query;
  private final boolean resultMappingEnsured;
  private final UnknownColumnHandler unknownColumnHandler;
  private final DuplicateColumnHandler duplicateColumnHandler;

  public MappingSupport(
      EntityType<?> entityType,
      Query query,
      boolean resultMappingEnsured,
      UnknownColumnHandler unknownColumnHandler,
      DuplicateColumnHandler duplicateColumnHandler) {
    this.entityType = Objects.requireNonNull(entityType);
    this.query = Objects.requireNonNull(query);
    this.resultMappingEnsured = resultMappingEnsured;
    this.unknownColumnHandler = Objects.requireNonNull(unknownColumnHandler);
    this.duplicateColumnHandler = Objects.requireNonNull(duplicateColumnHandler);
  }

  /**
   * Creates a mapping of result set column indices to their corresponding property types. This
   * method processes the column metadata and maps each column index to a {@code PropType}, based on
   * the provided column name to property type mapping.
   *
   * <p>Handles duplicate column names and unknown columns via the respective handlers. Throws an
   * exception if result mapping is enforced and there are unmapped properties.
   *
   * @param resultSetMeta the metadata of the result set
   * @param columnNameMap a map of lower-case column names to their corresponding property types
   * @return a map where keys are result set column indices and values are the corresponding
   *     property types
   * @throws SQLException if a database access error occurs
   */
  public Map<Integer, MappingSupport.PropType> createIndexMap(
      ResultSetMetaData resultSetMeta, Map<String, PropType> columnNameMap) throws SQLException {
    HashMap<Integer, PropType> indexMap = new HashMap<>();
    Set<PropType> unmappedPropertySet =
        resultMappingEnsured ? new HashSet<>(columnNameMap.values()) : new HashSet<>();
    Set<String> seenColumnNames = new HashSet<>();
    int count = resultSetMeta.getColumnCount();
    for (int i = 1; i < count + 1; i++) {
      String columnName = resultSetMeta.getColumnLabel(i);
      String lowerCaseColumnName = columnName.toLowerCase();
      if (!seenColumnNames.add(lowerCaseColumnName)) {
        duplicateColumnHandler.handle(query, lowerCaseColumnName);
      }
      PropType propertyType = columnNameMap.get(lowerCaseColumnName);
      if (propertyType == null) {
        if (ROWNUMBER_COLUMN_NAME.equals(lowerCaseColumnName)) {
          continue;
        }
        unknownColumnHandler.handle(query, entityType, lowerCaseColumnName, columnNameMap);
      } else {
        unmappedPropertySet.remove(propertyType);
        indexMap.put(i, propertyType);
      }
    }
    if (resultMappingEnsured && !unmappedPropertySet.isEmpty()) {
      throwResultMappingException(unmappedPropertySet);
    }
    return indexMap;
  }

  /**
   * Throws a {@link ResultMappingException} when there are unmapped properties during result
   * mapping. This method gathers information about the unmapped properties, expected column names,
   * and relevant SQL details to construct and throw the exception.
   *
   * @param unmappedPropertySet the set of property types that could not be mapped to result set
   *     columns
   */
  private void throwResultMappingException(Set<PropType> unmappedPropertySet) {
    Naming naming = query.getConfig().getNaming();
    int size = unmappedPropertySet.size();
    List<String> unmappedPropertyNames = new ArrayList<>(size);
    List<String> expectedColumnNames = new ArrayList<>(size);
    for (PropType propType : unmappedPropertySet) {
      unmappedPropertyNames.add(propType.name());
      expectedColumnNames.add(propType.columnName(naming::apply));
    }
    Class<?> entityClass;
    Iterator<PropType> iterator = unmappedPropertySet.iterator();
    if (iterator.hasNext()) {
      entityClass = iterator.next().entityType().getEntityClass();
    } else {
      entityClass = this.entityType.getEntityClass();
    }
    Sql<?> sql = query.getSql();
    throw new ResultMappingException(
        query.getConfig().getExceptionSqlLogType(),
        entityClass.getName(),
        unmappedPropertyNames,
        expectedColumnNames,
        sql.getKind(),
        sql.getRawSql(),
        sql.getFormattedSql(),
        sql.getSqlFilePath());
  }

  /**
   * Represents a combination of an entity type, a property type, and the property path for mapping
   * between result set columns and properties in an entity.
   *
   * <p>This record encapsulates the basic metadata required for mapping a specific property of an
   * entity, including the property's name and its corresponding column in the database.
   *
   * @param entityType The metadata of the entity type that this property belongs to.
   * @param propertyType The metadata of the property type including its name and column details.
   * @param propertyPath The path representing the property within the entity structure.
   */
  public record PropType(
      EntityType<?> entityType, EntityPropertyType<?, ?> propertyType, String propertyPath) {
    public String name() {
      return propertyType.getName();
    }

    public String columnName(BiFunction<NamingType, String, String> namingFunction) {
      return propertyType.getColumnName(namingFunction);
    }
  }

  /**
   * Represents a property mapping for an entity during result set processing. This record
   * encapsulates metadata and value related to a specific property, providing utility methods for
   * accessing property-specific details.
   *
   * @param propType The type of the property containing metadata such as entity type, property
   *     path, and property type.
   * @param property The underlying property object providing accessors and modifiers.
   * @param rawValue The raw value associated with this property, as retrieved from the data source.
   */
  public record Prop(PropType propType, Property<Object, ?> property, Object rawValue) {

    public EntityType<?> entityType() {
      return propType.entityType();
    }

    public String propertyPath() {
      return propType.propertyPath();
    }

    public String name() {
      return propType.propertyType().getName();
    }

    public boolean isId() {
      return propType.propertyType().isId();
    }

    public Wrapper<?> wrapper() {
      return property.getWrapper();
    }
  }
}
