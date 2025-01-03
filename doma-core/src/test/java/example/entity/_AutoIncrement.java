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
package example.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import javax.annotation.processing.Generated;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.jdbc.entity.AbstractEntityType;
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
import org.seasar.doma.jdbc.id.BuiltinIdentityIdGenerator;
import org.seasar.doma.wrapper.IntegerWrapper;

@Generated("")
public class _AutoIncrement extends AbstractEntityType<AutoIncrement> {

  private static final _AutoIncrement singleton = new _AutoIncrement();

  private final NamingType __namingType = NamingType.NONE;

  public final GeneratedIdPropertyType<AutoIncrement, Integer, Integer> id =
      new GeneratedIdPropertyType<>(
          AutoIncrement.class,
          () -> new BasicScalar<>(IntegerWrapper::new),
          "id",
          "ID",
          __namingType,
          false,
          new BuiltinIdentityIdGenerator());

  private final String __name = "AutoIncrement";

  private final String __catalogName = null;

  private final String __schemaName = null;

  private final String __tableName = "";

  private final List<EntityPropertyType<AutoIncrement, ?>> __idPropertyTypes;

  private final List<EntityPropertyType<AutoIncrement, ?>> __entityPropertyTypes;

  private final Map<String, EntityPropertyType<AutoIncrement, ?>> __entityPropertyTypeMap;

  private _AutoIncrement() {
    List<EntityPropertyType<AutoIncrement, ?>> __idList = new ArrayList<>();
    __idList.add(id);
    __idPropertyTypes = Collections.unmodifiableList(__idList);
    List<EntityPropertyType<AutoIncrement, ?>> __list = new ArrayList<>();
    __list.add(id);
    __entityPropertyTypes = Collections.unmodifiableList(__list);
    Map<String, EntityPropertyType<AutoIncrement, ?>> __map = new HashMap<>();
    __map.put("id", id);
    __entityPropertyTypeMap = Collections.unmodifiableMap(__map);
  }

  @Override
  public boolean isImmutable() {
    return false;
  }

  @Override
  public AutoIncrement newEntity(Map<String, Property<AutoIncrement, ?>> args) {
    AutoIncrement entity = new AutoIncrement();
    args.values().forEach(v -> v.save(entity));
    return entity;
  }

  @Override
  public Class<AutoIncrement> getEntityClass() {
    return AutoIncrement.class;
  }

  @Override
  public String getName() {
    return __name;
  }

  @Override
  public List<EntityPropertyType<AutoIncrement, ?>> getEntityPropertyTypes() {
    return __entityPropertyTypes;
  }

  @Override
  public EntityPropertyType<AutoIncrement, ?> getEntityPropertyType(String propertyName) {
    return __entityPropertyTypeMap.get(propertyName);
  }

  @Override
  public void saveCurrentStates(AutoIncrement __entity) {}

  @Override
  public AutoIncrement getOriginalStates(AutoIncrement entity) {
    return null;
  }

  @Override
  public GeneratedIdPropertyType<AutoIncrement, ?, ?> getGeneratedIdPropertyType() {
    return id;
  }

  @Override
  public VersionPropertyType<AutoIncrement, ?, ?> getVersionPropertyType() {
    return null;
  }

  @Override
  public TenantIdPropertyType<AutoIncrement, ?, ?> getTenantIdPropertyType() {
    return null;
  }

  @Override
  public List<EntityPropertyType<AutoIncrement, ?>> getIdPropertyTypes() {
    return __idPropertyTypes;
  }

  @Override
  public void preInsert(AutoIncrement entity, PreInsertContext<AutoIncrement> context) {}

  @Override
  public void preUpdate(AutoIncrement entity, PreUpdateContext<AutoIncrement> context) {}

  @Override
  public void preDelete(AutoIncrement entity, PreDeleteContext<AutoIncrement> context) {}

  @Override
  public void postInsert(AutoIncrement entity, PostInsertContext<AutoIncrement> context) {}

  @Override
  public void postUpdate(AutoIncrement entity, PostUpdateContext<AutoIncrement> context) {}

  @Override
  public void postDelete(AutoIncrement entity, PostDeleteContext<AutoIncrement> context) {}

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

  public static _AutoIncrement getSingletonInternal() {
    return singleton;
  }
}
