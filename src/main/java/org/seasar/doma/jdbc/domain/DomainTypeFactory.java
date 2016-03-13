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
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.Conventions;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.internal.util.MethodUtil;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.message.Message;

/**
 * {@link DomainType} のファクトリクラスです。
 * 
 * @author taedium
 * @since 1.8.0
 */
public final class DomainTypeFactory {

    /**
     * {@link DomainType} のインスタンスを生成します。
     * 
     * @param <BASIC>
     *            基本型
     * @param <DOMAIN>
     *            ドメイン型
     * @param domainClass
     *            ドメインクラス
     * @return {@link DomainType} のインスタンス
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     * @throws DomaIllegalArgumentException
     *             ドメインクラスに {@link Domain} もしくは {@code EnumDomain} が注釈されていない場合
     * @throws DomainTypeNotFoundException
     *             ドメインクラスに対応するメタクラスが見つからない場合
     * @since 2.0.0
     */
    public static <BASIC, DOMAIN> DomainType<BASIC, DOMAIN> getDomainType(
            Class<DOMAIN> domainClass) {
        return getDomainType(domainClass, new ClassHelper() {
        });
    }

    /**
     * {@link ClassHelper} を使って {@link DomainType} のインスタンスを生成します。
     * 
     * @param <BASIC>
     *            基本型
     * @param <DOMAIN>
     *            ドメイン型
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
    public static <BASIC, DOMAIN> DomainType<BASIC, DOMAIN> getDomainType(
            Class<DOMAIN> domainClass, ClassHelper classHelper) {
        if (domainClass == null) {
            throw new DomaNullPointerException("domainClass");
        }
        if (classHelper == null) {
            throw new DomaNullPointerException("classHelper");
        }
        if (!domainClass.isAnnotationPresent(Domain.class)) {
            throw new DomaIllegalArgumentException("domainClass",
                    Message.DOMA2205.getMessage(domainClass.getName()));
        }
        String domainTypeClassName = Conventions.toFullMetaName(domainClass
                .getName());
        try {
            Class<DOMAIN> clazz = classHelper.forName(domainTypeClassName);
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
     * @param <BASIC>
     *            基本型
     * @param <DOMAIN>
     *            ドメイン型
     * @param domainClass
     *            ドメインクラス
     * @return {@link DomainType} のインスタンス、存在しない場合 {@code null}
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     * @since 2.0.0
     */
    public static <BASIC, DOMAIN> DomainType<BASIC, DOMAIN> getExternalDomainType(
            Class<DOMAIN> domainClass) {
        return getExternalDomainType(domainClass, new ClassHelper() {
        });
    }

    /**
     * {@link ClassHelper} を使って {@link DomainType} のインスタンスを生成します。
     * 
     * @param <BASIC>
     *            基本型
     * @param <DOMAIN>
     *            ドメイン型
     * @param domainClass
     *            ドメインクラス
     * @param classHelper
     *            クラスヘルパー
     * @return {@link DomainType} のインスタンス、存在しない場合 {@code null}
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     * @since 1.27.0
     */
    public static <BASIC, DOMAIN> DomainType<BASIC, DOMAIN> getExternalDomainType(
            Class<DOMAIN> domainClass, ClassHelper classHelper) {
        if (domainClass == null) {
            throw new DomaNullPointerException("domainClass");
        }
        if (classHelper == null) {
            throw new DomaNullPointerException("classHelper");
        }
        String domainTypeClassName = Constants.EXTERNAL_DOMAIN_METATYPE_ROOT_PACKAGE
                + "." + Conventions.toFullMetaName(domainClass.getName());
        try {
            Class<DOMAIN> clazz = classHelper.forName(domainTypeClassName);
            Method method = ClassUtil.getMethod(clazz, "getSingletonInternal");
            return MethodUtil.invoke(method, null);
        } catch (WrapException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
