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
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.jdbc.util.MetaTypeUtil;
import org.seasar.doma.internal.message.Message;
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.internal.util.MethodUtil;

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
     *             ドメインクラスに {@link Domain} もしくは {@link EnumDomain} が注釈されていない場合
     * @throws DomainTypeNotFoundException
     *             ドメインクラスに対応するメタクラスが見つからない場合
     */
    public static <V, D> DomainType<V, D> getDomainType(Class<D> domainClass) {
        if (domainClass == null) {
            throw new DomaNullPointerException("domainClass");
        }
        if (!domainClass.isAnnotationPresent(Domain.class)
                && !domainClass.isAnnotationPresent(EnumDomain.class)) {
            throw new DomaIllegalArgumentException("domainClass",
                    Message.DOMA2205.getMessage(domainClass.getName()));
        }
        String domainTypeClassName = MetaTypeUtil.getMetaTypeName(domainClass
                .getName());
        try {
            Class<D> clazz = ClassUtil.forName(domainTypeClassName);
            Method method = ClassUtil.getMethod(clazz, "getSingletonInternal");
            return MethodUtil.invoke(method, null);
        } catch (WrapException e) {
            throw new DomainTypeNotFoundException(e.getCause(),
                    domainClass.getName(), domainTypeClassName);
        }
    }
}
