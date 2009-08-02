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
package org.seasar.doma.entity;

import junit.framework.TestCase;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.domain.StringDomain;

import example.entity.Emp;
import example.entity.Emp_;

/**
 * @author taedium
 * 
 */
public class BuiltinEntityUtilDelegateTest extends TestCase {

    private final BuiltinEntityUtilDelegate delegate = new BuiltinEntityUtilDelegate();

    public void testGetDomain() throws Exception {
        Emp emp = new Emp_();
        IntegerDomain id = delegate.getDomain(emp, IntegerDomain.class, "id");
        assertNotNull(id);
        StringDomain id2 = delegate.getDomain(emp, StringDomain.class, "id");
        assertNull(id2);
        StringDomain nonexistent = delegate.getDomain(emp, StringDomain.class,
                "nonexistent");
        assertNull(nonexistent);
    }

    public void testNonEntityArgumentException() throws Exception {
        try {
            delegate.getDomain("aaa", IntegerDomain.class, "id");
            fail();
        } catch (DomaIllegalArgumentException expected) {
        }
    }

}
