package org.seasar.doma.jdbc.builder;

import example.domain.PhoneNumber;
import example.entity.Emp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlParameter;
import org.seasar.doma.message.Message;

public class SelectBuilderTest extends TestCase {

  public void testGetSql() throws Exception {
    SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select");
    builder.sql("id").sql(",");
    builder.sql("name").sql(",");
    builder.sql("salary");
    builder.sql("from Emp");
    builder.sql("where");
    builder.sql("name like ").param(String.class, "S%");
    builder.sql("and");
    builder.sql("age > ").param(int.class, 20);

    String sql =
        String.format(
            "select%n"
                + "id,%n"
                + "name,%n"
                + "salary%n"
                + "from Emp%n"
                + "where%n"
                + "name like ?%n"
                + "and%n"
                + "age > ?");
    assertEquals(sql, builder.getSql().getRawSql());

    Emp emp = builder.getEntitySingleResult(Emp.class);
    assertNull(emp);
  }

  public void testRmoveLast() throws Exception {
    SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("aaa").sql("bbb");
    builder.removeLast();
    assertEquals("aaa", builder.getSql().getRawSql());
  }

  public void testSingleResult_Entity() throws Exception {
    SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select");
    builder.sql("id").sql(",");
    builder.sql("name").sql(",");
    builder.sql("salary");
    builder.sql("from Emp");
    builder.sql("where");
    builder.sql("name like ").param(String.class, "S%");
    builder.sql("and");
    builder.sql("age > ").param(int.class, 20);
    Emp emp = builder.getEntitySingleResult(Emp.class);
    assertNull(emp);
  }

  public void testSingleResult_Map() throws Exception {
    SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select");
    builder.sql("id").sql(",");
    builder.sql("name").sql(",");
    builder.sql("salary");
    builder.sql("from Emp");
    builder.sql("where");
    builder.sql("name like ").param(String.class, "S%");
    builder.sql("and");
    builder.sql("age > ").param(int.class, 20);
    Map<String, Object> emp = builder.getMapSingleResult(MapKeyNamingType.CAMEL_CASE);
    assertNull(emp);
  }

  public void testSingleResult_Domain() throws Exception {
    SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select ccc from Emp");
    builder.sql("where");
    builder.sql("aaa = ").param(String.class, "aaa");
    builder.sql("and");
    builder.sql("bbb = ").param(int.class, 100);
    PhoneNumber phoneNumber = builder.getScalarSingleResult(PhoneNumber.class);
    assertNull(phoneNumber);
  }

  public void testSingleResult_Basic() throws Exception {
    SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select ccc from Emp");
    builder.sql("where");
    builder.sql("aaa = ").param(String.class, "aaa");
    builder.sql("and");
    builder.sql("bbb = ").param(int.class, 100);
    String result = builder.getScalarSingleResult(String.class);
    assertNull(result);
  }

