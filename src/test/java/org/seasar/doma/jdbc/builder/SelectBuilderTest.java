package org.seasar.doma.jdbc.builder;

import example.entity.Emp;
import example.holder.PhoneNumber;
import java.util.Arrays;
import java.util.Collections;
import junit.framework.TestCase;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

public class SelectBuilderTest extends TestCase {

  public void testGetSql() throws Exception {
    var builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select");
    builder.sql("id").sql(",");
    builder.sql("name").sql(",");
    builder.sql("salary");
    builder.sql("from Emp");
    builder.sql("where");
    builder.sql("name like ").param(String.class, "S%");
    builder.sql("and");
    builder.sql("age > ").param(int.class, 20);

    var sql =
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

    var emp = builder.getEntitySingleResult(Emp.class);
    assertNull(emp);
  }

  public void testRmoveLast() throws Exception {
    var builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("aaa").sql("bbb");
    builder.removeLast();
    assertEquals("aaa", builder.getSql().getRawSql());
  }

  public void testSingleResult_Entity() throws Exception {
    var builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select");
    builder.sql("id").sql(",");
    builder.sql("name").sql(",");
    builder.sql("salary");
    builder.sql("from Emp");
    builder.sql("where");
    builder.sql("name like ").param(String.class, "S%");
    builder.sql("and");
    builder.sql("age > ").param(int.class, 20);
    var emp = builder.getEntitySingleResult(Emp.class);
    assertNull(emp);
  }

  public void testSingleResult_Map() throws Exception {
    var builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select");
    builder.sql("id").sql(",");
    builder.sql("name").sql(",");
    builder.sql("salary");
    builder.sql("from Emp");
    builder.sql("where");
    builder.sql("name like ").param(String.class, "S%");
    builder.sql("and");
    builder.sql("age > ").param(int.class, 20);
    var emp = builder.getMapSingleResult(MapKeyNamingType.CAMEL_CASE);
    assertNull(emp);
  }

  public void testSingleResult_Holder() throws Exception {
    var builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select ccc from Emp");
    builder.sql("where");
    builder.sql("aaa = ").param(String.class, "aaa");
    builder.sql("and");
    builder.sql("bbb = ").param(int.class, 100);
    var phoneNumber = builder.getScalarSingleResult(PhoneNumber.class);
    assertNull(phoneNumber);
  }

  public void testSingleResult_Basic() throws Exception {
    var builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select ccc from Emp");
    builder.sql("where");
    builder.sql("aaa = ").param(String.class, "aaa");
    builder.sql("and");
    builder.sql("bbb = ").param(int.class, 100);
    var result = builder.getScalarSingleResult(String.class);
    assertNull(result);
  }

  public void testSingleResult_DomaIllegalArgumentException() throws Exception {
    var builder = SelectBuilder.newInstance(new MockConfig());
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
    var builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select * from Emp");
    builder.sql("where");
    builder.sql("aaa = ").param(String.class, "aaa");
    builder.sql("and");
    builder.sql("bbb = ").param(int.class, 100);
    var list = builder.getEntityResultList(Emp.class);
    assertNotNull(list);
  }

  public void testGetResultList_Map() throws Exception {
    var builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select * from Emp");
    builder.sql("where");
    builder.sql("aaa = ").param(String.class, "aaa");
    builder.sql("and");
    builder.sql("bbb = ").param(int.class, 100);
    var list = builder.getMapResultList(MapKeyNamingType.CAMEL_CASE);
    assertNotNull(list);
  }

  public void testGetResultList_Holder() throws Exception {
    var builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select ccc from Emp");
    builder.sql("where");
    builder.sql("aaa = ").param(String.class, "aaa");
    builder.sql("and");
    builder.sql("bbb = ").param(int.class, 100);
    var list = builder.getScalarResultList(PhoneNumber.class);
    assertNotNull(list);
  }

  public void testGetResultList_Basic() throws Exception {
    var builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select ccc from Emp");
    builder.sql("where");
    builder.sql("aaa = ").param(String.class, "aaa");
    builder.sql("and");
    builder.sql("bbb = ").param(int.class, 100);
    var list = builder.getScalarResultList(String.class);
    assertNotNull(list);
  }

  public void testLiteral() throws Exception {
    var builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select");
    builder.sql("id").sql(",");
    builder.sql("name").sql(",");
    builder.sql("salary");
    builder.sql("from Emp");
    builder.sql("where");
    builder.sql("name = ").literal(String.class, "aaa");
    builder.sql("and");
    builder.sql("age > ").param(int.class, 20);
    var sql =
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
    var builder = SelectBuilder.newInstance(new MockConfig());
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
    var builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select ccc from Emp");
    builder.sql("where");
    builder.sql("aaa in (").params(String.class, Arrays.asList("x", "y", "z")).sql(")");
    var sql = builder.getSql();
    var rawSql = String.format("select ccc from Emp%n" + "where%n" + "aaa in (?, ?, ?)");
    assertEquals(rawSql, sql.getRawSql());

    var params = sql.getParameters();
    assertEquals(3, params.size());
    assertEquals("x", params.get(0).getValue());
    assertEquals("y", params.get(1).getValue());
    assertEquals("z", params.get(2).getValue());
  }

  public void testParams_empty() throws Exception {
    var builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select ccc from Emp");
    builder.sql("where");
    builder.sql("aaa in (").params(String.class, Collections.emptyList()).sql(")");
    var sql = builder.getSql();
    var rawSql = String.format("select ccc from Emp%n" + "where%n" + "aaa in (null)");
    assertEquals(rawSql, sql.getRawSql());

    var params = sql.getParameters();
    assertEquals(0, params.size());
  }

  public void testLiterals() throws Exception {
    var builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select ccc from Emp");
    builder.sql("where");
    builder.sql("aaa in (").literals(String.class, Arrays.asList("x", "y", "z")).sql(")");
    var sql = builder.getSql();
    var rawSql = String.format("select ccc from Emp%n" + "where%n" + "aaa in ('x', 'y', 'z')");
    assertEquals(rawSql, sql.getRawSql());

    var params = sql.getParameters();
    assertEquals(0, params.size());
  }

  public void testLiterals_empty() throws Exception {
    var builder = SelectBuilder.newInstance(new MockConfig());
    builder.sql("select ccc from Emp");
    builder.sql("where");
    builder.sql("aaa in (").params(String.class, Collections.emptyList()).sql(")");
    var sql = builder.getSql();
    var rawSql = String.format("select ccc from Emp%n" + "where%n" + "aaa in (null)");
    assertEquals(rawSql, sql.getRawSql());

    var params = sql.getParameters();
    assertEquals(0, params.size());
  }
}
