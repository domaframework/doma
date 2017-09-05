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
package org.seasar.doma.jdbc.holder;

import java.util.Optional;

import org.seasar.doma.internal.jdbc.scalar.Scalar;

/**
 * A description for a value holder class.
 * <p>
 * The implementation class is not required to be thread safe.
 * 
 * @param <BASIC>
 *            the basic type
 * @param <HOLDER>
 *            the holder type
 */
public interface HolderDesc<BASIC, HOLDER> {

    /**
     * Returns the basic class.
     * <p>
     * This method may return a primitive type, so the return type is not
     * {@code Class<BASIC>} but {@code Class<?>}.
     * 
     * @return the basic class
     */
    Class<?> getBasicClass();

    /**
     * Returns the holder class.
     * 
     * @return the holder class
     */
    Class<HOLDER> getHolderClass();

    /**
     * Create a scalar.
     * 
     * @return the scalar
     */
    Scalar<BASIC, HOLDER> createScalar();

    /**
     * Create a scalar that has the initial value.
     * 
     * @param value
     *            the initial value
     * @return the scalar
     */
    Scalar<BASIC, HOLDER> createScalar(HOLDER value);

    /**
     * Create an optional scalar.
     * 
     * @return the optional scalar
     */
    Scalar<BASIC, Optional<HOLDER>> createOptionalScalar();

    /**
     * Create an optional scalar that has the initial value.
     * 
     * @param value
     *            the initial value
     * @return the optional scalar
     */
    Scalar<BASIC, Optional<HOLDER>> createOptionalScalar(HOLDER value);
}
