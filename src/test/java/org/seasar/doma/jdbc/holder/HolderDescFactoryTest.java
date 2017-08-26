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

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.ClassHelper;

import example.holder.PhoneNumber;
import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class HolderDescFactoryTest extends TestCase {

    private ClassHelper classHelper = new ClassHelper() {
    };

    public void testGetHolderDesc() throws Exception {
        HolderDesc<String, PhoneNumber> desc = HolderDescFactory.getHolderDesc(PhoneNumber.class,
                classHelper);
        assertNotNull(desc);
    }

    public void testGetHolderDesc_DomaIllegalArgumentException() throws Exception {
        try {
            HolderDescFactory.getHolderDesc(Object.class, classHelper);
            fail();
        } catch (DomaIllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void testGetHolderType_HolderTypeNotFoundException() throws Exception {
        try {
            HolderDescFactory.getHolderDesc(Money.class, classHelper);
            fail();
        } catch (HolderDescNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
