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
package org.seasar.doma.jdbc.domain;

import java.lang.reflect.Method;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.Domain;
import org.seasar.doma.EnumDomain;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.jdbc.util.MetaTypeUtil;
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.internal.util.MethodUtil;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.DefaultClassHelper;
import org.seasar.doma.message.Message;

/**
 * {@link DomainType} のファクトリクラスです。
 * 
 * @author taedium
 * @since 1.8.0
 */
@SuppressWarnings("deprecation")
public final class DomainTypeFactory {

    /**
     * {@link DomainType} のインスタンスを生成します。
     * 
     * @param <V>
     *            ドメインクラスが扱う値の型
     * @param <D>
     *            ドメインクラスの型
     * @param domainClass
     *            ドメインクラス
     * @return {@link DomainType} のインスタンス
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     * @throws DomaIllegalArgumentException
     *             ドメインクラスに {@link Domain} もしくは {@code EnumDomain} が注釈されていない場合
     * @throws DomainTypeNotFoundException
     *             ドメインクラスに対応するメタクラスが見つからない場合
     * @deprecated {@link DomainTypeFactory#getDomainType(Class, ClassHelper)}
     *             を使ってください。
     */
    @Deprecated
    public static <V, D> DomainType<V, D> getDomainType(Class<D> domainClass) {
        return getDomainType(domainClass, new DefaultClassHelper());
    }

    /**
     * {@link DomainType} のインスタンスを生成します。
     * 
     * @param <V>
     *            ドメインクラスが扱う値の型
     * @param <D>
     *            ドメインクラスの型
     * @param domainClass
     *            ドメインクラス
     * @param classHelper
     *            クラスヘルパー
     * @return {@link DomainType} のインスタンス
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     * @throws DomaIllegalArgumentException
     *             ドメインクラスに {@link Domain} もしくは {@code EnumDomain} が注釈されていない場合
     * @throws DomainTypeNotFoundException
     *             ドメインクラスに対応するメタクラスが見つからない場合
     * @since 1.27.0
     */
    public static <V, D> DomainType<V, D> getDomainType(Class<D> domainClass,
            ClassHelper classHelper) {
        if (domainClass == null) {
            throw new DomaNullPointerException("domainClass");
        }
        if (classHelper == null) {
            throw new DomaNullPointerException("classHelper");
        }
        if (!domainClass.isAnnotationPresent(Domain.class)
                && !domainClass.isAnnotationPresent(EnumDomain.class)) {
            throw new DomaIllegalArgumentException("domainClass",
                    Message.DOMA2205.getMessage(domainClass.getName()));
        }
        String domainTypeClassName = MetaTypeUtil.getMetaTypeName(domainClass
                .getName());
        try {
            Class<D> clazz = classHelper.forName(domainTypeClassName);
            Method method = ClassUtil.getMethod(clazz, "getSingletonInternal");
            return MethodUtil.invoke(method, null);
        } catch (WrapException e) {
            throw new DomainTypeNotFoundException(e.getCause(),
                    domainClass.getName(), domainTypeClassName);
        } catch (Exception e) {
            throw new DomainTypeNotFoundException(e, domainClass.getName(),
                    domainTypeClassName);
        }
    }

    /**
     * {@link DomainType} のインスタンスを生成します。
     * 
     * @param <V>
     *            ドメインクラスが扱う値の型
     * @param <D>
     *            ドメインクラスの型
     * @param domainClass
     *            ドメインクラス
     * @return {@link DomainType} のインスタンス、存在しない場合 {@code null}
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     * @since 1.25.0
     * @deprecated {@link DomainTypeFactory#getExternalDomainType(Class, ClassHelper)}
     *             を使ってください。
     */
    @Deprecated
    public static <V, D> DomainType<V, D> getExternalDomainType(
            Class<D> domainClass) {
        return getExternalDomainType(domainClass, new DefaultClassHelper());
    }

    /**
     * {@link DomainType} のインスタンスを生成します。
     * 
     * @param <V>
     *            ドメインクラスが扱う値の型
     * @param <D>
     *            ドメインクラスの型
     * @param domainClass
     *            ドメインクラス
     * @param classHelper
     *            クラスヘルパー
     * @return {@link DomainType} のインスタンス、存在しない場合 {@code null}
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     * @since 1.27.0
     */
    public static <V, D> DomainType<V, D> getExternalDomainType(
            Class<D> domainClass, ClassHelper classHelper) {
        if (domainClass == null) {
            throw new DomaNullPointerException("domainClass");
        }
        if (classHelper == null) {
            throw new DomaNullPointerException("classHelper");
        }
        String domainTypeClassName = Constants.EXTERNAL_DOMAIN_METATYPE_ROOT_PACKAGE
                + "." + MetaTypeUtil.getMetaTypeName(domainClass.getName());
        try {
            Class<D> clazz = classHelper.forName(domainTypeClassName);
            Method method = ClassUtil.getMethod(clazz, "getSingletonInternal");
            return MethodUtil.invoke(method, null);
        } catch (WrapException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
