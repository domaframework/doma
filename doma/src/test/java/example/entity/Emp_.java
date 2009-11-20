/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import org.seasar.doma.internal.jdbc.entity.AssignedIdPropertyType;
import org.seasar.doma.internal.jdbc.entity.BasicPropertyType;
import org.seasar.doma.internal.jdbc.entity.EntityPropertyType;
import org.seasar.doma.internal.jdbc.entity.EntityType;
import org.seasar.doma.internal.jdbc.entity.EntityTypeFactory;
import org.seasar.doma.internal.jdbc.entity.GeneratedIdPropertyType;
import org.seasar.doma.internal.jdbc.entity.VersionPropertyType;
import org.seasar.doma.jdbc.entity.NullEntityListener;
import org.seasar.doma.wrapper.BigDecimalWrapper;
import org.seasar.doma.wrapper.IntegerWrapper;
import org.seasar.doma.wrapper.StringWrapper;
import org.seasar.doma.wrapper.Wrapper;

@Generated("")
public class Emp_ implements EntityTypeFactory<Emp> {

    @Override
    public EntityType<Emp> createEntityType() {
        return new EmpType();
    }

    @Override
    public EntityType<Emp> createEntityType(Emp entity) {
        return new EmpType(entity);
    }

    public static class EmpType implements EntityType<Emp> {

        private static final NullEntityListener __listener = new NullEntityListener();

        private final AssignedIdPropertyType<IntegerWrapper> id = new AssignedIdPropertyType<IntegerWrapper>(
                "id", "ID", new IntegerWrapper());

        private final BasicPropertyType<StringWrapper> name = new BasicPropertyType<StringWrapper>(
                "name", "NAME", new StringWrapper(), true, true);

        private final BasicPropertyType<BigDecimalWrapper> salary = new BasicPropertyType<BigDecimalWrapper>(
                "salary", "SALARY", new BigDecimalWrapper(), true, true);

        private final VersionPropertyType<IntegerWrapper> version = new VersionPropertyType<IntegerWrapper>(
                "version", "VERSION", new IntegerWrapper());

        private final HashMap<String, Wrapper<?>> __originalStates;

        private final String __name = "Emp";

        private final String __catalogName = null;

        private final String __schemaName = null;

        private final String __tableName = "EMP";

        private List<EntityPropertyType<?>> __properties;

        private Map<String, EntityPropertyType<?>> __propertyMap;

        private final Emp __entity;

        private EmpType() {
            this(new Emp());
        }

        private EmpType(Emp entity) {
            this.__entity = entity;
            if (HashMap.class.isInstance(entity.originalStates)) {
                @SuppressWarnings("unchecked")
                HashMap<String, Wrapper<?>> originalStates = (HashMap<String, Wrapper<?>>) entity.originalStates;
                __originalStates = originalStates;
            } else {
                __originalStates = null;
            }
            refreshEntityTypeInternal();
        }

        @Override
        public Emp getEntity() {
            refreshEntityInternal();
            return __entity;
        }

        @Override
        public Class<Emp> getEntityClass() {
            return Emp.class;
        }

        @Override
        public void refreshEntity() {
            refreshEntityInternal();
        }

        private void refreshEntityInternal() {
            __entity.id = id.getWrapper().get();
            __entity.name = name.getWrapper().get();
            __entity.salary = salary.getWrapper().get();
            __entity.version = version.getWrapper().get();

            if (__originalStates != null) {
                __originalStates.clear();
                __originalStates.put("id", id.getWrapper());
                __originalStates.put("name", name.getWrapper());
                __originalStates.put("salary", salary.getWrapper());
                __originalStates.put("version", version.getWrapper());
            }
        }

        private void refreshEntityTypeInternal() {
            id.getWrapper().set(__entity.id);
            name.getWrapper().set(__entity.name);
            salary.getWrapper().set(__entity.salary);
            version.getWrapper().set(__entity.version);
        }

        @Override
        public String getName() {
            return __name;
        }

        @Override
        public List<EntityPropertyType<?>> getEntityPropertyTypes() {
            if (__properties == null) {
                List<EntityPropertyType<?>> list = new ArrayList<EntityPropertyType<?>>();
                list.add(id);
                list.add(name);
                list.add(salary);
                list.add(version);
                __properties = Collections.unmodifiableList(list);
            }
            return __properties;
        }

        @Override
        public EntityPropertyType<?> getEntityPropertyType(String propertyName) {
            if (__propertyMap == null) {
                Map<String, EntityPropertyType<?>> map = new HashMap<String, EntityPropertyType<?>>();
                map.put("id", id);
                map.put("name", name);
                map.put("salary", salary);
                map.put("version", version);
                __propertyMap = Collections.unmodifiableMap(map);
            }
            return __propertyMap.get(propertyName);
        }

        @Override
        public Map<String, Wrapper<?>> getOriginalStates() {
            return __originalStates;
        }

        @Override
        public GeneratedIdPropertyType<?> getGeneratedIdPropertyType() {
            return null;
        }

        @Override
        public VersionPropertyType<?> getVersionPropertyType() {
            return version;
        }

        @Override
        public void preInsert() {
            __listener.preInsert(__entity);
            refreshEntityTypeInternal();
        }

        @Override
        public void preUpdate() {
            __listener.preUpdate(__entity);
            refreshEntityTypeInternal();
        }

        @Override
        public void preDelete() {
            __listener.preDelete(__entity);
            refreshEntityTypeInternal();
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

    }

    public static class EmpAccessor {

        public void setId(Emp entity, Integer id) {
            entity.id = id;
        }

        public Integer getId(Emp entity) {
            return entity.id;
        }

        public void setName(Emp entity, String name) {
            entity.name = name;
        }

        public String getName(Emp entity) {
            return entity.name;
        }

        public void setSalary(Emp entity, BigDecimal salary) {
            entity.salary = salary;
        }

        public BigDecimal getSalary(Emp entity) {
            return entity.salary;
        }

        public void setVersion(Emp entity, Integer version) {
            entity.version = version;
        }

        public Integer getVerion(Emp entity) {
            return entity.version;
        }

        public void setOriginalStates(Emp entity, Serializable originalStates) {
            entity.originalStates = originalStates;
        }

        public Serializable getOriginalStates(Emp entity) {
            return entity.originalStates;
        }
    }

}
