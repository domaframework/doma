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
package org.seasar.doma.internal.apt.entity;

import java.util.List;
import java.util.Set;

import org.seasar.doma.internal.jdbc.entity.EntityPropertyType;
import org.seasar.doma.internal.jdbc.entity.EntityType;
import org.seasar.doma.internal.jdbc.entity.EntityTypeFactory;
import org.seasar.doma.internal.jdbc.entity.GeneratedIdPropertyType;
import org.seasar.doma.internal.jdbc.entity.VersionPropertyType;

/**
 * @author taedium
 * 
 */
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

        private EmpType() {
            this(new Emp());
        }

        private EmpType(Emp entity) {
        }

        @Override
        public Emp getEntity() {
            return null;
        }

        @Override
        public Class<Emp> getEntityClass() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public EntityPropertyType<?> getEntityPropertyType(String propertyName) {
            return null;
        }

        @Override
        public List<EntityPropertyType<?>> getEntityPropertyTypes() {
            return null;
        }

        @Override
        public GeneratedIdPropertyType<?> getGeneratedIdPropertyType() {
            return null;
        }

        @Override
        public VersionPropertyType<?> getVersionPropertyType() {
            return null;
        }

        @Override
        public void preDelete() {
        }

        @Override
        public void preInsert() {
        }

        @Override
        public void preUpdate() {
        }

        @Override
        public Set<String> getChangedProperties() {
            return null;
        }

        @Override
        public void refreshEntity() {
        }

        @Override
        public String getCatalogName() {
            return null;
        }

        @Override
        public String getSchemaName() {
            return null;
        }

        @Override
        public String getTableName() {
            return null;
        }

    }

}
