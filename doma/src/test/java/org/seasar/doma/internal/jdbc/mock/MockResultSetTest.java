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
package org.seasar.doma.internal.jdbc.mock;

import java.math.BigDecimal;

import junit.framework.TestCase;

public class MockResultSetTest extends TestCase {

    public void testNext() throws Exception {
        MockResultSet resultSet = new MockResultSet();
        resultSet.rows.add(new RowData("aaa"));
        resultSet.rows.add(new RowData("bbb"));

        assertTrue(resultSet.next());
        assertEquals("aaa", resultSet.getString(1));
        assertTrue(resultSet.next());
        assertEquals("bbb", resultSet.getString(1));
        assertFalse(resultSet.next());
    }

    public void testGetString() throws Exception {
        MockResultSet resultSet = new MockResultSet();
        resultSet.rows.add(new RowData("aaa", "bbb"));

        assertTrue(resultSet.next());
        assertEquals("aaa", resultSet.getString(1));
        assertEquals("bbb", resultSet.getString(2));
    }

    public void testGetInteger() throws Exception {
        MockResultSet resultSet = new MockResultSet();
        resultSet.rows.add(new RowData(10, 20));

        assertTrue(resultSet.next());
        assertEquals(10, resultSet.getInt(1));
        assertEquals(20, resultSet.getInt(2));
    }

    public void testGetBigDecimal() throws Exception {
        MockResultSet resultSet = new MockResultSet();
        resultSet.rows.add(new RowData(new BigDecimal(10), new BigDecimal(20)));

        assertTrue(resultSet.next());
        assertEquals(new BigDecimal(10), resultSet.getBigDecimal(1));
        assertEquals(new BigDecimal(20), resultSet.getBigDecimal(2));
    }
}
