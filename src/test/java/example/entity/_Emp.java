/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package example.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import javax.annotation.Generated;
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

@Generated("")
public class _Emp extends AbstractEntityType<Emp> {

  private static _Emp singleton = new _Emp();

  private static final org.seasar.doma.jdbc.entity.OriginalStatesAccessor<Emp>
      __originalStatesAccessor =
          new org.seasar.doma.jdbc.entity.OriginalStatesAccessor<>(Emp.class, "originalStates");

  private final NamingType __namingType = NamingType.UPPER_CASE;

  public final AssignedIdPropertyType<Object, Emp, Integer, Object> id =
      new AssignedIdPropertyType<>(
          Emp.class,
          Integer.class,
          Integer.class,
          () -> new org.seasar.doma.wrapper.IntegerWrapper(),
          null,
          null,
          "id",
          "ID",
          __namingType,
          false);

  public final DefaultPropertyType<Object, Emp, String, Object> name =
      new DefaultPropertyType<>(
          Emp.class,
          String.class,
          String.class,
          () -> new org.seasar.doma.wrapper.StringWrapper(),
          null,
          null,
          "name",
          "NAME",
          __namingType,
          true,
          true,
          false);

  public final DefaultPropertyType<Object, Emp, BigDecimal, BigDecimal> salary =
      new DefaultPropertyType<>(
          Emp.class,
          BigDecimal.class,
          BigDecimal.class,
          () -> new org.seasar.doma.wrapper.BigDecimalWrapper(),
          null,
          null,
          "salary",
          "SALARY",
          __namingType,
          true,
          true,
          false);

  public final VersionPropertyType<Object, Emp, Integer, Integer> version =
      new VersionPropertyType<>(
          Emp.class,
          Integer.class,
          Integer.class,
          () -> new org.seasar.doma.wrapper.IntegerWrapper(),
          null,
          null,
          "version",
          "VERSION",
          __namingType,
          false);

  private final String __name = "Emp";

  private final String __catalogName = null;

  private final String __schemaName = null;

  private final String __tableName = "";

  private final List<EntityPropertyType<Emp, ?>> __idPropertyTypes;

  private final List<EntityPropertyType<Emp, ?>> __entityPropertyTypes;

  private final Map<String, EntityPropertyType<Emp, ?>> __entityPropertyTypeMap;

  private _Emp() {
    List<EntityPropertyType<Emp, ?>> __idList = new ArrayList<>();
    __idList.add(id);
    __idPropertyTypes = Collections.unmodifiableList(__idList);
    List<EntityPropertyType<Emp, ?>> __list = new ArrayList<>();
    __list.add(id);
    __list.add(name);
    __list.add(salary);
    __list.add(version);
    __entityPropertyTypes = Collections.unmodifiableList(__list);
    Map<String, EntityPropertyType<Emp, ?>> __map = new HashMap<>();
    __map.put("id", id);
    __map.put("name", name);
    __map.put("salary", salary);
    __map.put("version", version);
    __entityPropertyTypeMap = Collections.unmodifiableMap(__map);
  }

  @Override
  public boolean isImmutable() {
    return false;
  }

  @Override
  public Emp newEntity(Map<String, Property<Emp, ?>> args) {
    Emp entity = new Emp();
    args.values().forEach(v -> v.save(entity));
    return entity;
  }

  @Override
  public Class<Emp> getEntityClass() {
    return Emp.class;
  }

  @Override
  public String getName() {
    return __name;
  }

  @Override
  public List<EntityPropertyType<Emp, ?>> getEntityPropertyTypes() {
    return __entityPropertyTypes;
  }

  @Override
  public EntityPropertyType<Emp, ?> getEntityPropertyType(String propertyName) {
    return __entityPropertyTypeMap.get(propertyName);
  }

  @Override
  public void saveCurrentStates(Emp __entity) {
    Emp __currentStates = new Emp();
    id.copy(__currentStates, __entity);
    name.copy(__currentStates, __entity);
    salary.copy(__currentStates, __entity);
    version.copy(__currentStates, __entity);
    __originalStatesAccessor.set(__entity, __currentStates);
  }

  @Override
  public Emp getOriginalStates(Emp entity) {
    if (entity.originalStates instanceof Emp) {
      Emp originalStates = (Emp) entity.originalStates;
      return originalStates;
    }
    return null;
  }

  @Override
  public GeneratedIdPropertyType<Object, Emp, ?, ?> getGeneratedIdPropertyType() {
    return null;
  }

  @Override
  public VersionPropertyType<Object, Emp, ?, ?> getVersionPropertyType() {
    return version;
  }

  @Override
  public TenantIdPropertyType<Object, Emp, ?, ?> getTenantIdPropertyType() {
    return null;
  }

  @Override
  public List<EntityPropertyType<Emp, ?>> getIdPropertyTypes() {
    return __idPropertyTypes;
  }

  @Override
  public void preInsert(Emp entity, PreInsertContext<Emp> context) {}

  @Override
  public void preUpdate(Emp entity, PreUpdateContext<Emp> context) {}

  @Override
  public void preDelete(Emp entity, PreDeleteContext<Emp> context) {}

  @Override
  public void postInsert(Emp entity, PostInsertContext<Emp> context) {}

  @Override
  public void postUpdate(Emp entity, PostUpdateContext<Emp> context) {}

  @Override
  public void postDelete(Emp entity, PostDeleteContext<Emp> context) {}

  @Override
  public String getCatalogName() {
    return __catalogName;
  }

  @Override
  public String getSchemaName() {
    return __schemaName;
  }

  @Override
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

  public static _Emp getSingletonInternal() {
    return singleton;
  }
}
