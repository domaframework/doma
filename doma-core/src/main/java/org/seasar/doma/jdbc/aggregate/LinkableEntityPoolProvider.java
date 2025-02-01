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
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.seasar.doma.internal.jdbc.command.AbstractObjectProvider;
import org.seasar.doma.internal.jdbc.command.FetchSupport;
import org.seasar.doma.internal.jdbc.command.MappingSupport;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.entity.AssociationPropertyType;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.jdbc.query.Query;

/**
 * The LinkableEntityPoolProvider class is responsible for creating and supplying instances of
 * LinkableEntityPool. It processes database query results, maps them to entity properties, manages
 * caching to avoid duplicate instantiation, and handles linking of associated entities. This
 * provider is configured with essential details such as the entity type, query, aggregation
 * strategy, and mapping and fetching mechanisms.
 *
 * <p>It ensures efficient and reusable entity instantiation through its caching mechanism and
 * provides a robust support for mapping result set columns to entity properties, even in cases
 * involving complex associations and aggregation strategies.
 */
public class LinkableEntityPoolProvider extends AbstractObjectProvider<LinkableEntityPool> {

  private final EntityType<?> entityType;
  private final Query query;
  private final Map<LinkableEntityKey, Object> cache;
  private Map<Integer, MappingSupport.PropType> indexMap;
  private final MappingSupport mappingSupport;
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
    this.cache = cache;
    this.mappingSupport =
        new MappingSupport(
            entityType,
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
  public LinkableEntityPool get(ResultSet resultSet) throws SQLException {
    List<MappingSupport.Prop> props = cratesProps(resultSet);
    Map<AssociationIdentifier, List<MappingSupport.Prop>> propGroup =
        groupPropsByAssociationIdentifier(props);
    return createEntityPool(propGroup);
  }

  /**
   * Processes the given ResultSet and constructs a list of MappingSupport.Prop objects. Each Prop
   * object is created by extracting the raw value from the ResultSet using the associated property
   * type and its index, as defined by the indexMap. If the indexMap is null, it initializes it by
   * creating a column name mapping and determining the appropriate indexes based on the ResultSet's
   * metadata.
   *
   * @param resultSet the ResultSet from which property values are extracted, must not be null
   * @return a list of MappingSupport.Prop objects, each representing a property with its extracted
   *     value
   * @throws SQLException if a database access error occurs
   */
  private List<MappingSupport.Prop> cratesProps(ResultSet resultSet) throws SQLException {
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

  /**
   * Creates a column name mapping for the entity type associated with this provider. This method
   * retrieves the entity property types and populates a map where column names are keys and their
   * corresponding property types are values. It also ensures that the mapping includes appropriate
   * table aliases for column names to avoid conflicts.
   *
   * @return a map where the keys are column names (with applied table aliases) and the values are
   *     corresponding {@link MappingSupport.PropType} objects
   */
  private Map<String, MappingSupport.PropType> createColumnNameMap() {
    List<? extends EntityPropertyType<?, ?>> propertyTypes = entityType.getEntityPropertyTypes();
    Map<String, MappingSupport.PropType> result = new HashMap<>(propertyTypes.size());
    collectColumnNames(result, entityType, "", aggregateStrategyType.getTableAlias());
    return result;
  }

  /**
   * Collects and maps column names and their associated property types based on the provided entity
   * type and its properties. This method recursively processes entity properties and association
   * properties to populate the given map with column names and corresponding property type
   * information.
   *
   * @param map a map where column names (in a prefixed, lowercase format) are mapped to their
   *     corresponding {@link MappingSupport.PropType} values
   * @param source the {@link EntityType} representing the source entity for which column names and
   *     property types are being collected
   * @param propertyPath the property path used to identify the hierarchy of properties, allowing
   *     proper association handling
   * @param tableAlias the table alias used as a prefix for column names to ensure uniqueness across
   *     multiple joined tables
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
            associationLinkerType.getTargetName(),
            associationLinkerType.getTableAlias());
      }
    }
  }

  /**
   * Groups a list of {@link MappingSupport.Prop} objects by their associated {@link
   * AssociationIdentifier}. The grouping is determined based on the combination of the property
   * path and entity type for each property.
   *
   * @param props a list of {@link MappingSupport.Prop} objects representing entity properties to be
   *     grouped
   * @return a map where the keys are {@link AssociationIdentifier} objects and the values are lists
   *     of {@link MappingSupport.Prop} objects grouped by their association identifier
   */
  private Map<AssociationIdentifier, List<MappingSupport.Prop>> groupPropsByAssociationIdentifier(
      List<MappingSupport.Prop> props) {
    return props.stream()
        .collect(
            Collectors.groupingBy(
                it -> new AssociationIdentifier(it.propertyPath(), it.entityType())));
  }

  /**
   * Creates and populates a {@link LinkableEntityPool} based on the provided group of properties
   * organized by their {@link AssociationIdentifier}. The method processes the given properties,
   * checks their validity, determines the appropriate keys and states, and associates the created
   * entities with a unique key in the pool. It ensures caching and state preservation for entities.
   *
   * @param propGroup a map where keys are {@link AssociationIdentifier} objects representing the
   *     association identifiers and values are lists of {@link MappingSupport.Prop}, which define
   *     properties for the entities within that association.
   * @return a {@link LinkableEntityPool} containing the processed and created entities organized by
   *     their associations.
   */
  private LinkableEntityPool createEntityPool(
      Map<AssociationIdentifier, List<MappingSupport.Prop>> propGroup) {
    LinkableEntityPool entityPool = new LinkableEntityPool();

    for (Map.Entry<AssociationIdentifier, List<MappingSupport.Prop>> entry : propGroup.entrySet()) {
      AssociationIdentifier identifier = entry.getKey();
      List<MappingSupport.Prop> props = entry.getValue();
      if (props.stream().allMatch(p -> p.rawValue() == null)) {
        continue;
      }
      LinkableEntityKey entityKey;
      if (entityType.getIdPropertyTypes().isEmpty()) {
        entityKey = new LinkableEntityKey(identifier, Collections.singletonList(new Object()));
      } else {
        List<?> items =
            props.stream()
                .filter(MappingSupport.Prop::isId)
                .map(it -> it.wrapper().get())
                .collect(toList());
        entityKey = new LinkableEntityKey(identifier, items);
      }
      Object entity =
          cache.computeIfAbsent(
              entityKey,
              k -> {
                @SuppressWarnings("unchecked")
                EntityType<Object> entityType = (EntityType<Object>) k.entityType();
                Map<String, Property<Object, ?>> states =
                    props.stream()
                        .collect(
                            Collectors.toMap(
                                MappingSupport.Prop::name, MappingSupport.Prop::property));
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
}
