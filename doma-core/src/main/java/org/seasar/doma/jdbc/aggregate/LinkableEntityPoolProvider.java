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
package org.seasar.doma.jdbc.aggregate;

import static java.util.stream.Collectors.toList;
import static org.seasar.doma.internal.Constants.ROWNUMBER_COLUMN_NAME;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.seasar.doma.internal.jdbc.command.AbstractObjectProvider;
import org.seasar.doma.internal.jdbc.command.FetchSupport;
import org.seasar.doma.jdbc.DuplicateColumnHandler;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.ResultMappingException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.UnknownColumnHandler;
import org.seasar.doma.jdbc.entity.AssociationPropertyType;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.jdbc.query.Query;
import org.seasar.doma.wrapper.Wrapper;

public class LinkableEntityPoolProvider extends AbstractObjectProvider<LinkableEntityPool> {

  private final EntityType<?> entityType;

  private final Query query;

  private final boolean resultMappingEnsured;

  private final Map<LinkableEntityKey, Object> cache;

  private final UnknownColumnHandler unknownColumnHandler;

  private final DuplicateColumnHandler duplicateColumnHandler;

  private Map<Integer, PropType> indexMap;

  private final FetchSupport fetchSupport;

  private final AggregateStrategyType aggregateStrategyType;

  private final Map<String, AssociationLinkerType<?, ?>> associationLinkerTypeMap;

  public LinkableEntityPoolProvider(
      EntityType<?> entityType,
      AggregateStrategyType aggregateStrategyType,
      Query query,
      boolean resultMappingEnsured,
      Map<LinkableEntityKey, Object> cache) {
    assertNotNull(entityType, aggregateStrategyType, query, cache);
    this.entityType = entityType;
    this.query = query;
    this.resultMappingEnsured = resultMappingEnsured;
    this.cache = cache;
    this.unknownColumnHandler = query.getConfig().getUnknownColumnHandler();
    this.duplicateColumnHandler = query.getConfig().getDuplicateColumnHandler();
    this.fetchSupport = new FetchSupport(query);
    this.aggregateStrategyType = aggregateStrategyType;
    this.associationLinkerTypeMap =
        aggregateStrategyType.getAssociationLinkerTypes().stream()
            .collect(Collectors.toMap(AssociationLinkerType::getPropertyPath, Function.identity()));
  }

  @Override
  public LinkableEntityPool get(ResultSet resultSet) throws SQLException {
    List<Prop> props = cratesProps(resultSet);
    Map<AssociationIdentifier, List<Prop>> propGroup = groupPropsByAssociationIdentifier(props);
    return createEntityPool(propGroup);
  }

  private List<Prop> cratesProps(ResultSet resultSet) throws SQLException {
    assertNotNull(resultSet);
    if (indexMap == null) {
      indexMap = createIndexMap(resultSet.getMetaData());
    }
    List<Prop> props = new ArrayList<>(indexMap.size());
    for (Map.Entry<Integer, PropType> entry : indexMap.entrySet()) {
      Integer index = entry.getKey();
      PropType propType = entry.getValue();
      EntityPropertyType<?, ?> propertyType = propType.propertyType();
      @SuppressWarnings("unchecked")
      Property<Object, ?> property = (Property<Object, ?>) propertyType.createProperty();
      Object rawValue = fetchSupport.fetch(resultSet, property, index);
      props.add(new Prop(propType, property, rawValue));
    }
    return props;
  }

  private HashMap<Integer, PropType> createIndexMap(ResultSetMetaData resultSetMeta)
      throws SQLException {
    HashMap<Integer, PropType> indexMap = new HashMap<>();
    HashMap<String, PropType> columnNameMap = createColumnNameMap();
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

  private HashMap<String, PropType> createColumnNameMap() {
    List<? extends EntityPropertyType<?, ?>> propertyTypes = entityType.getEntityPropertyTypes();
    HashMap<String, PropType> result = new HashMap<>(propertyTypes.size());
    collectColumnNames(result, entityType, "", aggregateStrategyType.getTableAlias());
    return result;
  }

  private void collectColumnNames(
      Map<String, PropType> map, EntityType<?> source, String propertyPath, String tableAlias) {
    Naming naming = query.getConfig().getNaming();
    String prefix = tableAlias + "_";
    for (EntityPropertyType<?, ?> propertyType : source.getEntityPropertyTypes()) {
      String columnName = propertyType.getColumnName(naming::apply);
      map.put(prefix + columnName.toLowerCase(), new PropType(source, propertyType, propertyPath));
    }
    String propertyPrefix = propertyPath.isEmpty() ? "" : propertyPath + ".";
    for (AssociationPropertyType associationPropertyType : source.getAssociationPropertyTypes()) {
      String path = propertyPrefix + associationPropertyType.getName();
      AssociationLinkerType<?, ?> associationLinkerType = associationLinkerTypeMap.get(path);
      if (associationLinkerType != null && associationLinkerType.getSource() == source) {
        collectColumnNames(
            map,
            associationLinkerType.getTarget(),
            associationLinkerType.getTargetName(),
            associationLinkerType.getTableAlias());
      }
    }
  }

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

  private Map<AssociationIdentifier, List<Prop>> groupPropsByAssociationIdentifier(
      List<Prop> props) {
    return props.stream()
        .collect(
            Collectors.groupingBy(
                it -> new AssociationIdentifier(it.propertyPath(), it.entityType())));
  }

  private LinkableEntityPool createEntityPool(Map<AssociationIdentifier, List<Prop>> propGroup) {
    LinkableEntityPool entityPool = new LinkableEntityPool();

    for (Map.Entry<AssociationIdentifier, List<Prop>> entry : propGroup.entrySet()) {
      AssociationIdentifier identifier = entry.getKey();
      List<Prop> props = entry.getValue();
      if (props.stream().allMatch(p -> p.rawValue == null)) {
        continue;
      }
      LinkableEntityKey entityKey;
      if (entityType.getIdPropertyTypes().isEmpty()) {
        entityKey = new LinkableEntityKey(identifier, Collections.singletonList(new Object()));
      } else {
        List<?> items =
            props.stream().filter(Prop::isId).map(it -> it.wrapper().get()).collect(toList());
        entityKey = new LinkableEntityKey(identifier, items);
      }
      Object entity =
          cache.computeIfAbsent(
              entityKey,
              k -> {
                @SuppressWarnings("unchecked")
                EntityType<Object> entityType = (EntityType<Object>) k.entityType();
                Map<String, Property<Object, ?>> states =
                    props.stream().collect(Collectors.toMap(Prop::name, Prop::property));
                Object newEntity = entityType.newEntity(states);
                if (!entityType.isImmutable()) {
                  entityType.saveCurrentStates(newEntity);
                }
                return newEntity;
              });
      entityPool.add(new LinkableEntity(entityKey, entity));
    }

    return entityPool;
  }

  record PropType(
      EntityType<?> entityType, EntityPropertyType<?, ?> propertyType, String propertyPath) {
    public String name() {
      return propertyType.getName();
    }

    public String columnName(BiFunction<NamingType, String, String> namingFunction) {
      return propertyType.getColumnName(namingFunction);
    }
  }

  record Prop(PropType propType, Property<Object, ?> property, Object rawValue) {

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
