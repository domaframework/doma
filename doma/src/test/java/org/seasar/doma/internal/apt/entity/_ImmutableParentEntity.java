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

public class _ImmutableParentEntity extends
        AbstractEntityType<ImmutableParentEntity> {

    public BasicPropertyType<Object, ImmutableParentEntity, Integer, Object> $aaa = new BasicPropertyType<Object, ImmutableParentEntity, Integer, Object>(
            ImmutableParentEntity.class, Integer.class, IntegerWrapper.class,
            null, null, "aaa", "AAA", true, true);

    public BasicPropertyType<Object, ImmutableParentEntity, Integer, Object> $bbb = new BasicPropertyType<Object, ImmutableParentEntity, Integer, Object>(
            ImmutableParentEntity.class, Integer.class, IntegerWrapper.class,
            null, null, "bbb", "BBB", true, true);

    private _ImmutableParentEntity() {
    }

    @Override
    public void saveCurrentStates(ImmutableParentEntity entity) {
    }

    @Override
    public String getCatalogName() {

        return null;
    }

    @Override
    public Class<ImmutableParentEntity> getEntityClass() {

        return null;
    }

    @Override
    public EntityPropertyType<ImmutableParentEntity, ?> getEntityPropertyType(
            String name) {

        return null;
    }

    @Override
    public List<EntityPropertyType<ImmutableParentEntity, ?>> getEntityPropertyTypes() {

        return null;
    }

    @Override
    public GeneratedIdPropertyType<Object, ImmutableParentEntity, ?, ?> getGeneratedIdPropertyType() {

        return null;
    }

    @Override
    public String getName() {

        return null;
    }

    @Override
    public ImmutableParentEntity getOriginalStates(ImmutableParentEntity entity) {

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
    public VersionPropertyType<Object, ImmutableParentEntity, ?, ?> getVersionPropertyType() {

        return null;
    }

    @Override
    public ImmutableParentEntity newEntity() {

        return null;
    }

    @Override
    public void preDelete(ImmutableParentEntity entity,
            PreDeleteContext<ImmutableParentEntity> context) {
    }

    @Override
    public void preInsert(ImmutableParentEntity entity,
            PreInsertContext<ImmutableParentEntity> context) {
    }

    @Override
    public void preUpdate(ImmutableParentEntity entity,
            PreUpdateContext<ImmutableParentEntity> context) {
    }

    @Override
    public void postDelete(ImmutableParentEntity entity,
            PostDeleteContext<ImmutableParentEntity> context) {
    }

    @Override
    public void postInsert(ImmutableParentEntity entity,
            PostInsertContext<ImmutableParentEntity> context) {
    }

    @Override
    public void postUpdate(ImmutableParentEntity entity,
            PostUpdateContext<ImmutableParentEntity> context) {
    }

    @Override
    public List<EntityPropertyType<ImmutableParentEntity, ?>> getIdPropertyTypes() {
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

    public static _ImmutableParentEntity getSingletonInternal() {
        return null;
    }

    @Override
    public boolean isImmutable() {
        return false;
    }

    @Override
    public ImmutableParentEntity newEntity(Map<String, Object> args) {
        return null;
    }

}
