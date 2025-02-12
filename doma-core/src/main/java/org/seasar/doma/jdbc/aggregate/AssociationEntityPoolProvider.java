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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.seasar.doma.internal.jdbc.command.AbstractObjectProvider;
import org.seasar.doma.internal.jdbc.command.FetchSupport;
import org.seasar.doma.internal.jdbc.command.MappingSupport;
import org.seasar.doma.jdbc.EntityId;
import org.seasar.doma.jdbc.EntityRef;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.entity.AssociationPropertyType;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.jdbc.query.Query;

public class AssociationEntityPoolProvider extends AbstractObjectProvider<AssociationEntityPool> {

  private final EntityType<?> rootEntityType;
  private final Query query;
  private final Set<EntityId> rootEntityIds;
  private final Map<EntityId, EntityRef> entityCache;
  private Map<Integer, MappingSupport.PropType> indexMap;
  private final MappingSupport mappingSupport;
  private final FetchSupport fetchSupport;
  private final AggregateStrategyType aggregateStrategyType;
  private final Map<String, AssociationLinkerType<?, ?>> associationLinkerTypeMap;

  public AssociationEntityPoolProvider(
      EntityType<?> rootEntityType,
      AggregateStrategyType aggregateStrategyType,
      Query query,
      boolean resultMappingEnsured,
      Set<EntityId> rootEntityIds,
      Map<EntityId, EntityRef> entityCache) {
    assertNotNull(rootEntityType, aggregateStrategyType, query, rootEntityIds, entityCache);
    this.rootEntityType = rootEntityType;
    this.query = query;
    this.rootEntityIds = rootEntityIds;
    this.entityCache = entityCache;
    this.mappingSupport =
        new MappingSupport(
            rootEntityType,
            query,
            resultMappingEnsured,
            query.getConfig().getUnknownColumnHandler(),
            query.getConfig().getDuplicateColumnHandler());
    this.fetchSupport = new FetchSupport(query);
    this.aggregateStrategyType = aggregateStrategyType;
    this.associationLinkerTypeMap =
        aggregateStrategyType.getAssociationLinkerTypes().stream()
            .collect(Collectors.toMap(AssociationLinkerType::getPropertyPath, Function.identity()));
  }

  @Override
  public AssociationEntityPool get(ResultSet resultSet) throws SQLException {
    List<MappingSupport.Prop> props = createProps(resultSet);
    Map<AssociationPathKey, List<MappingSupport.Prop>> propGroup = groupPropsByPathKey(props);
    return createEntityPool(propGroup);
  }

  /** Processes the given ResultSet and constructs a list of MappingSupport.Prop objects. */
  private List<MappingSupport.Prop> createProps(ResultSet resultSet) throws SQLException {
    assertNotNull(resultSet);
    if (indexMap == null) {
      Map<String, MappingSupport.PropType> columnNameMap = createColumnNameMap();
      indexMap = mappingSupport.createIndexMap(resultSet.getMetaData(), columnNameMap);
    }
    List<MappingSupport.Prop> props = new ArrayList<>(indexMap.size());
    for (Map.Entry<Integer, MappingSupport.PropType> entry : indexMap.entrySet()) {
      Integer index = entry.getKey();
      MappingSupport.PropType propType = entry.getValue();
      EntityPropertyType<?, ?> propertyType = propType.propertyType();
      @SuppressWarnings("unchecked")
      Property<Object, ?> property = (Property<Object, ?>) propertyType.createProperty();
      Object rawValue = fetchSupport.fetch(resultSet, property, index);
      props.add(new MappingSupport.Prop(propType, property, rawValue));
    }
    return props;
  }

  /** Creates a column name mapping for the entity type associated with this provider. */
  private Map<String, MappingSupport.PropType> createColumnNameMap() {
    List<? extends EntityPropertyType<?, ?>> propertyTypes =
        rootEntityType.getEntityPropertyTypes();
    Map<String, MappingSupport.PropType> result = new HashMap<>(propertyTypes.size());
    collectColumnNames(result, rootEntityType, "", aggregateStrategyType.getTableAlias());
    return result;
  }

