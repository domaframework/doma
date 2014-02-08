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

import java.math.BigDecimal;
import java.math.BigInteger;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class BigDecimalWrapperTest extends TestCase {

    /**
     * 
     */
    public void testSetBigDecimalAsNumber() {
        Number greaterThanLongMaxValue = new BigDecimal(Long.MAX_VALUE)
                .add(BigDecimal.ONE);
        BigDecimalWrapper wrapper = new BigDecimalWrapper();
        wrapper.set(greaterThanLongMaxValue);
        assertEquals(greaterThanLongMaxValue, wrapper.get());
    }

    /**
     * 
     */
    public void testSetBigInteger() {
        BigInteger greaterThanLongMaxValue = new BigDecimal(Long.MAX_VALUE)
                .add(BigDecimal.ONE).toBigInteger();
        BigDecimalWrapper wrapper = new BigDecimalWrapper();
        wrapper.set(greaterThanLongMaxValue);
        assertEquals(new BigDecimal(greaterThanLongMaxValue), wrapper.get());
    }

    /**
     * 
     */
    public void testIncrement() {
        BigDecimalWrapper wrapper = new BigDecimalWrapper(new BigDecimal(10));
        wrapper.increment();
        assertEquals(new BigDecimal(11), wrapper.get());
    }

    /**
     * 
     */
    public void testDecrement() {
        BigDecimalWrapper wrapper = new BigDecimalWrapper(new BigDecimal(10));
        wrapper.decrement();
        assertEquals(new BigDecimal(9), wrapper.get());
    }
}
