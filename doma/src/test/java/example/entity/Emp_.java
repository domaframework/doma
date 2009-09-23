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

import org.seasar.doma.jdbc.entity.AbstractEntityMeta;
import org.seasar.doma.jdbc.entity.AssignedIdPropertyMeta;
import org.seasar.doma.jdbc.entity.BasicPropertyMeta;
import org.seasar.doma.jdbc.entity.BuiltinEntityListener;
import org.seasar.doma.jdbc.entity.EntityMeta;
import org.seasar.doma.jdbc.entity.EntityMetaFactory;
import org.seasar.doma.jdbc.entity.EntityPropertyMeta;
import org.seasar.doma.jdbc.entity.GeneratedIdPropertyMeta;
import org.seasar.doma.jdbc.entity.VersionPropertyMeta;
import org.seasar.doma.wrapper.BigDecimalWrapper;
import org.seasar.doma.wrapper.IntegerWrapper;
import org.seasar.doma.wrapper.StringWrapper;

@Generated("")
public class Emp_ implements EntityMetaFactory<Emp> {

    @Override
    public EntityMeta<Emp> createEntityMeta() {
        return new Meta();
    }

    @Override
    public EntityMeta<Emp> createEntityMeta(Emp entity) {
        return new Meta(entity);
    }

    public static class Meta extends AbstractEntityMeta<Emp> {

        private static final BuiltinEntityListener __listener = new BuiltinEntityListener();

        private final AssignedIdPropertyMeta<IntegerWrapper> id = new AssignedIdPropertyMeta<IntegerWrapper>(
                "id", null, new IntegerWrapper());

        private final BasicPropertyMeta<StringWrapper> name = new BasicPropertyMeta<StringWrapper>(
                "name", null, new StringWrapper(), true, true);

        private final BasicPropertyMeta<BigDecimalWrapper> salary = new BasicPropertyMeta<BigDecimalWrapper>(
                "salary", null, new BigDecimalWrapper(), true, true);

        private final VersionPropertyMeta<IntegerWrapper> version = new VersionPropertyMeta<IntegerWrapper>(
                "version", null, new IntegerWrapper());

        private final Set<String> dirtyStates;

        private final String __name = "emp";

        private List<EntityPropertyMeta<?>> __properties;

        private Map<String, EntityPropertyMeta<?>> __propertyMap;

        private final Emp __entity;

        private Meta() {
            this(new Emp());
        }

        private Meta(Emp entity) {
            super(null, null, null);
            this.__entity = entity;
            id.getWrapper().set(entity.id);
            name.getWrapper().set(entity.name);
            salary.getWrapper().set(entity.salary);
            version.getWrapper().set(entity.version);
            dirtyStates = entity.dirtyStates;
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
        public List<EntityPropertyMeta<?>> getPropertyMetas() {
            if (__properties == null) {
                List<EntityPropertyMeta<?>> list = new ArrayList<EntityPropertyMeta<?>>();
                list.add(id);
                list.add(name);
                list.add(salary);
                list.add(version);
                __properties = Collections.unmodifiableList(list);
            }
            return __properties;
        }

        @Override
        public EntityPropertyMeta<?> getPropertyMeta(String propertyName) {
            if (__propertyMap == null) {
                Map<String, EntityPropertyMeta<?>> map = new HashMap<String, EntityPropertyMeta<?>>();
                map.put("id", id);
                map.put("name", name);
                map.put("salary", salary);
                map.put("version", version);
                __propertyMap = Collections.unmodifiableMap(map);
            }
            return __propertyMap.get(propertyName);
        }

        @Override
        public GeneratedIdPropertyMeta<?> getGeneratedIdProperty() {
            return null;
        }

        @Override
        public VersionPropertyMeta<?> getVersionProperty() {
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
        public Set<String> getModifiedProperties() {
            return dirtyStates;
        }

    }

}
