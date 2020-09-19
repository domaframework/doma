package org.seasar.doma.jdbc.criteria.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import javax.annotation.Generated;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.jdbc.entity.AbstractEntityType;
import org.seasar.doma.jdbc.entity.AssignedIdPropertyType;
import org.seasar.doma.jdbc.entity.DefaultPropertyType;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.GeneratedIdPropertyType;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.entity.PostUpdateContext;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.jdbc.entity.TenantIdPropertyType;
import org.seasar.doma.jdbc.entity.VersionPropertyType;
import org.seasar.doma.wrapper.IntegerWrapper;
import org.seasar.doma.wrapper.StringWrapper;

@Generated("")
public class _Dept extends AbstractEntityType<Dept> {

  private static final _Dept singleton = new _Dept();

  private final NamingType __namingType = NamingType.SNAKE_UPPER_CASE;

  public final AssignedIdPropertyType<Dept, Integer, Integer> id =
      new AssignedIdPropertyType<>(
          Dept.class,
          () -> new BasicScalar<>(IntegerWrapper::new),
          "id",
          "ID",
          __namingType,
          false);

  public final DefaultPropertyType<Dept, String, String> name =
      new DefaultPropertyType<>(
          Dept.class,
          () -> new BasicScalar<>(StringWrapper::new),
          "name",
          "NAME",
          __namingType,
          true,
          true,
          false);

  private final String __name = "Dept";

  private final String __catalogName = "CATA";

  private final String __schemaName = null;

  private final String __tableName = "";

  private final List<EntityPropertyType<Dept, ?>> __idPropertyTypes;

  private final List<EntityPropertyType<Dept, ?>> __entityPropertyTypes;

  private final Map<String, EntityPropertyType<Dept, ?>> __entityPropertyTypeMap;

  public _Dept() {
    List<EntityPropertyType<Dept, ?>> __idList = new ArrayList<>();
    __idList.add(id);
    __idPropertyTypes = Collections.unmodifiableList(__idList);
    List<EntityPropertyType<Dept, ?>> __list = new ArrayList<>();
    __list.add(id);
    __list.add(name);
    __entityPropertyTypes = Collections.unmodifiableList(__list);
    Map<String, EntityPropertyType<Dept, ?>> __map = new HashMap<>();
    __map.put("id", id);
    __map.put("name", name);
    __entityPropertyTypeMap = Collections.unmodifiableMap(__map);
  }

  @Override
  public boolean isImmutable() {
    return false;
  }

  @Override
  public Dept newEntity(Map<String, Property<Dept, ?>> args) {
    Dept entity = new Dept();
    args.values().forEach(v -> v.save(entity));
    return entity;
  }

  @Override
  public Class<Dept> getEntityClass() {
    return Dept.class;
  }

  @Override
  public String getName() {
    return __name;
  }

  @Override
  public List<EntityPropertyType<Dept, ?>> getEntityPropertyTypes() {
    return __entityPropertyTypes;
  }

  @Override
  public EntityPropertyType<Dept, ?> getEntityPropertyType(String propertyName) {
    return __entityPropertyTypeMap.get(propertyName);
  }

  @Override
  public void saveCurrentStates(Dept __entity) {}

  @Override
  public Dept getOriginalStates(Dept entity) {
    return null;
  }

  @Override
  public GeneratedIdPropertyType<Dept, ?, ?> getGeneratedIdPropertyType() {
    return null;
  }

  @Override
  public VersionPropertyType<Dept, ?, ?> getVersionPropertyType() {
    return null;
  }

  @Override
  public TenantIdPropertyType<Dept, ?, ?> getTenantIdPropertyType() {
    return null;
  }

  @Override
  public List<EntityPropertyType<Dept, ?>> getIdPropertyTypes() {
    return __idPropertyTypes;
  }

  @Override
  public void preInsert(Dept entity, PreInsertContext<Dept> context) {}

  @Override
  public void preUpdate(Dept entity, PreUpdateContext<Dept> context) {}

  @Override
  public void preDelete(Dept entity, PreDeleteContext<Dept> context) {}

  @Override
  public void postInsert(Dept entity, PostInsertContext<Dept> context) {}

  @Override
  public void postUpdate(Dept entity, PostUpdateContext<Dept> context) {}

  @Override
  public void postDelete(Dept entity, PostDeleteContext<Dept> context) {}

  @Override
  public String getCatalogName() {
    return __catalogName;
  }

  @Override
  public String getSchemaName() {
    return __schemaName;
  }

  @Override
  @Deprecated
  public String getTableName() {
    return getTableName((namingType, text) -> namingType.apply(text));
  }

  @Override
  public String getTableName(BiFunction<NamingType, String, String> namingFunction) {
    if (__tableName.isEmpty()) {
      return namingFunction.apply(getNamingType(), getName());
    }
    return __tableName;
  }

  @Override
  public NamingType getNamingType() {
    return __namingType;
  }

  @Override
  public boolean isQuoteRequired() {
    return false;
  }

  public static _Dept getSingletonInternal() {
    return singleton;
  }
}
