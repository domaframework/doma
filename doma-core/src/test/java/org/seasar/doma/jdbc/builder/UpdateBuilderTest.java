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
package org.seasar.doma.jdbc.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlParameter;

public class UpdateBuilderTest {

  @Test
  public void test() {
    UpdateBuilder builder = UpdateBuilder.newInstance(new MockConfig());
    builder.sql("update Emp");
    builder.sql("set");
    builder.sql("name = ").param(String.class, "SMIHT").sql(",");
    builder.sql("salary = ").param(BigDecimal.class, new BigDecimal("1000"));
    builder.sql("where");
    builder.sql("ID = ").param(int.class, 10);
    builder.execute();
  }

  @Test
  public void testGetSql() {
    UpdateBuilder builder = UpdateBuilder.newInstance(new MockConfig());
    builder.sql("update Emp");
    builder.sql("set");
    builder.sql("name = ").param(String.class, "SMIHT").sql(",");
    builder.sql("salary = ").param(BigDecimal.class, new BigDecimal("1000"));
    builder.sql("where");
    builder.sql("ID = ").param(int.class, 10);

    String sql =
        String.format(
            "update Emp%n" + "set%n" + "name = ?,%n" + "salary = ?%n" + "where%n" + "ID = ?");
    assertEquals(sql, builder.getSql().getRawSql());

    builder.execute();
  }

  @Test
  public void testLiteral() {
    UpdateBuilder builder = UpdateBuilder.newInstance(new MockConfig());
    builder.sql("update Emp");
    builder.sql("set");
    builder.sql("name = ").literal(String.class, "SMITH").sql(",");
    builder.sql("salary = ").literal(BigDecimal.class, new BigDecimal("1000"));
    builder.sql("where");
    builder.sql("ID = ").param(int.class, 10);

    String sql =
        String.format(
            "update Emp%n"
                + "set%n"
                + "name = 'SMITH',%n"
                + "salary = 1000%n"
                + "where%n"
                + "ID = ?");
    assertEquals(sql, builder.getSql().getRawSql());

    builder.execute();
  }

  @Test
  public void testParams() {
    UpdateBuilder builder = UpdateBuilder.newInstance(new MockConfig());
    builder.sql("update Emp");
    builder.sql("set");
    builder.sql("name = 'SMITH'");
    builder.sql("where");
    builder.sql("name in (").params(String.class, Arrays.asList("x", "y", "z")).sql(")");
    builder.sql("and");
    builder.sql("salary in (").params(int.class, Arrays.asList(10, 20, 30)).sql(")");
    Sql<?> sql = builder.getSql();
    String rawSql =
        String.format(
            "update Emp%n"
                + "set%n"
                + "name = 'SMITH'%n"
                + "where%n"
                + "name in (?, ?, ?)%n"
                + "and%n"
                + "salary in (?, ?, ?)");
    assertEquals(rawSql, sql.getRawSql());

    List<? extends SqlParameter> params = sql.getParameters();
    assertEquals(6, params.size());
    assertEquals("x", params.get(0).getValue());
    assertEquals("y", params.get(1).getValue());
    assertEquals("z", params.get(2).getValue());
    assertEquals(10, params.get(3).getValue());
    assertEquals(20, params.get(4).getValue());
    assertEquals(30, params.get(5).getValue());
  }

  @Test
  public void testParams_empty() {
    UpdateBuilder builder = UpdateBuilder.newInstance(new MockConfig());
    builder.sql("update Emp");
    builder.sql("set");
    builder.sql("name = 'SMITH'");
    builder.sql("where");
    builder.sql("name in (").params(String.class, Collections.emptyList()).sql(")");
    builder.sql("and");
    builder.sql("salary in (").params(int.class, Collections.emptyList()).sql(")");
    Sql<?> sql = builder.getSql();
    String rawSql =
        String.format(
            "update Emp%n"
                + "set%n"
                + "name = 'SMITH'%n"
                + "where%n"
                + "name in (null)%n"
                + "and%n"
                + "salary in (null)");
    assertEquals(rawSql, sql.getRawSql());

    List<? extends SqlParameter> params = sql.getParameters();
    assertEquals(0, params.size());
  }

  @Test
  public void testLiterals() {
    UpdateBuilder builder = UpdateBuilder.newInstance(new MockConfig());
    builder.sql("update Emp");
    builder.sql("set");
    builder.sql("name = 'SMITH'");
    builder.sql("where");
    builder.sql("name in (").literals(String.class, Arrays.asList("x", "y", "z")).sql(")");
    builder.sql("and");
    builder.sql("salary in (").literals(int.class, Arrays.asList(10, 20, 30)).sql(")");
    Sql<?> sql = builder.getSql();
    String rawSql =
        String.format(
            "update Emp%n"
                + "set%n"
                + "name = 'SMITH'%n"
                + "where%n"
                + "name in ('x', 'y', 'z')%n"
                + "and%n"
                + "salary in (10, 20, 30)");
    assertEquals(rawSql, sql.getRawSql());

    List<? extends SqlParameter> params = sql.getParameters();
    assertEquals(0, params.size());
  }

  @Test
  public void testLiterals_empty() {
    UpdateBuilder builder = UpdateBuilder.newInstance(new MockConfig());
    builder.sql("update Emp");
    builder.sql("set");
    builder.sql("name = 'SMITH'");
    builder.sql("where");
    builder.sql("name in (").literals(String.class, Collections.emptyList()).sql(")");
    builder.sql("and");
    builder.sql("salary in (").literals(int.class, Collections.emptyList()).sql(")");
    Sql<?> sql = builder.getSql();
    String rawSql =
        String.format(
            "update Emp%n"
                + "set%n"
                + "name = 'SMITH'%n"
                + "where%n"
                + "name in (null)%n"
                + "and%n"
                + "salary in (null)");
    assertEquals(rawSql, sql.getRawSql());

    List<? extends SqlParameter> params = sql.getParameters();
    assertEquals(0, params.size());
  }
}
