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
package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/**
 * {@link Enum} のラッパーです。
 * 
 * @author taedium
 * 
 * @param <E>
 *            {@link Enum} の型
 */
public class EnumWrapper<E extends Enum<E>> extends AbstractWrapper<E> {

    /** {@link Enum} のクラス */
    protected final Class<E> enumClass;

    /**
     * インスタンスを構築します。
     * 
     * @param enumClass
     *            {@link Enum} のクラス
     * @throws DomaNullPointerException
     *             {@link Enum} のクラスが {@code null} の場合
     */
    public EnumWrapper(Class<E> enumClass) {
        this(enumClass, null);
    }

    /**
     * 値を指定してインスタンスを構築します。
     * 
     * @param enumClass
     *            {@link Enum} のクラス
     * @param value
     *            値
     * @throws DomaNullPointerException
     *             {@link Enum} のクラスが {@code null} の場合
     */
    public EnumWrapper(Class<E> enumClass, E value) {
        super(value);
        if (enumClass == null) {
            throw new DomaNullPointerException("enumClass");
        }
        this.enumClass = enumClass;
    }

    /**
     * {@link Enum} のクラスを返します。
     * 
     * @return {@link Enum} のクラス
     */
    public Class<E> getEnumClass() {
        return enumClass;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            WrapperVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        if (visitor instanceof EnumWrapperVisitor<?, ?, ?>) {
            EnumWrapperVisitor<R, P, TH> v = (EnumWrapperVisitor<R, P, TH>) visitor;
            return v.visitEnumWrapper(this, p);
        }
        return visitor.visitUnknownWrapper(this, p);
    }
}
