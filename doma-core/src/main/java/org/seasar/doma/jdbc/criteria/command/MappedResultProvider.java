package org.seasar.doma.jdbc.criteria.command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import org.seasar.doma.internal.jdbc.command.AbstractObjectProvider;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.jdbc.query.Query;

public class MappedResultProvider<RESULT> extends AbstractObjectProvider<RESULT> {
  private final FetchSupport fetchSupport;
  private final Function<DataRow, RESULT> mapper;

  public MappedResultProvider(Query query, Function<DataRow, RESULT> mapper) {
    Objects.requireNonNull(query);
    this.fetchSupport = new FetchSupport(query);
    this.mapper = Objects.requireNonNull(mapper);
  }

  @Override
  @SuppressWarnings("unchecked")
  public RESULT get(ResultSet resultSet) throws SQLException {
    DataRow dataRow =
        new DataRow() {
          int index = 1;

          @Override
          public <ENTITY> ENTITY get(EntityMetamodel<ENTITY> entityMetamodel) {
            List<PropertyMetamodel<?>> propertyMetamodels = entityMetamodel.allPropertyMetamodels();
            Map<String, Property<ENTITY, ?>> states = new HashMap<>(propertyMetamodels.size());
            List<Object> rawValues = new ArrayList<>(propertyMetamodels.size());
            for (PropertyMetamodel<?> propertyMetamodel : propertyMetamodels) {
              EntityPropertyType<?, ?> propertyType = propertyMetamodel.asType();
              Property<ENTITY, ?> property = (Property<ENTITY, ?>) propertyType.createProperty();
              try {
                Object rawValue = fetchSupport.fetch(resultSet, property, index++);
                rawValues.add(rawValue);
              } catch (SQLException e) {
                throw new UncheckedSQLException(e);
              }
              states.put(propertyType.getName(), property);
            }
            if (rawValues.stream().allMatch(Objects::isNull)) {
              return null;
            }
            EntityType<ENTITY> entityType = entityMetamodel.asType();
            ENTITY entity = entityType.newEntity(states);
            if (!entityType.isImmutable()) {
              entityType.saveCurrentStates(entity);
            }
            return entity;
          }

          @Override
          public <PROPERTY> PROPERTY get(PropertyMetamodel<PROPERTY> propertyMetamodel) {
            Property<?, ?> property = propertyMetamodel.asType().createProperty();
            try {
              fetchSupport.fetch(resultSet, property, index++);
            } catch (SQLException e) {
              throw new UncheckedSQLException(e);
            }
            return (PROPERTY) property.get();
          }
        };
    try {
      return mapper.apply(dataRow);
    } catch (UncheckedSQLException e) {
      throw e.getCause();
    }
  }
}
