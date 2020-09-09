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

    assertEquals(new Integer(100), ps.bindValues.get(0).getValue());
    assertEquals(new Integer(200), ps.bindValues.get(1).getValue());
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
