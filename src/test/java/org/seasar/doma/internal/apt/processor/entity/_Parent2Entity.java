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

import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
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
import org.seasar.doma.wrapper.IntegerWrapper;

public class _Parent2Entity extends AbstractEntityDesc<Parent2Entity> {

    private final NamingType __namingType = NamingType.UPPER_CASE;

    public DefaultPropertyDesc<Parent2Entity, Integer, Integer> $aaa = new DefaultPropertyDesc<>(
            Parent2Entity.class, () -> new BasicScalar<>(new IntegerWrapper(), false), "aaa", "AAA",
            __namingType, true, true, false);

    public DefaultPropertyDesc<Parent2Entity, Integer, Integer> $bbb = new DefaultPropertyDesc<>(
            Parent2Entity.class, () -> new BasicScalar<>(new IntegerWrapper(), false), "bbb", "BBB",
            __namingType, true, true, false);

    private _Parent2Entity() {
    }

    @Override
    public void saveCurrentStates(Parent2Entity entity) {
    }

    @Override
    public String getCatalogName() {

        return null;
    }

    @Override
    public Class<Parent2Entity> getEntityClass() {

        return null;
    }

    @Override
    public EntityPropertyDesc<Parent2Entity, ?> getEntityPropertyDesc(String name) {

        return null;
    }

    @Override
    public List<EntityPropertyDesc<Parent2Entity, ?>> getEntityPropertyDescs() {

        return null;
    }

    @Override
    public GeneratedIdPropertyDesc<Parent2Entity, ?, ?> getGeneratedIdPropertyDesc() {

        return null;
    }

    @Override
    public String getName() {

        return null;
    }

    @Override
    public Parent2Entity getOriginalStates(Parent2Entity entity) {

        return null;
    }

    @Override
    public String getSchemaName() {

        return null;
    }

    @Override
    public String getTableName(BiFunction<NamingType, String, String> namingFunction) {

        return null;
    }

    @Override
    public VersionPropertyDesc<Parent2Entity, ?, ?> getVersionPropertyDesc() {

        return null;
    }

    @Override
    public void preDelete(Parent2Entity entity, PreDeleteContext<Parent2Entity> context) {
    }

    @Override
    public void preInsert(Parent2Entity entity, PreInsertContext<Parent2Entity> context) {
    }

    @Override
    public void preUpdate(Parent2Entity entity, PreUpdateContext<Parent2Entity> context) {
    }

    @Override
    public void postDelete(Parent2Entity entity, PostDeleteContext<Parent2Entity> context) {
    }

    @Override
    public void postInsert(Parent2Entity entity, PostInsertContext<Parent2Entity> context) {
    }

    @Override
    public void postUpdate(Parent2Entity entity, PostUpdateContext<Parent2Entity> context) {
    }

    @Override
    public List<EntityPropertyDesc<Parent2Entity, ?>> getIdPropertyDescs() {
        return null;
    }

    @Override
    public NamingType getNamingType() {
        return null;
    }

    public static _Parent2Entity getSingletonInternal() {
        return null;
    }

    @Override
    public boolean isImmutable() {
        return false;
    }

    @Override
    public Parent2Entity newEntity(Map<String, Property<Parent2Entity, ?>> args) {
        return null;
    }

    @Override
    public boolean isQuoteRequired() {
        return false;
    }
}
