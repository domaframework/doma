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

import org.seasar.doma.jdbc.entity.EmbeddableType;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.Property;

public class _UserAddress implements EmbeddableType<UserAddress> {

    private static _UserAddress singleton = new _UserAddress();

    @Override
    public <ENTITY> List<EntityPropertyType<ENTITY, ?>> getEmbeddablePropertyTypes(
            String embeddedPropertyName, Class<ENTITY> entityClass,
            NamingType namingType) {
        return java.util.Arrays
                .asList(new org.seasar.doma.jdbc.entity.DefaultPropertyType<ENTITY, java.lang.String, Object>(
                        entityClass, java.lang.String.class,
                        () -> new org.seasar.doma.wrapper.StringWrapper(),
                        null, embeddedPropertyName + ".city", "",
                        namingType, true, true, false),
                        new org.seasar.doma.jdbc.entity.DefaultPropertyType<ENTITY, java.lang.String, Object>(
                                entityClass,
                                java.lang.String.class,
                                () -> new org.seasar.doma.wrapper.StringWrapper(),
                                null, embeddedPropertyName + ".street",
                                "", namingType, true, true, false));
    }

    @Override
    public <ENTITY> UserAddress newEmbeddable(String embeddedPropertyName,
            Map<String, Property<ENTITY, ?>> __args) {
        return new UserAddress(
                (java.lang.String) (__args.get(embeddedPropertyName + ".city") != null ? __args
                        .get(embeddedPropertyName + ".city").get() : null),
                (java.lang.String) (__args
                        .get(embeddedPropertyName + ".street") != null ? __args
                        .get(embeddedPropertyName + ".street").get() : null));
    }

    public static _UserAddress getSingletonInternal() {
        return singleton;
    }

}
