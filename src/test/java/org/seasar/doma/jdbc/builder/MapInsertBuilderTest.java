package org.seasar.doma.jdbc.builder;

import java.util.LinkedHashMap;
import java.util.List;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.SqlParameter;

/** @author bakenezumi */
public class MapInsertBuilderTest extends TestCase {

  @SuppressWarnings("serial")
  public void test() throws Exception {
    MapInsertBuilder builder = MapInsertBuilder.newInstance(new MockConfig(), "Emp");
    builder.execute(
        new LinkedHashMap<String, Object>() {
          {
            put("name", "SMITH");
            put("salary", 100);
          }
        });
  }

  @SuppressWarnings("serial")
  public void testGetSql() throws Exception {
    MapInsertBuilder builder = MapInsertBuilder.newInstance(new MockConfig(), "Emp");

    builder.execute(
        new LinkedHashMap<String, Object>() {
          {
            put("name", "SMITH");
            put("salary", 100);
          }
        });

    String expectedSql = String.format("insert into Emp" + " (name, salary)%n" + "values (?, ?)");
    assertEquals(expectedSql, builder.getSql().getRawSql());

    List<? extends SqlParameter> parameters = builder.getSql().getParameters();
    assertEquals(2, parameters.size());
    assertEquals("SMITH", parameters.get(0).getValue());
    assertEquals(100, parameters.get(1).getValue());
  }

  @SuppressWarnings("serial")
  public void testNullValue() throws Exception {
    MapInsertBuilder builder = MapInsertBuilder.newInstance(new MockConfig(), "Emp");
    builder.execute(
        new LinkedHashMap<String, Object>() {
          {
            put("name", null);
            put("salary", 100);
          }
        });

    String expectedSql =
        String.format("insert into Emp" + " (name, salary)%n" + "values (%nNULL, ?)");
    assertEquals(expectedSql, builder.getSql().getRawSql());

    List<? extends SqlParameter> parameters = builder.getSql().getParameters();
    assertEquals(1, parameters.size());
    assertEquals(100, parameters.get(0).getValue());
  }

  @SuppressWarnings("serial")
  public void testLastNullValue() throws Exception {
    MapInsertBuilder builder = MapInsertBuilder.newInstance(new MockConfig(), "Emp");
    builder.execute(
        new LinkedHashMap<String, Object>() {
          {
            put("salary", 100);
            put("name", null);
          }
        });

    String expectedSql =
        String.format("insert into Emp" + " (salary, name)%n" + "values (?, %nNULL)");
    assertEquals(expectedSql, builder.getSql().getRawSql());

    List<? extends SqlParameter> parameters = builder.getSql().getParameters();
    assertEquals(1, parameters.size());
    assertEquals(100, parameters.get(0).getValue());
  }
}