  public void testSingleResult_DomaIllegalArgumentException() throws Exception {
    SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select");
    builder.sql("aaa").sql(",");
    builder.sql("bbb");
    builder.sql("from Emp");
    builder.sql("where");
    builder.sql("aaa = ").param(String.class, "aaa");
    builder.sql("and");
    builder.sql("bbb = ").param(int.class, 100);
    try {
      builder.getScalarSingleResult(Class.class);
      fail();
    } catch (DomaIllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  public void testGetResultList_Entity() throws Exception {
    SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select * from Emp");
    builder.sql("where");
    builder.sql("aaa = ").param(String.class, "aaa");
    builder.sql("and");
    builder.sql("bbb = ").param(int.class, 100);
    List<Emp> list = builder.getEntityResultList(Emp.class);
    assertNotNull(list);
  }

  public void testGetResultList_Map() throws Exception {
    SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select * from Emp");
    builder.sql("where");
    builder.sql("aaa = ").param(String.class, "aaa");
    builder.sql("and");
    builder.sql("bbb = ").param(int.class, 100);
    List<Map<String, Object>> list = builder.getMapResultList(MapKeyNamingType.CAMEL_CASE);
    assertNotNull(list);
  }

  public void testGetResultList_Domain() throws Exception {
    SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select ccc from Emp");
    builder.sql("where");
    builder.sql("aaa = ").param(String.class, "aaa");
    builder.sql("and");
    builder.sql("bbb = ").param(int.class, 100);
    List<PhoneNumber> list = builder.getScalarResultList(PhoneNumber.class);
    assertNotNull(list);
  }

  public void testGetResultList_Basic() throws Exception {
    SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select ccc from Emp");
    builder.sql("where");
    builder.sql("aaa = ").param(String.class, "aaa");
    builder.sql("and");
    builder.sql("bbb = ").param(int.class, 100);
    List<String> list = builder.getScalarResultList(String.class);
    assertNotNull(list);
  }

  public void testLiteral() throws Exception {
    SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select");
    builder.sql("id").sql(",");
    builder.sql("name").sql(",");
    builder.sql("salary");
    builder.sql("from Emp");
    builder.sql("where");
    builder.sql("name = ").literal(String.class, "aaa");
    builder.sql("and");
    builder.sql("age > ").param(int.class, 20);
    String sql =
        String.format(
            "select%n"
                + "id,%n"
                + "name,%n"
                + "salary%n"
                + "from Emp%n"
                + "where%n"
                + "name = 'aaa'%n"
                + "and%n"
                + "age > ?");
    assertEquals(sql, builder.getSql().getRawSql());
  }

  public void testLiteral_singleQuoteIncluded() throws Exception {
    SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select");
    builder.sql("id").sql(",");
    builder.sql("name").sql(",");
    builder.sql("salary");
    builder.sql("from Emp");
    builder.sql("where");
    builder.sql("code = ").literal(String.class, "a'aa");
    builder.sql("and");
    builder.sql("age > ").param(int.class, 20);
    try {
      builder.getSql();
    } catch (JdbcException e) {
      assertEquals(Message.DOMA2224, e.getMessageResource());
    }
  }

  public void testParams() throws Exception {
    SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select ccc from Emp");
    builder.sql("where");
    builder.sql("aaa in (").params(String.class, Arrays.asList("x", "y", "z")).sql(")");
    Sql<?> sql = builder.getSql();
    String rawSql = String.format("select ccc from Emp%n" + "where%n" + "aaa in (?, ?, ?)");
    assertEquals(rawSql, sql.getRawSql());

    List<? extends SqlParameter> params = sql.getParameters();
    assertEquals(3, params.size());
    assertEquals("x", params.get(0).getValue());
    assertEquals("y", params.get(1).getValue());
    assertEquals("z", params.get(2).getValue());
  }

  public void testParams_empty() throws Exception {
    SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select ccc from Emp");
    builder.sql("where");
    builder.sql("aaa in (").params(String.class, Collections.emptyList()).sql(")");
    Sql<?> sql = builder.getSql();
    String rawSql = String.format("select ccc from Emp%n" + "where%n" + "aaa in (null)");
    assertEquals(rawSql, sql.getRawSql());

    List<? extends SqlParameter> params = sql.getParameters();
    assertEquals(0, params.size());
  }

  public void testLiterals() throws Exception {
    SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select ccc from Emp");
    builder.sql("where");
    builder.sql("aaa in (").literals(String.class, Arrays.asList("x", "y", "z")).sql(")");
    Sql<?> sql = builder.getSql();
    String rawSql = String.format("select ccc from Emp%n" + "where%n" + "aaa in ('x', 'y', 'z')");
    assertEquals(rawSql, sql.getRawSql());

    List<? extends SqlParameter> params = sql.getParameters();
    assertEquals(0, params.size());
  }

  public void testLiterals_empty() throws Exception {
    SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select ccc from Emp");
    builder.sql("where");
    builder.sql("aaa in (").params(String.class, Collections.emptyList()).sql(")");
    Sql<?> sql = builder.getSql();
    String rawSql = String.format("select ccc from Emp%n" + "where%n" + "aaa in (null)");
    assertEquals(rawSql, sql.getRawSql());

    List<? extends SqlParameter> params = sql.getParameters();
    assertEquals(0, params.size());
  }
}
