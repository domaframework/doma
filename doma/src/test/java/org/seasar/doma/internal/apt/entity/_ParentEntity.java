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
package org.seasar.doma.internal.apt.entity;

import java.util.List;
import java.util.Map;

import org.seasar.doma.jdbc.entity.AbstractEntityType;
import org.seasar.doma.jdbc.entity.BasicPropertyType;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.GeneratedIdPropertyType;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.entity.PostUpdateContext;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;
import org.seasar.doma.jdbc.entity.VersionPropertyType;
import org.seasar.doma.wrapper.IntegerWrapper;

public class _ParentEntity extends AbstractEntityType<ParentEntity> {

    public BasicPropertyType<Object, ParentEntity, Integer, Object> $aaa = new BasicPropertyType<Object, ParentEntity, Integer, Object>(
            ParentEntity.class, Integer.class, IntegerWrapper.class, null,
            null, "aaa", "AAA", true, true);

    public BasicPropertyType<Object, ParentEntity, Integer, Object> $bbb = new BasicPropertyType<Object, ParentEntity, Integer, Object>(
            ParentEntity.class, Integer.class, IntegerWrapper.class, null,
            null, "bbb", "BBB", true, true);

    private _ParentEntity() {
    }

    @Override
    public void saveCurrentStates(ParentEntity entity) {
    }

    @Override
    public String getCatalogName() {

        return null;
    }

    @Override
    public Class<ParentEntity> getEntityClass() {

        return null;
    }

    @Override
    public EntityPropertyType<ParentEntity, ?> getEntityPropertyType(String name) {

        return null;
    }

    @Override
    public List<EntityPropertyType<ParentEntity, ?>> getEntityPropertyTypes() {

        return null;
    }

    @Override
    public GeneratedIdPropertyType<Object, ParentEntity, ?, ?> getGeneratedIdPropertyType() {

        return null;
    }

    @Override
    public String getName() {

        return null;
    }

    @Override
    public ParentEntity getOriginalStates(ParentEntity entity) {

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

    @Override
    public VersionPropertyType<Object, ParentEntity, ?, ?> getVersionPropertyType() {

        return null;
    }

    @Override
    public ParentEntity newEntity() {

        return null;
    }

    @Override
    public void preDelete(ParentEntity entity,
            PreDeleteContext<ParentEntity> context) {
    }

    @Override
    public void preInsert(ParentEntity entity,
            PreInsertContext<ParentEntity> context) {
    }

    @Override
    public void preUpdate(ParentEntity entity,
            PreUpdateContext<ParentEntity> context) {
    }

    @Override
    public void postDelete(ParentEntity entity,
            PostDeleteContext<ParentEntity> context) {
    }

    @Override
    public void postInsert(ParentEntity entity,
            PostInsertContext<ParentEntity> context) {
    }

    @Override
    public void postUpdate(ParentEntity entity,
            PostUpdateContext<ParentEntity> context) {
    }

    @Override
    public List<EntityPropertyType<ParentEntity, ?>> getIdPropertyTypes() {
        return null;
    }

    @Override
    public String getQualifiedTableName() {
        return null;
    }

    @Override
    public NamingType getNamingType() {
        return null;
    }

    public static _ParentEntity getSingletonInternal() {
        return null;
    }

    @Override
    public boolean isImmutable() {
        return false;
    }

    @Override
    public ParentEntity newEntity(Map<String, Object> args) {
        return null;
    }

}
