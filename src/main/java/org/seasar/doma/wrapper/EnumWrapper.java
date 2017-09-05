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
 * A wrapper for the {@link Enum} class.
 * 
 * @param <E>
 *            The enum type subclass
 */
public class EnumWrapper<E extends Enum<E>> extends AbstractWrapper<E> {

    /**
     * Creates an instance.
     * 
     * @param enumClass
     *            the {@link Enum} class
     * @throws DomaNullPointerException
     *             if the {@code enumClass} is {@code null}
     */
    public EnumWrapper(Class<E> enumClass) {
        this(enumClass, null);
    }

    /**
     * Creates an instance with a value.
     * 
     * @param enumClass
     *            the {@link Enum} class
     * @param value
     *            the enum value
     * @throws DomaNullPointerException
     *             if the {@code enumClass} is {@code null}
     */
    public EnumWrapper(Class<E> enumClass, E value) {
        super(enumClass, value);
        if (enumClass == null) {
            throw new DomaNullPointerException("enumClass");
        }
    }

    @Override
    public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
            throws TH {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        return visitor.visitEnumWrapper(this, p, q);
    }
}
