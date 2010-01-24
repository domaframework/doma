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
import java.sql.Types;

import junit.framework.TestCase;

public class MockPreparedStatementTest extends TestCase {

    public void testSetString() throws Exception {
        MockPreparedStatement ps = new MockPreparedStatement();
        ps.setString(1, "aaa");
        ps.setString(2, "bbb");

        assertEquals("aaa", ps.bindValues.get(0).getValue());
        assertEquals("bbb", ps.bindValues.get(1).getValue());
    }

    public void testSetInt() throws Exception {
        MockPreparedStatement ps = new MockPreparedStatement();
        ps.setInt(1, 100);
        ps.setInt(2, 200);

        assertEquals(new Integer(100), ps.bindValues.get(0).getValue());
        assertEquals(new Integer(200), ps.bindValues.get(1).getValue());
    }

    public void testSetBigDecimal() throws Exception {
        MockPreparedStatement ps = new MockPreparedStatement();
        ps.setBigDecimal(1, new BigDecimal(10));
        ps.setBigDecimal(2, new BigDecimal(20));

        assertEquals(new BigDecimal(10), ps.bindValues.get(0).getValue());
        assertEquals(new BigDecimal(20), ps.bindValues.get(1).getValue());
    }

    public void testSetNull() throws Exception {
        MockPreparedStatement ps = new MockPreparedStatement();
        ps.setNull(1, Types.INTEGER);
        ps.setNull(2, Types.VARCHAR);

        assertNull(ps.bindValues.get(0).getValue());
        assertNull(ps.bindValues.get(1).getValue());
    }

    public void testExecuteUpdate() throws Exception {
        MockPreparedStatement ps = new MockPreparedStatement();
        assertEquals(1, ps.executeUpdate());
    }

    public void testExecuteUpdate_updatedRows() throws Exception {
        MockPreparedStatement ps = new MockPreparedStatement();
        assertEquals(1, ps.executeUpdate());

        ps.updatedRows = 0;
        assertEquals(0, ps.executeUpdate());
    }
}
