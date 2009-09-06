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

import java.sql.NClob;

import org.seasar.doma.DomaNullPointerException;

/**
 * {@link NClob} を値の型とするドメインの骨格実装です。
 * 
 * @author taedium
 * 
 * @param <D>
 *            ドメインの型
 */
public abstract class NClobDomain<D extends NClobDomain<D>>
        extends AbstractDomain<NClob, D> {

    /**
     * デフォルトの値でインスタンス化します。
     */
    protected NClobDomain() {
        this(null);
    }

    /**
     * 値を指定してインスタンス化します。
     * 
     * @param v
     *            値
     */
    protected NClobDomain(NClob v) {
        super(NClob.class, v);
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            DomainVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        if (NClobDomainVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            NClobDomainVisitor<R, P, TH> v = NClobDomainVisitor.class
                    .cast(visitor);
            return v.visitAbstractNClobDomain(this, p);
        }
        return visitor.visitUnknownDomain(this, p);
    }

}
