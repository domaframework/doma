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
import org.seasar.doma.jdbc.entity.VersionPropertyType;

@Generated("")
public class _ImmutableEmp extends AbstractEntityType<ImmutableEmp> {

    private static _ImmutableEmp singleton = new _ImmutableEmp();

    private final NamingType __namingType = NamingType.UPPER_CASE;

    public final AssignedIdPropertyType<Object, ImmutableEmp, Integer, Object> id = new AssignedIdPropertyType<>(
            ImmutableEmp.class, Integer.class, Integer.class,
            () -> new org.seasar.doma.wrapper.IntegerWrapper(), null, null,
            "id", "ID", __namingType, false);

    public final DefaultPropertyType<Object, ImmutableEmp, String, Object> name = new DefaultPropertyType<>(
            ImmutableEmp.class, String.class, String.class,
            () -> new org.seasar.doma.wrapper.StringWrapper(), null, null,
            "name", "NAME", __namingType, true, true, false);

    public final DefaultPropertyType<Object, ImmutableEmp, BigDecimal, BigDecimal> salary = new DefaultPropertyType<>(
            ImmutableEmp.class, BigDecimal.class, BigDecimal.class,
            () -> new org.seasar.doma.wrapper.BigDecimalWrapper(), null, null,
            "salary", "SALARY", __namingType, true, true, false);

    public final VersionPropertyType<Object, ImmutableEmp, Integer, Integer> version = new VersionPropertyType<>(
            ImmutableEmp.class, Integer.class, Integer.class,
            () -> new org.seasar.doma.wrapper.IntegerWrapper(), null, null,
            "version", "VERSION", __namingType, false);

    private final String __name = "Emp";

    private final String __catalogName = null;

    private final String __schemaName = null;

    private final String __tableName = "EMP";

    private final List<EntityPropertyType<ImmutableEmp, ?>> __idPropertyTypes;

    private final List<EntityPropertyType<ImmutableEmp, ?>> __entityPropertyTypes;

    private final Map<String, EntityPropertyType<ImmutableEmp, ?>> __entityPropertyTypeMap;

    private _ImmutableEmp() {
        List<EntityPropertyType<ImmutableEmp, ?>> __idList = new ArrayList<>();
        __idList.add(id);
        __idPropertyTypes = Collections.unmodifiableList(__idList);
        List<EntityPropertyType<ImmutableEmp, ?>> __list = new ArrayList<>();
        __list.add(id);
        __list.add(name);
        __list.add(salary);
        __list.add(version);
        __entityPropertyTypes = Collections.unmodifiableList(__list);
        Map<String, EntityPropertyType<ImmutableEmp, ?>> __map = new HashMap<>();
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
    public ImmutableEmp newEntity(Map<String, Property<ImmutableEmp, ?>> args) {
        return new ImmutableEmp((Integer) (args.containsKey("id") ? args.get(
                "id").get() : null), (String) (args.containsKey("name") ? args
                .get("name").get() : null),
                (BigDecimal) (args.containsKey("salary") ? args.get("salary")
                        .get() : null),
                (Integer) (args.containsKey("version") ? args.get("version")
                        .get() : null));
    }

    @Override
    public Class<ImmutableEmp> getEntityClass() {
        return ImmutableEmp.class;
    }

    @Override
    public String getName() {
        return __name;
    }

    @Override
    public List<EntityPropertyType<ImmutableEmp, ?>> getEntityPropertyTypes() {
        return __entityPropertyTypes;
    }

    @Override
    public EntityPropertyType<ImmutableEmp, ?> getEntityPropertyType(
            String propertyName) {
        return __entityPropertyTypeMap.get(propertyName);
    }

    @Override
    public void saveCurrentStates(ImmutableEmp __entity) {
    }

    @Override
    public ImmutableEmp getOriginalStates(ImmutableEmp entity) {
        return null;
    }

    @Override
    public GeneratedIdPropertyType<Object, ImmutableEmp, ?, ?> getGeneratedIdPropertyType() {
        return null;
    }

    @Override
    public VersionPropertyType<Object, ImmutableEmp, ?, ?> getVersionPropertyType() {
        return version;
    }

    @Override
    public List<EntityPropertyType<ImmutableEmp, ?>> getIdPropertyTypes() {
        return __idPropertyTypes;
    }

    @Override
    public void preInsert(ImmutableEmp entity,
            PreInsertContext<ImmutableEmp> context) {
    }

    @Override
    public void preUpdate(ImmutableEmp entity,
            PreUpdateContext<ImmutableEmp> context) {
    }

    @Override
    public void preDelete(ImmutableEmp entity,
            PreDeleteContext<ImmutableEmp> context) {
    }

    @Override
    public void postInsert(ImmutableEmp entity,
            PostInsertContext<ImmutableEmp> context) {
    }

    @Override
    public void postUpdate(ImmutableEmp entity,
            PostUpdateContext<ImmutableEmp> context) {
    }

    @Override
    public void postDelete(ImmutableEmp entity,
            PostDeleteContext<ImmutableEmp> context) {
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
        return __tableName;
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
        return false;
    }

    public static _ImmutableEmp getSingletonInternal() {
        return singleton;
    }
}
