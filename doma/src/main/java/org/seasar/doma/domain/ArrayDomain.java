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
package org.seasar.doma.domain;

import java.sql.Array;
import java.sql.SQLException;

import org.seasar.doma.DomaNullPointerException;

/**
 * {@link Array} を値の型とするドメインのための骨格実装です。
 * 
 * @author taedium
 * 
 * @param <D>
 *            ドメインの型
 * @param <E>
 *            配列の要素の型
 */
public abstract class ArrayDomain<D extends ArrayDomain<D, E>, E>
        extends AbstractDomain<Array, ArrayDomain<D, E>> {

    /**
     * デフォルトの値でインスタンス化します。
     */
    protected ArrayDomain() {
        this(null);
    }

    /**
     * 値を指定してインスタンス化します。
     * 
     * @param value
     *            値
     */
    protected ArrayDomain(Array value) {
        super(Array.class, value);
    }

    /**
     * 配列を返します。
     * <p>
     * {@link Array#getArray()} を 要素の型の配列にキャストして返します。
     * 
     * @return 配列
     * @throws DomainIllegalStateException
     *             SQL例外が発生した場合
     */
    @SuppressWarnings("unchecked")
    public E[] getArray() {
        try {
            return (E[]) value.getArray();
        } catch (SQLException e) {
            throw new DomainIllegalStateException(e);
        }
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            DomainVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        if (ArrayDomainVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            ArrayDomainVisitor<R, P, TH> v = ArrayDomainVisitor.class
                    .cast(visitor);
            return v.visitAbstractArrayDomain(this, p);
        }
        return visitor.visitUnknownDomain(this, p);
    }

}
