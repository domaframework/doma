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
package org.seasar.doma.jdbc.domain;

import junit.framework.TestCase;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.DefaultClassHelper;

import example.domain.PhoneNumber;

/**
 * @author taedium
 * 
 */
public class DomainTypeFactoryTest extends TestCase {

    public void testGetDomainType() throws Exception {
        DomainType<String, PhoneNumber> type = DomainTypeFactory.getDomainType(
                PhoneNumber.class, new DefaultClassHelper());
        assertNotNull(type);
    }

    public void testGetDomainType_DomaIllegalArgumentException()
            throws Exception {
        try {
            DomainTypeFactory.getDomainType(Object.class,
                    new DefaultClassHelper());
            fail();
        } catch (DomaIllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void testGetDomainType_DomainTypeNotFoundException()
            throws Exception {
        try {
            DomainTypeFactory.getDomainType(Money.class,
                    new DefaultClassHelper());
            fail();
        } catch (DomainTypeNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
