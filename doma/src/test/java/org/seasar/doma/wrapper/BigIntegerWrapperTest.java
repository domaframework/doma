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
public class BigIntegerWrapperTest extends TestCase {

    /**
     * 
     */
    public void testSetBigIntegerAsNumber() {
        Number greaterThanLongMaxValue = new BigDecimal(Long.MAX_VALUE).add(
                BigDecimal.ONE).toBigInteger();
        BigIntegerWrapper wrapper = new BigIntegerWrapper();
        wrapper.set(greaterThanLongMaxValue);
        assertEquals(greaterThanLongMaxValue, wrapper.get());
    }

    /**
     * 
     */
    public void testSetBigDecimal() {
        BigDecimal greaterThanLongMaxValue = new BigDecimal(Long.MAX_VALUE)
                .add(BigDecimal.ONE);
        BigIntegerWrapper wrapper = new BigIntegerWrapper();
        wrapper.set(greaterThanLongMaxValue);
        assertEquals(greaterThanLongMaxValue.toBigInteger(), wrapper.get());
    }

    /**
     * 
     */
    public void testIncrement() {
        BigIntegerWrapper wrapper = new BigIntegerWrapper(new BigInteger("10"));
        wrapper.increment();
        assertEquals(new BigInteger("11"), wrapper.get());
    }

    /**
     * 
     */
    public void testDecrement() {
        BigIntegerWrapper wrapper = new BigIntegerWrapper(new BigInteger("10"));
        wrapper.decrement();
        assertEquals(new BigInteger("9"), wrapper.get());
    }
}
