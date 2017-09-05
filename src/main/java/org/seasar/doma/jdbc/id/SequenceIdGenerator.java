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
package org.seasar.doma.jdbc.id;

import org.seasar.doma.jdbc.JdbcException;

/**
 * A generator that uses a database SEQUENCE.
 */
public interface SequenceIdGenerator extends IdGenerator {

    /**
     * Sets the qualified name of the sequence.
     * 
     * @param qualifiedSequenceName
     *            the qualified name of the sequence
     */
    void setQualifiedSequenceName(String qualifiedSequenceName);

    /**
     * Sets the initial value.
     * 
     * @param initialValue
     *            the initial value
     */
    void setInitialValue(long initialValue);

    /**
     * Sets the allocation size.
     * 
     * @param allocationSize
     *            the allocation size
     */
    void setAllocationSize(long allocationSize);

    /**
     * Initializes this generator.
     * 
     * @throws JdbcException
     *             if the initialization is failed
     */
    void initialize();
}
