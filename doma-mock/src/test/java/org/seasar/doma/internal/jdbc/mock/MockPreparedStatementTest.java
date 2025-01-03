/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.jdbc.mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.sql.Types;
import org.junit.jupiter.api.Test;

public class MockPreparedStatementTest {

  @Test
  public void testSetString() throws Exception {
    @SuppressWarnings("resource")
    MockPreparedStatement ps = new MockPreparedStatement();
    ps.setString(1, "aaa");
    ps.setString(2, "bbb");

    assertEquals("aaa", ps.bindValues.get(0).getValue());
    assertEquals("bbb", ps.bindValues.get(1).getValue());
  }

  @Test
  public void testSetInt() throws Exception {
    @SuppressWarnings("resource")
    MockPreparedStatement ps = new MockPreparedStatement();
    ps.setInt(1, 100);
    ps.setInt(2, 200);

    assertEquals(100, ps.bindValues.get(0).getValue());
    assertEquals(200, ps.bindValues.get(1).getValue());
  }

  @Test
  public void testSetBigDecimal() throws Exception {
    @SuppressWarnings("resource")
    MockPreparedStatement ps = new MockPreparedStatement();
    ps.setBigDecimal(1, new BigDecimal(10));
    ps.setBigDecimal(2, new BigDecimal(20));

    assertEquals(new BigDecimal(10), ps.bindValues.get(0).getValue());
    assertEquals(new BigDecimal(20), ps.bindValues.get(1).getValue());
  }

  @Test
  public void testSetNull() throws Exception {
    @SuppressWarnings("resource")
    MockPreparedStatement ps = new MockPreparedStatement();
    ps.setNull(1, Types.INTEGER);
    ps.setNull(2, Types.VARCHAR);

    assertNull(ps.bindValues.get(0).getValue());
    assertNull(ps.bindValues.get(1).getValue());
  }

  @Test
  public void testExecuteUpdate() throws Exception {
    @SuppressWarnings("resource")
    MockPreparedStatement ps = new MockPreparedStatement();
    assertEquals(1, ps.executeUpdate());
  }

  @Test
  public void testExecuteUpdate_updatedRows() throws Exception {
    @SuppressWarnings("resource")
    MockPreparedStatement ps = new MockPreparedStatement();
    assertEquals(1, ps.executeUpdate());

    ps.updatedRows = 0;
    assertEquals(0, ps.executeUpdate());
  }
}
