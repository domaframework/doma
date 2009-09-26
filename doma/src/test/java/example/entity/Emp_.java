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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Generated;

import org.seasar.doma.internal.jdbc.entity.AssignedIdPropertyType;
import org.seasar.doma.internal.jdbc.entity.BasicPropertyType;
import org.seasar.doma.internal.jdbc.entity.EntityPropertyType;
import org.seasar.doma.internal.jdbc.entity.EntityType;
import org.seasar.doma.internal.jdbc.entity.EntityTypeFactory;
import org.seasar.doma.internal.jdbc.entity.GeneratedIdPropertyType;
import org.seasar.doma.internal.jdbc.entity.VersionPropertyType;
import org.seasar.doma.jdbc.entity.BuiltinEntityListener;
import org.seasar.doma.wrapper.BigDecimalWrapper;
import org.seasar.doma.wrapper.IntegerWrapper;
import org.seasar.doma.wrapper.StringWrapper;

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

        private static final BuiltinEntityListener __listener = new BuiltinEntityListener();

        private final AssignedIdPropertyType<IntegerWrapper> id = new AssignedIdPropertyType<IntegerWrapper>(
                "id", null, new IntegerWrapper());

        private final BasicPropertyType<StringWrapper> name = new BasicPropertyType<StringWrapper>(
                "name", null, new StringWrapper(), true, true);

        private final BasicPropertyType<BigDecimalWrapper> salary = new BasicPropertyType<BigDecimalWrapper>(
                "salary", null, new BigDecimalWrapper(), true, true);

        private final VersionPropertyType<IntegerWrapper> version = new VersionPropertyType<IntegerWrapper>(
                "version", null, new IntegerWrapper());

        private final Set<String> modifiedProperties;

        private final String __name = "Emp";

        private final String __catalogName;

        private final String __schemaName;

        private final String __tableName;

        private List<EntityPropertyType<?>> __properties;

        private Map<String, EntityPropertyType<?>> __propertyMap;

        private final Emp __entity;

        private EmpType() {
            this(new Emp());
        }

        private EmpType(Emp entity) {
            this.__entity = entity;
            this.__catalogName = null;
            this.__schemaName = null;
            this.__tableName = null;
            id.getWrapper().set(entity.id);
            name.getWrapper().set(entity.name);
            salary.getWrapper().set(entity.salary);
            version.getWrapper().set(entity.version);
            modifiedProperties = entity.dirtyStates;
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

        protected void refreshEntityInternal() {
            __entity.id = id.getWrapper().get();
            __entity.name = name.getWrapper().get();
            __entity.salary = salary.getWrapper().get();
            __entity.version = version.getWrapper().get();
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
        }

        @Override
        public void preUpdate() {
            __listener.preUpdate(__entity);
        }

        @Override
        public void preDelete() {
            __listener.preDelete(__entity);
        }

        @Override
        public Set<String> getChangedProperties() {
            return modifiedProperties;
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

}
