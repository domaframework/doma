package org.seasar.doma.internal.jdbc.mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

public class MockResultSetTest {

  @Test
  public void testNext() throws Exception {
    @SuppressWarnings("resource")
    MockResultSet resultSet = new MockResultSet();
    resultSet.rows.add(new RowData("aaa"));
    resultSet.rows.add(new RowData("bbb"));

    assertTrue(resultSet.next());
    assertEquals("aaa", resultSet.getString(1));
    assertTrue(resultSet.next());
    assertEquals("bbb", resultSet.getString(1));
    assertFalse(resultSet.next());
  }

  @Test
  public void testGetString() throws Exception {
    @SuppressWarnings("resource")
    MockResultSet resultSet = new MockResultSet();
    resultSet.rows.add(new RowData("aaa", "bbb"));

    assertTrue(resultSet.next());
    assertEquals("aaa", resultSet.getString(1));
    assertEquals("bbb", resultSet.getString(2));
  }

  @Test
  public void testGetInteger() throws Exception {
    @SuppressWarnings("resource")
    MockResultSet resultSet = new MockResultSet();
    resultSet.rows.add(new RowData(10, 20));

    assertTrue(resultSet.next());
    assertEquals(10, resultSet.getInt(1));
    assertEquals(20, resultSet.getInt(2));
  }

  @Test
  public void testGetBigDecimal() throws Exception {
    @SuppressWarnings("resource")
    MockResultSet resultSet = new MockResultSet();
    resultSet.rows.add(new RowData(new BigDecimal(10), new BigDecimal(20)));

    assertTrue(resultSet.next());
    assertEquals(new BigDecimal(10), resultSet.getBigDecimal(1));
    assertEquals(new BigDecimal(20), resultSet.getBigDecimal(2));
  }
}
