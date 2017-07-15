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
package org.seasar.doma.internal.apt.processor.entity;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import org.seasar.doma.jdbc.entity.AbstractEntityDesc;
import org.seasar.doma.jdbc.entity.DefaultPropertyDesc;
import org.seasar.doma.jdbc.entity.EntityPropertyDesc;
import org.seasar.doma.jdbc.entity.GeneratedIdPropertyDesc;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.entity.PostUpdateContext;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.jdbc.entity.VersionPropertyDesc;

public class _OriginalStatesParentEntity extends
        AbstractEntityDesc<OriginalStatesParentEntity> {

    private final NamingType __namingType = NamingType.UPPER_CASE;

    public DefaultPropertyDesc<OriginalStatesParentEntity, Integer, Integer> $aaa = new DefaultPropertyDesc<>(
            OriginalStatesParentEntity.class,
            () -> new org.seasar.doma.wrapper.IntegerWrapper(), null,
            "aaa", "AAA", __namingType, true, true, false);

    public DefaultPropertyDesc<OriginalStatesParentEntity, Integer, Integer> $bbb = new DefaultPropertyDesc<>(
            OriginalStatesParentEntity.class,
            () -> new org.seasar.doma.wrapper.IntegerWrapper(), null,
            "bbb", "BBB", __namingType, true, true, false);

    private _OriginalStatesParentEntity() {
    }

    @Override
    public void saveCurrentStates(OriginalStatesParentEntity entity) {
    }

    @Override
    public String getCatalogName() {

        return null;
    }

    @Override
    public Class<OriginalStatesParentEntity> getEntityClass() {

        return null;
    }

    @Override
    public EntityPropertyDesc<OriginalStatesParentEntity, ?> getEntityPropertyDesc(
            String name) {

        return null;
    }

    @Override
    public List<EntityPropertyDesc<OriginalStatesParentEntity, ?>> getEntityPropertyDescs() {

        return null;
    }

    @Override
    public GeneratedIdPropertyDesc<OriginalStatesParentEntity, ?, ?> getGeneratedIdPropertyDesc() {

        return null;
    }

    @Override
    public String getName() {

        return null;
    }

    @Override
    public OriginalStatesParentEntity getOriginalStates(
            OriginalStatesParentEntity entity) {

        return null;
    }

    @Override
    public String getSchemaName() {

        return null;
    }

    @Override
    public String getTableName(
            BiFunction<NamingType, String, String> namingFunction) {

        return null;
    }

    @Override
    public VersionPropertyDesc<OriginalStatesParentEntity, ?, ?> getVersionPropertyDesc() {

        return null;
    }

    @Override
    public void preDelete(OriginalStatesParentEntity entity,
            PreDeleteContext<OriginalStatesParentEntity> context) {
    }

    @Override
    public void preInsert(OriginalStatesParentEntity entity,
            PreInsertContext<OriginalStatesParentEntity> context) {
    }

    @Override
    public void preUpdate(OriginalStatesParentEntity entity,
            PreUpdateContext<OriginalStatesParentEntity> context) {
    }

    @Override
    public void postDelete(OriginalStatesParentEntity entity,
            PostDeleteContext<OriginalStatesParentEntity> context) {
    }

    @Override
    public void postInsert(OriginalStatesParentEntity entity,
            PostInsertContext<OriginalStatesParentEntity> context) {
    }

    @Override
    public void postUpdate(OriginalStatesParentEntity entity,
            PostUpdateContext<OriginalStatesParentEntity> context) {
    }

    @Override
    public List<EntityPropertyDesc<OriginalStatesParentEntity, ?>> getIdPropertyDescs() {
        return null;
    }

    @Override
    public NamingType getNamingType() {
        return null;
    }

    public static _OriginalStatesParentEntity getSingletonInternal() {
        return null;
    }

    @Override
    public boolean isImmutable() {
        return false;
    }

    @Override
    public OriginalStatesParentEntity newEntity(
            Map<String, Property<OriginalStatesParentEntity, ?>> args) {
        return null;
    }

    @Override
    public boolean isQuoteRequired() {
        return false;
    }
}
