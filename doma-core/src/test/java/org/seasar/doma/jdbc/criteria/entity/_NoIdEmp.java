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
package org.seasar.doma.jdbc.criteria.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import javax.annotation.processing.Generated;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.jdbc.entity.AbstractEntityType;
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
import org.seasar.doma.wrapper.BigDecimalWrapper;
import org.seasar.doma.wrapper.IntegerWrapper;
import org.seasar.doma.wrapper.StringWrapper;

@Generated("")
public class _NoIdEmp extends AbstractEntityType<NoIdEmp> {

  private static final _NoIdEmp singleton = new _NoIdEmp();

  private final NamingType __namingType = NamingType.SNAKE_UPPER_CASE;

  public final DefaultPropertyType<NoIdEmp, Integer, Integer> id =
      new DefaultPropertyType<>(
          NoIdEmp.class,
          () -> new BasicScalar<>(IntegerWrapper::new),
          "id",
          "ID",
          __namingType,
          true,
          true,
          false);

  public final DefaultPropertyType<NoIdEmp, String, String> name =
      new DefaultPropertyType<>(
          NoIdEmp.class,
          () -> new BasicScalar<>(StringWrapper::new),
          "name",
          "NAME",
          __namingType,
          true,
          true,
          false);

  public final DefaultPropertyType<NoIdEmp, BigDecimal, BigDecimal> salary =
      new DefaultPropertyType<>(
          NoIdEmp.class,
          () -> new BasicScalar<>(BigDecimalWrapper::new),
          "salary",
          "SALARY",
          __namingType,
          true,
          true,
          false);

  public final VersionPropertyType<NoIdEmp, Integer, Integer> version =
      new VersionPropertyType<>(
          NoIdEmp.class,
          () -> new BasicScalar<>(IntegerWrapper::new),
          "version",
          "VERSION",
          __namingType,
          false);

  private final String __name = "NoIdEmp";

  private final String __catalogName = null;

  private final String __schemaName = null;

  private final String __tableName = "";

  private final List<EntityPropertyType<NoIdEmp, ?>> __idPropertyTypes;

  private final List<EntityPropertyType<NoIdEmp, ?>> __entityPropertyTypes;

  private final Map<String, EntityPropertyType<NoIdEmp, ?>> __entityPropertyTypeMap;

  public _NoIdEmp() {
    List<EntityPropertyType<NoIdEmp, ?>> __idList = new ArrayList<>();
    __idPropertyTypes = Collections.unmodifiableList(__idList);
    List<EntityPropertyType<NoIdEmp, ?>> __list = new ArrayList<>();
    __list.add(id);
    __list.add(name);
    __list.add(salary);
    __list.add(version);
    __entityPropertyTypes = Collections.unmodifiableList(__list);
    Map<String, EntityPropertyType<NoIdEmp, ?>> __map = new HashMap<>();
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
  public NoIdEmp newEntity(Map<String, Property<NoIdEmp, ?>> args) {
    NoIdEmp entity = new NoIdEmp();
    args.values().forEach(v -> v.save(entity));
    return entity;
  }

  @Override
  public Class<NoIdEmp> getEntityClass() {
    return NoIdEmp.class;
  }

  @Override
  public String getName() {
    return __name;
  }

  @Override
  public List<EntityPropertyType<NoIdEmp, ?>> getEntityPropertyTypes() {
    return __entityPropertyTypes;
  }

  @Override
  public EntityPropertyType<NoIdEmp, ?> getEntityPropertyType(String propertyName) {
    return __entityPropertyTypeMap.get(propertyName);
  }

  @Override
  public void saveCurrentStates(NoIdEmp __entity) {}

  @Override
  public NoIdEmp getOriginalStates(NoIdEmp entity) {
    return null;
  }

  @Override
  public GeneratedIdPropertyType<NoIdEmp, ?, ?> getGeneratedIdPropertyType() {
    return null;
  }

  @Override
  public VersionPropertyType<NoIdEmp, ?, ?> getVersionPropertyType() {
    return version;
  }

  @Override
  public TenantIdPropertyType<NoIdEmp, ?, ?> getTenantIdPropertyType() {
    return null;
  }

  @Override
  public List<EntityPropertyType<NoIdEmp, ?>> getIdPropertyTypes() {
    return __idPropertyTypes;
  }

  @Override
  public void preInsert(NoIdEmp entity, PreInsertContext<NoIdEmp> context) {}

  @Override
  public void preUpdate(NoIdEmp entity, PreUpdateContext<NoIdEmp> context) {}

  @Override
  public void preDelete(NoIdEmp entity, PreDeleteContext<NoIdEmp> context) {}

  @Override
  public void postInsert(NoIdEmp entity, PostInsertContext<NoIdEmp> context) {}

  @Override
  public void postUpdate(NoIdEmp entity, PostUpdateContext<NoIdEmp> context) {}

  @Override
  public void postDelete(NoIdEmp entity, PostDeleteContext<NoIdEmp> context) {}

  @Override
  public String getCatalogName() {
    return __catalogName;
  }

  @Override
  public String getSchemaName() {
    return __schemaName;
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

  public static _NoIdEmp getSingletonInternal() {
    return singleton;
  }
}
