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

import javax.annotation.processing.Generated;

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
import org.seasar.doma.jdbc.entity.VersionPropertyType;
import org.seasar.doma.jdbc.id.BuiltinIdentityIdGenerator;

@Generated("")
public class _IdGeneratedEmp extends AbstractEntityType<IdGeneratedEmp> {

    private static _IdGeneratedEmp singleton = new _IdGeneratedEmp();

    private static final org.seasar.doma.jdbc.entity.OriginalStatesAccessor<IdGeneratedEmp> __originalStatesAccessor = new org.seasar.doma.jdbc.entity.OriginalStatesAccessor<>(
            IdGeneratedEmp.class, "originalStates");

    private final NamingType __namingType = NamingType.UPPER_CASE;

    public final GeneratedIdPropertyType<IdGeneratedEmp, Integer, Object> id = new GeneratedIdPropertyType<>(
            IdGeneratedEmp.class, Integer.class,
            () -> new org.seasar.doma.wrapper.IntegerWrapper(), null,
            "id", "ID", __namingType, false, new BuiltinIdentityIdGenerator());

    public final DefaultPropertyType<IdGeneratedEmp, String, Object> name = new DefaultPropertyType<>(
            IdGeneratedEmp.class, String.class,
            () -> new org.seasar.doma.wrapper.StringWrapper(), null,
            "name", "NAME", __namingType, true, true, false);

    public final DefaultPropertyType<IdGeneratedEmp, BigDecimal, BigDecimal> salary = new DefaultPropertyType<>(
            IdGeneratedEmp.class, BigDecimal.class,
            () -> new org.seasar.doma.wrapper.BigDecimalWrapper(), null,
            "salary", "SALARY", __namingType, true, true, false);

    public final VersionPropertyType<IdGeneratedEmp, Integer, Integer> version = new VersionPropertyType<>(
            IdGeneratedEmp.class, Integer.class,
            () -> new org.seasar.doma.wrapper.IntegerWrapper(), null,
            "version", "VERSION", __namingType, false);

    private final String __name = "Emp";

    private final String __catalogName = "CATA";

    private final String __schemaName = null;

    private final String __tableName = "";

    private final List<EntityPropertyType<IdGeneratedEmp, ?>> __idPropertyTypes;

    private final List<EntityPropertyType<IdGeneratedEmp, ?>> __entityPropertyTypes;

    private final Map<String, EntityPropertyType<IdGeneratedEmp, ?>> __entityPropertyTypeMap;

    private _IdGeneratedEmp() {
        List<EntityPropertyType<IdGeneratedEmp, ?>> __idList = new ArrayList<>();
        __idList.add(id);
        __idPropertyTypes = Collections.unmodifiableList(__idList);
        List<EntityPropertyType<IdGeneratedEmp, ?>> __list = new ArrayList<>();
        __list.add(id);
        __list.add(name);
        __list.add(salary);
        __list.add(version);
        __entityPropertyTypes = Collections.unmodifiableList(__list);
        Map<String, EntityPropertyType<IdGeneratedEmp, ?>> __map = new HashMap<>();
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
    public IdGeneratedEmp newEntity(
            Map<String, Property<IdGeneratedEmp, ?>> args) {
        IdGeneratedEmp entity = new IdGeneratedEmp();
        args.values().forEach(v -> v.save(entity));
        return entity;
    }

    @Override
    public Class<IdGeneratedEmp> getEntityClass() {
        return IdGeneratedEmp.class;
    }

    @Override
    public String getName() {
        return __name;
    }

    @Override
    public List<EntityPropertyType<IdGeneratedEmp, ?>> getEntityPropertyTypes() {
        return __entityPropertyTypes;
    }

    @Override
    public EntityPropertyType<IdGeneratedEmp, ?> getEntityPropertyType(
            String propertyName) {
        return __entityPropertyTypeMap.get(propertyName);
    }

    @Override
    public void saveCurrentStates(IdGeneratedEmp __entity) {
        IdGeneratedEmp __currentStates = new IdGeneratedEmp();
        id.copy(__currentStates, __entity);
        name.copy(__currentStates, __entity);
        salary.copy(__currentStates, __entity);
        version.copy(__currentStates, __entity);
        __originalStatesAccessor.set(__entity, __currentStates);
    }

    @Override
    public IdGeneratedEmp getOriginalStates(IdGeneratedEmp entity) {
        if (entity.originalStates instanceof IdGeneratedEmp) {
            IdGeneratedEmp originalStates = (IdGeneratedEmp) entity.originalStates;
            return originalStates;
        }
        return null;
    }

    @Override
    public GeneratedIdPropertyType<IdGeneratedEmp, ?, ?> getGeneratedIdPropertyType() {
        return id;
    }

    @Override
    public VersionPropertyType<IdGeneratedEmp, ?, ?> getVersionPropertyType() {
        return version;
    }

    @Override
    public List<EntityPropertyType<IdGeneratedEmp, ?>> getIdPropertyTypes() {
        return __idPropertyTypes;
    }

    @Override
    public void preInsert(IdGeneratedEmp entity,
            PreInsertContext<IdGeneratedEmp> context) {
    }

    @Override
    public void preUpdate(IdGeneratedEmp entity,
            PreUpdateContext<IdGeneratedEmp> context) {
    }

    @Override
    public void preDelete(IdGeneratedEmp entity,
            PreDeleteContext<IdGeneratedEmp> context) {
    }

    @Override
    public void postInsert(IdGeneratedEmp entity,
            PostInsertContext<IdGeneratedEmp> context) {
    }

    @Override
    public void postUpdate(IdGeneratedEmp entity,
            PostUpdateContext<IdGeneratedEmp> context) {
    }

    @Override
    public void postDelete(IdGeneratedEmp entity,
            PostDeleteContext<IdGeneratedEmp> context) {
    }

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
    public String getTableName(
            BiFunction<NamingType, String, String> namingFunction) {
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
        return true;
    }

    public static _IdGeneratedEmp getSingletonInternal() {
        return singleton;
    }
}
