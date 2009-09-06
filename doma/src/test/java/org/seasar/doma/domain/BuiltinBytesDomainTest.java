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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class BuiltinBytesDomainTest extends TestCase {

    public void testEquals() throws Exception {
        BuiltinBytesDomain domain = new BuiltinBytesDomain(new byte[] { 1, 2, 3 });
        BuiltinBytesDomain domain2 = new BuiltinBytesDomain(new byte[] { 1, 2, 3 });
        assertTrue(domain.equals(domain2));

        domain.setNull();
        assertFalse(domain.equals(domain2));

        domain2.setNull();
        assertTrue(domain.equals(domain2));

        domain.set(new byte[] { 1 });
        assertFalse(domain.equals(domain2));
    }

    public void testSerializable() throws Exception {
        BuiltinBytesDomain domain = new BuiltinBytesDomain(new byte[] { 1, 2, 3 });
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(domain);
        oos.flush();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        assertEquals(domain, ois.readObject());
    }
}
