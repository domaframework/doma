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
package org.seasar.doma.internal.jdbc.sql;

import java.util.Optional;

import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.jdbc.holder.HolderDesc;
import org.seasar.doma.jdbc.holder.HolderDescFactory;

import example.holder.PhoneNumber;
import junit.framework.TestCase;

/**
 * @author nakamura-to
 *
 */
public class ScalarInOutParameterTest extends TestCase {

    public void testGetHolderClass() throws Exception {
        HolderDesc<String, PhoneNumber> holderDesc = HolderDescFactory
                .getHolderDesc(PhoneNumber.class, new ClassHelper() {
                });
        Scalar<String, PhoneNumber> scalar = holderDesc.createScalar();
        Reference<PhoneNumber> ref = new Reference<>();
        ScalarInOutParameter<String, PhoneNumber> parameter = new ScalarInOutParameter<>(
                () -> scalar, ref);
        Optional<Class<?>> optional = parameter.getHolderClass();
        assertEquals(PhoneNumber.class, optional.get());
    }

    public void testGetHolderClass_optional() throws Exception {
        HolderDesc<String, PhoneNumber> holderDesc = HolderDescFactory
                .getHolderDesc(PhoneNumber.class, new ClassHelper() {
                });
        Scalar<String, Optional<PhoneNumber>> scalar = holderDesc.createOptionalScalar();
        Reference<Optional<PhoneNumber>> ref = new Reference<>();
        ScalarInOutParameter<String, Optional<PhoneNumber>> parameter = new ScalarInOutParameter<>(
                () -> scalar, ref);
        Optional<Class<?>> optional = parameter.getHolderClass();
        assertEquals(PhoneNumber.class, optional.get());
    }
}
