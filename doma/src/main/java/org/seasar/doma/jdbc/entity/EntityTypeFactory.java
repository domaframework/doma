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
package org.seasar.doma.jdbc.entity;

import java.lang.reflect.Method;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.Entity;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.jdbc.util.MetaTypeUtil;
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.internal.util.MethodUtil;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.DefaultClassHelper;
import org.seasar.doma.message.Message;

/**
 * {@link EntityType} のファクトリクラスです。
 * 
 * @author taedium
 * @since 1.8.0
 */
public final class EntityTypeFactory {

    /**
     * {@link EntityType} のインスタンスを生成します。
     * 
     * @param <E>
     *            エンティティの型
     * @param entityClass
     *            エンティティクラス
     * @return {@link EntityType} のインスタンス
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     * @throws DomaIllegalArgumentException
     *             エンティティクラスに {@link Entity} が注釈されていない場合
     * @throws EntityTypeNotFoundException
     *             エンティティクラスに対応するメタクラスが見つからない場合
     * @deprecated {@link EntityTypeFactory#getEntityType(Class, ClassHelper)}
     *             を使ってください。
     */
    @Deprecated
    public static <E> EntityType<E> getEntityType(Class<E> entityClass) {
        return getEntityType(entityClass, new DefaultClassHelper());
    }

    /**
     * {@link EntityType} のインスタンスを生成します。
     * 
     * @param <E>
     *            エンティティの型
     * @param entityClass
     *            エンティティクラス
     * @param classHelper
     *            クラスヘルパー
     * @return {@link EntityType} のインスタンス
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     * @throws DomaIllegalArgumentException
     *             エンティティクラスに {@link Entity} が注釈されていない場合
     * @throws EntityTypeNotFoundException
     *             エンティティクラスに対応するメタクラスが見つからない場合
     * @since 1.27.0
     */
    public static <E> EntityType<E> getEntityType(Class<E> entityClass,
            ClassHelper classHelper) {
        if (entityClass == null) {
            throw new DomaNullPointerException("entityClass");
        }
        if (classHelper == null) {
            throw new DomaNullPointerException("classHelper");
        }
        if (!entityClass.isAnnotationPresent(Entity.class)) {
            throw new DomaIllegalArgumentException("entityClass",
                    Message.DOMA2206.getMessage("entityClass"));
        }
        String entityTypeClassName = MetaTypeUtil.getMetaTypeName(entityClass
                .getName());
        try {
            Class<E> clazz = classHelper.forName(entityTypeClassName);
            Method method = ClassUtil.getMethod(clazz, "getSingletonInternal");
            return MethodUtil.invoke(method, null);
        } catch (WrapException e) {
            throw new EntityTypeNotFoundException(e.getCause(),
                    entityClass.getName(), entityTypeClassName);
        } catch (Exception e) {
            throw new EntityTypeNotFoundException(e.getCause(),
                    entityClass.getName(), entityTypeClassName);
        }
    }
}
