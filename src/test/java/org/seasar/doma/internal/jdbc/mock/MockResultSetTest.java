package org.seasar.doma.internal.jdbc.mock;

import java.math.BigDecimal;
import junit.framework.TestCase;

public class MockResultSetTest extends TestCase {

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

  public void testGetString() throws Exception {
    @SuppressWarnings("resource")
    MockResultSet resultSet = new MockResultSet();
    resultSet.rows.add(new RowData("aaa", "bbb"));

    assertTrue(resultSet.next());
    assertEquals("aaa", resultSet.getString(1));
    assertEquals("bbb", resultSet.getString(2));
  }

  public void testGetInteger() throws Exception {
    @SuppressWarnings("resource")
    MockResultSet resultSet = new MockResultSet();
    resultSet.rows.add(new RowData(10, 20));

    assertTrue(resultSet.next());
    assertEquals(10, resultSet.getInt(1));
    assertEquals(20, resultSet.getInt(2));
  }

  public void testGetBigDecimal() throws Exception {
    @SuppressWarnings("resource")
    MockResultSet resultSet = new MockResultSet();
    resultSet.rows.add(new RowData(new BigDecimal(10), new BigDecimal(20)));

    assertTrue(resultSet.next());
    assertEquals(new BigDecimal(10), resultSet.getBigDecimal(1));
    assertEquals(new BigDecimal(20), resultSet.getBigDecimal(2));
  }
}
