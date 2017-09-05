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
 * A wrapper for a basic type.
 * <p>
 * The implementation instance is not required to be thread safe.
 * 
 * @param <BASIC>
 *            The basic type
 */
public interface Wrapper<BASIC> {

    /**
     * Returns the value.
     * <p>
     * The value may be {@code null}.
     * 
     * @return the value.
     */
    BASIC get();

    /**
     * Sets the value.
     * 
     * @param value
     *            the value
     */
    void set(BASIC value);

    /**
     * Return a copy of the value.
     * 
     * @return a copy of the value
     */
    BASIC getCopy();

    /**
     * Returns the default value.
     * 
     * @return the boxed default value of a primitive type if this is a wrapper
     *         for a boxed type, else {@code null}
     */
    BASIC getDefault();

    /**
     * Whether this object has an equal value to the other one.
     * 
     * @param other
     *            the other object
     * @return {@code true} if this object has an equal value to the other one.
     */
    boolean hasEqualValue(Object other);

    /**
     * Returns the class of the basic type.
     * 
     * @return the class of the basic type
     */
    Class<BASIC> getBasicClass();

    /**
     * Accept a visitor.
     * 
     * @param <R>
     *            The result
     * @param <P>
     *            The first parameter type
     * @param <Q>
     *            The second parameter type
     * @param <TH>
     *            The error or exception type
     * @param visitor
     *            the visitor
     * @param p
     *            the first parameter
     * @param q
     *            the second parameter
     * @return the result
     * @throws TH
     *             the error or exception type
     * @throws DomaNullPointerException
     *             if {@code visitor} is {@code null}
     */
    <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
            throws TH;

}
