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
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.seasar.doma.jdbc.DuplicateColumnHandler;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.ResultMappingException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.UnknownColumnHandler;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.jdbc.query.Query;

public class EntityProvider<ENTITY> extends AbstractObjectProvider<ENTITY> {

  protected final EntityType<ENTITY> entityType;

  protected final Query query;

  protected final boolean resultMappingEnsured;

  protected final JdbcMappingVisitor jdbcMappingVisitor;

  protected final UnknownColumnHandler unknownColumnHandler;

  protected final DuplicateColumnHandler duplicateColumnHandler;

  private final MappingSupport mappingSupport;

  protected Map<Integer, EntityPropertyType<ENTITY, ?>> indexMap;

  public EntityProvider(EntityType<ENTITY> entityType, Query query, boolean resultMappingEnsured) {
    assertNotNull(entityType, query);
    this.entityType = entityType;
    this.query = query;
    this.resultMappingEnsured = resultMappingEnsured;
    this.jdbcMappingVisitor = query.getConfig().getDialect().getJdbcMappingVisitor();
    this.unknownColumnHandler = query.getConfig().getUnknownColumnHandler();
    this.duplicateColumnHandler = query.getConfig().getDuplicateColumnHandler();
    this.mappingSupport =
        new MappingSupport(
            entityType, query, resultMappingEnsured, unknownColumnHandler, duplicateColumnHandler);
  }

  @Override
  public ENTITY get(ResultSet resultSet) throws SQLException {
    return build(resultSet);
  }

  protected ENTITY build(ResultSet resultSet) throws SQLException {
    assertNotNull(resultSet);
    if (indexMap == null) {
      indexMap = createIndexMap(resultSet.getMetaData());
    }
    Map<String, Property<ENTITY, ?>> states = new HashMap<>(indexMap.size());
    for (Map.Entry<Integer, EntityPropertyType<ENTITY, ?>> entry : indexMap.entrySet()) {
      Integer index = entry.getKey();
      EntityPropertyType<ENTITY, ?> propertyType = entry.getValue();
      Property<ENTITY, ?> property = propertyType.createProperty();
      fetch(resultSet, property, index, jdbcMappingVisitor);
      states.put(propertyType.getName(), property);
    }
    ENTITY entity = entityType.newEntity(states);
    if (!entityType.isImmutable()) {
      entityType.saveCurrentStates(entity);
    }
    return entity;
  }

  @SuppressWarnings("removal")
  @Deprecated(forRemoval = true)
  protected HashMap<Integer, EntityPropertyType<ENTITY, ?>> createIndexMap(
      ResultSetMetaData resultSetMeta, EntityType<ENTITY> entityType) throws SQLException {
    HashMap<Integer, EntityPropertyType<ENTITY, ?>> indexMap = new HashMap<>();
    HashMap<String, EntityPropertyType<ENTITY, ?>> columnNameMap = createColumnNameMap(entityType);
    Set<EntityPropertyType<ENTITY, ?>> unmappedPropertySet =
        resultMappingEnsured ? new HashSet<>(columnNameMap.values()) : new HashSet<>();
    Set<String> seenColumnNames = new HashSet<>();
    int count = resultSetMeta.getColumnCount();
    for (int i = 1; i < count + 1; i++) {
      String columnName = resultSetMeta.getColumnLabel(i);
      String lowerCaseColumnName = columnName.toLowerCase();
      if (!seenColumnNames.add(lowerCaseColumnName)) {
        duplicateColumnHandler.handle(query, lowerCaseColumnName);
      }
      EntityPropertyType<ENTITY, ?> propertyType = columnNameMap.get(lowerCaseColumnName);
      if (propertyType == null) {
        if (ROWNUMBER_COLUMN_NAME.equals(lowerCaseColumnName)) {
          continue;
        }
        unknownColumnHandler.handle(query, entityType, lowerCaseColumnName);
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

  @SuppressWarnings("unchecked")
  private Map<Integer, EntityPropertyType<ENTITY, ?>> createIndexMap(
      ResultSetMetaData resultSetMeta) throws SQLException {
    Map<String, MappingSupport.PropType> columnNameMap = createColumnNameMap();
    return indexMap =
        mappingSupport.createIndexMap(resultSetMeta, columnNameMap).entrySet().stream()
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey,
                    e -> (EntityPropertyType<ENTITY, ?>) e.getValue().propertyType()));
  }

  @Deprecated
  protected HashMap<String, EntityPropertyType<ENTITY, ?>> createColumnNameMap(
      EntityType<ENTITY> entityType) {
    Naming naming = query.getConfig().getNaming();
    List<EntityPropertyType<ENTITY, ?>> propertyTypes = entityType.getEntityPropertyTypes();
    HashMap<String, EntityPropertyType<ENTITY, ?>> result = new HashMap<>(propertyTypes.size());
    for (EntityPropertyType<ENTITY, ?> propertyType : propertyTypes) {
      String columnName = propertyType.getColumnName(naming::apply);
      result.put(columnName.toLowerCase(), propertyType);
    }
    return result;
  }

  private HashMap<String, MappingSupport.PropType> createColumnNameMap() {
    Naming naming = query.getConfig().getNaming();
    List<EntityPropertyType<ENTITY, ?>> propertyTypes = entityType.getEntityPropertyTypes();
    HashMap<String, MappingSupport.PropType> result = new HashMap<>(propertyTypes.size());
    for (EntityPropertyType<ENTITY, ?> propertyType : propertyTypes) {
      String columnName = propertyType.getColumnName(naming::apply);
      result.put(
          columnName.toLowerCase(), new MappingSupport.PropType(entityType, propertyType, ""));
    }
    return result;
  }

  @Deprecated(forRemoval = true)
  protected void throwResultMappingException(
      Set<EntityPropertyType<ENTITY, ?>> unmappedPropertySet) {
    Naming naming = query.getConfig().getNaming();
    int size = unmappedPropertySet.size();
    List<String> unmappedPropertyNames = new ArrayList<>(size);
    List<String> expectedColumnNames = new ArrayList<>(size);
    for (EntityPropertyType<ENTITY, ?> propertyType : unmappedPropertySet) {
      unmappedPropertyNames.add(propertyType.getName());
      expectedColumnNames.add(propertyType.getColumnName(naming::apply));
    }
    Sql<?> sql = query.getSql();
    throw new ResultMappingException(
        query.getConfig().getExceptionSqlLogType(),
        entityType.getEntityClass().getName(),
        unmappedPropertyNames,
        expectedColumnNames,
        sql.getKind(),
        sql.getRawSql(),
        sql.getFormattedSql(),
        sql.getSqlFilePath());
  }
}
