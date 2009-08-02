/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.doma.entity;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.domain.Domain;

/**
 * エンティティに関するユーティリティです。
 * 
 * @author taedium
 * 
 */
public final class EntityUtil {

    private static volatile EntityUtilDelegate delegate = new BuiltinEntityUtilDelegate();

    /**
     * 委譲先を設定します。
     * 
     * @param delegate
     *            委譲先
     * @throws DomaNullPointerException
     *             {@code delegate} が {@code null} の場合
     */
    public static void setDelegate(EntityUtilDelegate delegate)
            throws DomaNullPointerException {
        if (delegate == null) {
            throw new DomaNullPointerException("delegate");
        }
        EntityUtil.delegate = delegate;
    }

    /**
     * エンティティが保持するドメインをプロパティ名で返します。
     * 
     * @param <D>
     *            ドメインの型
     * @param entity
     *            エンティティ
     * @param domainClass
     *            ドメインのクラス
     * @param propertyName
     *            プロパティ名
     * @return ドメイン
     * @throws DomaNullPointerException
     *             引数のいずれかが {@code null} の場合
     * @throws DomaIllegalArgumentException
     *             {@code entity} がエンティティでない場合
     */
    public <D extends Domain<?, ?>> D getDomain(Object entity,
            Class<D> domainClass, String propertyName) {
        return delegate.getDomain(entity, domainClass, propertyName);
    }

}
