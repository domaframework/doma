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

public class _Parent3Entity extends AbstractEntityType<Parent3Entity> {

    public BasicPropertyType<Object, Parent3Entity, Integer, Object> $aaa = new BasicPropertyType<Object, Parent3Entity, Integer, Object>(
            Parent3Entity.class, Integer.class, IntegerWrapper.class, null,
            null, "aaa", "AAA", true, true);

    public BasicPropertyType<Object, Parent3Entity, Integer, Object> $bbb = new BasicPropertyType<Object, Parent3Entity, Integer, Object>(
            Parent3Entity.class, Integer.class, IntegerWrapper.class, null,
            null, "bbb", "BBB", true, true);

    private _Parent3Entity() {
    }

    @Override
    public void saveCurrentStates(Parent3Entity entity) {
    }

    @Override
    public String getCatalogName() {

        return null;
    }

    @Override
    public Class<Parent3Entity> getEntityClass() {

        return null;
    }

    @Override
    public EntityPropertyType<Parent3Entity, ?> getEntityPropertyType(
            String name) {

        return null;
    }

    @Override
    public List<EntityPropertyType<Parent3Entity, ?>> getEntityPropertyTypes() {

        return null;
    }

    @Override
    public GeneratedIdPropertyType<Object, Parent3Entity, ?, ?> getGeneratedIdPropertyType() {

        return null;
    }

    @Override
    public String getName() {

        return null;
    }

    @Override
    public Parent3Entity getOriginalStates(Parent3Entity entity) {

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
    public VersionPropertyType<Object, Parent3Entity, ?, ?> getVersionPropertyType() {

        return null;
    }

    @Override
    public Parent3Entity newEntity() {

        return null;
    }

    @Override
    public void preDelete(Parent3Entity entity,
            PreDeleteContext<Parent3Entity> context) {
    }

    @Override
    public void preInsert(Parent3Entity entity,
            PreInsertContext<Parent3Entity> context) {
    }

    @Override
    public void preUpdate(Parent3Entity entity,
            PreUpdateContext<Parent3Entity> context) {
    }

    @Override
    public void postDelete(Parent3Entity entity,
            PostDeleteContext<Parent3Entity> context) {
    }

    @Override
    public void postInsert(Parent3Entity entity,
            PostInsertContext<Parent3Entity> context) {
    }

    @Override
    public void postUpdate(Parent3Entity entity,
            PostUpdateContext<Parent3Entity> context) {
    }

    @Override
    public List<EntityPropertyType<Parent3Entity, ?>> getIdPropertyTypes() {
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

    public static _Parent3Entity getSingletonInternal() {
        return null;
    }

    @Override
    public boolean isImmutable() {
        return false;
    }

    @Override
    public Parent3Entity newEntity(Map<String, Object> args) {
        return null;
    }

}
