package org.seasar.doma.jdbc.criteria.command;

import static java.util.stream.Collectors.toList;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.jdbc.command.JdbcValueGetter;
import org.seasar.doma.jdbc.JdbcMappable;
import org.seasar.doma.jdbc.JdbcMappingFunction;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.criteria.def.EntityDef;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.jdbc.query.Query;
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.wrapper.Wrapper;

public class EntityPoolProvider implements ObjectProvider<EntityPool> {

  private final List<EntityDef<?>> entityDefs;

  private final JdbcMappingVisitor jdbcMappingVisitor;

  public EntityPoolProvider(List<EntityDef<?>> entityDefs, Query query) {
    Objects.requireNonNull(entityDefs);
    Objects.requireNonNull(query);
    this.entityDefs = entityDefs;
    this.jdbcMappingVisitor = query.getConfig().getDialect().getJdbcMappingVisitor();
  }

  @Override
  @SuppressWarnings("unchecked")
  public EntityPool get(ResultSet resultSet) throws SQLException {
    Objects.requireNonNull(resultSet);
    EntityPool entityPool = new EntityPool();
    int index = 1;
    for (EntityDef<?> entityDef : entityDefs) {
      EntityType<?> entityType = entityDef.asType();
      List<? extends EntityPropertyType<?, ?>> propertyTypes = entityType.getEntityPropertyTypes();
      List<Prop> props = new ArrayList<>(propertyTypes.size());
      for (EntityPropertyType<?, ?> propertyType : propertyTypes) {
        Property<Object, ?> property = (Property<Object, ?>) propertyType.createProperty();
        Object rawValue = fetch(resultSet, property, index++);
        props.add(new Prop(propertyType, property, rawValue));
      }
      if (props.stream().allMatch(p -> p.rawValue == null)) {
        continue;
      }
      EntityKey key;
      if (entityType.getIdPropertyTypes().isEmpty()) {
        key = new EntityKey(entityDef, Collections.singletonList(new Object()));
      } else {
        List<?> items =
            props.stream()
                .filter(it -> it.propType.isId())
                .map(it -> it.prop.getWrapper().get())
                .collect(toList());
        key = new EntityKey(entityDef, items);
      }
      Map<String, Property<Object, ?>> states =
          props.stream().collect(Collectors.toMap(p -> p.propType.getName(), p -> p.prop));
      EntityData data = new EntityData(states);
      entityPool.put(key, data);
    }
    return entityPool;
  }

  private Object fetch(ResultSet resultSet, JdbcMappable<?> mappable, int index)
      throws SQLException {
    Wrapper<?> wrapper = mappable.getWrapper();
    JdbcValueGetterProxy proxy = new JdbcValueGetterProxy(new JdbcValueGetter(resultSet, index));
    wrapper.accept(jdbcMappingVisitor, proxy, mappable);
    return proxy.rawValue;
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

  private static class JdbcValueGetterProxy implements JdbcMappingFunction {
    private final JdbcValueGetter jdbcValueGetter;
    Object rawValue;

    JdbcValueGetterProxy(JdbcValueGetter jdbcValueGetter) {
      Objects.requireNonNull(jdbcValueGetter);
      this.jdbcValueGetter = jdbcValueGetter;
    }

    @Override
    public <R, V> R apply(Wrapper<V> wrapper, JdbcType<V> jdbcType) throws SQLException {
      JdbcTypeProxy<V> proxy = new JdbcTypeProxy<>(jdbcType);
      R result = jdbcValueGetter.apply(wrapper, proxy);
      rawValue = proxy.rawValue;
      return result;
    }
  }

  private static class JdbcTypeProxy<T> implements JdbcType<T> {
    private final JdbcType<T> jdbcType;
    T rawValue;

    JdbcTypeProxy(JdbcType<T> jdbcType) {
      Objects.requireNonNull(jdbcType);
      this.jdbcType = jdbcType;
    }

    @Override
    public T getValue(ResultSet resultSet, int index)
        throws DomaNullPointerException, SQLException {
      T value = jdbcType.getValue(resultSet, index);
      rawValue = value;
      return value;
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, T value)
        throws SQLException {
      jdbcType.setValue(preparedStatement, index, value);
    }

    @Override
    public void registerOutParameter(CallableStatement callableStatement, int index)
        throws SQLException {
      jdbcType.registerOutParameter(callableStatement, index);
    }

    @Override
    public T getValue(CallableStatement callableStatement, int index) throws SQLException {
      return jdbcType.getValue(callableStatement, index);
    }

    @Override
    public String convertToLogFormat(T value) {
      return jdbcType.convertToLogFormat(value);
    }
  }
}
