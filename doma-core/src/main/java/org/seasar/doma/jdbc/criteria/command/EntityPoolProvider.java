package org.seasar.doma.jdbc.criteria.command;

import static java.util.stream.Collectors.toList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.jdbc.query.Query;

public class EntityPoolProvider implements ObjectProvider<EntityPool> {
  private final List<EntityMetamodel<?>> entityMetamodels;
  private final FetchSupport fetchSupport;

  public EntityPoolProvider(List<EntityMetamodel<?>> entityMetamodels, Query query) {
    this.entityMetamodels = Objects.requireNonNull(entityMetamodels);
    Objects.requireNonNull(query);
    this.fetchSupport = new FetchSupport(query);
  }

  @Override
  @SuppressWarnings("unchecked")
  public EntityPool get(ResultSet resultSet) throws SQLException {
    Objects.requireNonNull(resultSet);
    EntityPool entityPool = new EntityPool();
    int index = 1;
    for (EntityMetamodel<?> entityMetamodel : entityMetamodels) {
      EntityType<?> entityType = entityMetamodel.asType();
      List<? extends EntityPropertyType<?, ?>> propertyTypes = entityType.getEntityPropertyTypes();
      List<Prop> props = new ArrayList<>(propertyTypes.size());
      for (EntityPropertyType<?, ?> propertyType : propertyTypes) {
        Property<Object, ?> property = (Property<Object, ?>) propertyType.createProperty();
        Object rawValue = fetchSupport.fetch(resultSet, property, index++);
        props.add(new Prop(propertyType, property, rawValue));
      }
      if (props.stream().allMatch(p -> p.rawValue == null)) {
        continue;
      }
      EntityKey key;
      if (entityType.getIdPropertyTypes().isEmpty()) {
        key = new EntityKey(entityMetamodel, Collections.singletonList(new Object()));
      } else {
        List<?> items =
            props.stream()
                .filter(it -> it.propType.isId())
                .map(it -> it.prop.getWrapper().get())
                .collect(toList());
        key = new EntityKey(entityMetamodel, items);
      }
      Map<String, Property<Object, ?>> states =
          props.stream().collect(Collectors.toMap(p -> p.propType.getName(), p -> p.prop));
      EntityData data = new EntityData(states);
      entityPool.put(key, data);
    }
    return entityPool;
  }

  private static class Prop {
    private final EntityPropertyType<?, ?> propType;
    private final Property<Object, ?> prop;
    private final Object rawValue;

    public Prop(EntityPropertyType<?, ?> propType, Property<Object, ?> prop, Object rawValue) {
      this.propType = propType;
      this.prop = prop;
      this.rawValue = rawValue;
    }
  }
}
