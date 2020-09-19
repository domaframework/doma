package org.seasar.doma.jdbc.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import example.domain.PhoneNumber;
import example.entity.Emp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlParameter;
import org.seasar.doma.message.Message;

public class SelectBuilderTest {

  @Test
  public void testGetSql() {
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

  @Test
  public void testRmoveLast() {
    SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("aaa").sql("bbb");
    builder.removeLast();
    assertEquals("aaa", builder.getSql().getRawSql());
  }

  @Test
  public void testSingleResult_Entity() {
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

  @Test
  public void testSingleResult_Map() {
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

  @Test
  public void testSingleResult_Domain() {
    SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select ccc from Emp");
    builder.sql("where");
    builder.sql("aaa = ").param(String.class, "aaa");
    builder.sql("and");
    builder.sql("bbb = ").param(int.class, 100);
    PhoneNumber phoneNumber = builder.getScalarSingleResult(PhoneNumber.class);
    assertNull(phoneNumber);
  }

  @Test
  public void testSingleResult_Basic() {
    SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select ccc from Emp");
    builder.sql("where");
    builder.sql("aaa = ").param(String.class, "aaa");
    builder.sql("and");
    builder.sql("bbb = ").param(int.class, 100);
    String result = builder.getScalarSingleResult(String.class);
    assertNull(result);
  }

  @Test
  public void testSingleResult_DomaIllegalArgumentException() {
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

  @Test
  public void testGetResultList_Entity() {
    SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select * from Emp");
    builder.sql("where");
    builder.sql("aaa = ").param(String.class, "aaa");
    builder.sql("and");
    builder.sql("bbb = ").param(int.class, 100);
    List<Emp> list = builder.getEntityResultList(Emp.class);
    assertNotNull(list);
  }

  @Test
  public void testGetResultList_Map() {
    SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select * from Emp");
    builder.sql("where");
    builder.sql("aaa = ").param(String.class, "aaa");
    builder.sql("and");
    builder.sql("bbb = ").param(int.class, 100);
    List<Map<String, Object>> list = builder.getMapResultList(MapKeyNamingType.CAMEL_CASE);
    assertNotNull(list);
  }

  @Test
  public void testGetResultList_Domain() {
    SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select ccc from Emp");
    builder.sql("where");
    builder.sql("aaa = ").param(String.class, "aaa");
    builder.sql("and");
    builder.sql("bbb = ").param(int.class, 100);
    List<PhoneNumber> list = builder.getScalarResultList(PhoneNumber.class);
    assertNotNull(list);
  }

  @Test
  public void testGetResultList_Basic() {
    SelectBuilder builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select ccc from Emp");
    builder.sql("where");
    builder.sql("aaa = ").param(String.class, "aaa");
    builder.sql("and");
    builder.sql("bbb = ").param(int.class, 100);
    List<String> list = builder.getScalarResultList(String.class);
    assertNotNull(list);
  }

  @Test
  public void testLiteral() {
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

  @Test
  public void testLiteral_singleQuoteIncluded() {
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

  @Test
  public void testParams() {
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

  @Test
  public void testParams_empty() {
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

  @Test
  public void testLiterals() {
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

  @Test
  public void testLiterals_empty() {
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
