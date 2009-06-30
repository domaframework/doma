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

import org.seasar.doma.domain.DomainIncomparableException;
import org.seasar.doma.domain.DomainValueNullPointerException;
import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.message.MessageCode;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class DomainExceptionTest extends TestCase {

    public void testE1001() throws Exception {
        StringDomain domain = new StringDomain("aaa");
        try {
            domain.lt(new StringDomain());
            fail();
        } catch (DomainIncomparableException e) {
            System.out.println(e.getMessage());
            assertEquals(MessageCode.DOMA1001, e.getMessageCode());
        }
    }

    public void testE1002() throws Exception {
        StringDomain domain = new StringDomain();
        try {
            domain.isEmpty();
            fail();
        } catch (DomainValueNullPointerException e) {
            System.out.println(e.getMessage());
            assertEquals(MessageCode.DOMA1002, e.getMessageCode());
        }
    }

}