  /**
   * Collects and maps column names and their associated property types based on the provided entity
   * type and its properties. This method recursively processes entity properties and association
   * properties to populate the given map with column names and corresponding property type
   * information.
   */
  private void collectColumnNames(
      Map<String, MappingSupport.PropType> map,
      EntityType<?> source,
      String propertyPath,
      String tableAlias) {
    Naming naming = query.getConfig().getNaming();
    String prefix = tableAlias + "_";
    for (EntityPropertyType<?, ?> propertyType : source.getEntityPropertyTypes()) {
      String columnName = propertyType.getColumnName(naming::apply);
      map.put(
          prefix + columnName.toLowerCase(),
          new MappingSupport.PropType(source, propertyType, propertyPath));
    }
    String propertyPrefix = propertyPath.isEmpty() ? "" : propertyPath + ".";
    for (AssociationPropertyType associationPropertyType : source.getAssociationPropertyTypes()) {
      String path = propertyPrefix + associationPropertyType.getName();
      AssociationLinkerType<?, ?> associationLinkerType = associationLinkerTypeMap.get(path);
      if (associationLinkerType != null && associationLinkerType.getSource() == source) {
        collectColumnNames(
            map,
            associationLinkerType.getTarget(),
            associationLinkerType.getPropertyPath(),
            associationLinkerType.getTableAlias());
      }
    }
  }

  private Map<AssociationPathKey, List<MappingSupport.Prop>> groupPropsByPathKey(
      List<MappingSupport.Prop> props) {
    return props.stream()
        .collect(
            Collectors.groupingBy(
                it -> new AssociationPathKey(it.propertyPath(), it.entityType())));
  }

  /**
   * Creates and populates a {@link AssociationEntityPool} based on the provided group of properties
   * organized by their {@link AssociationPathKey}.
   */
  private AssociationEntityPool createEntityPool(
      Map<AssociationPathKey, List<MappingSupport.Prop>> propGroup) {
    AssociationEntityPool entityPool = new AssociationEntityPool();

    for (Map.Entry<AssociationPathKey, List<MappingSupport.Prop>> entry : propGroup.entrySet()) {
      AssociationPathKey pathKey = entry.getKey();
      List<MappingSupport.Prop> props = entry.getValue();
      if (props.stream().allMatch(p -> p.rawValue() == null)) {
        continue;
      }
      AssociationEntityKey entityKey = createEntityKey(pathKey, props);
      EntityId entityId = entityKey.entityId();
      EntityRef entityRef = entityCache.computeIfAbsent(entityId, id -> createEntityRef(id, props));
      entityPool.add(new AssociationEntityPoolEntry(entityKey, entityRef));
      if (pathKey.isRoot()) {
        rootEntityIds.add(entityId);
      }
    }

    return entityPool;
  }

  private static AssociationEntityKey createEntityKey(
      AssociationPathKey pathKey, List<MappingSupport.Prop> props) {
    AssociationEntityKey entityKey;
    if (pathKey.entityType().getIdPropertyTypes().isEmpty()) {
      entityKey = new AssociationEntityKey(pathKey, Collections.singletonList(new Object()));
    } else {
      List<?> items =
          props.stream().filter(MappingSupport.Prop::isId).map(it -> it.wrapper().get()).toList();
      entityKey = new AssociationEntityKey(pathKey, items);
    }
    return entityKey;
  }

  private static EntityRef createEntityRef(EntityId entityId, List<MappingSupport.Prop> props) {
    @SuppressWarnings("unchecked")
    EntityType<Object> entityType = (EntityType<Object>) entityId.entityType();
    Map<String, Property<Object, ?>> states =
        props.stream()
            .collect(Collectors.toMap(MappingSupport.Prop::name, MappingSupport.Prop::property));
    Object entity = entityType.newEntity(states);
    if (!entityType.isImmutable()) {
      entityType.saveCurrentStates(entity);
    }
    return new EntityRef(entity);
  }
}
