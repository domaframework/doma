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
package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.EOF;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

/**
 * JMH benchmark comparing performance between the optimized SqlTokenizer and ClassicSqlTokenizer.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class SqlTokenizerBenchmark {

  // Test SQL samples representing different complexity levels
  private static final String SIMPLE_SQL = "SELECT * FROM users WHERE id = 1";

  private static final String COMPLEX_SQL =
      "SELECT e.id, e.name, d.department_name, s.salary "
          + "FROM employees e "
          + "INNER JOIN departments d ON e.dept_id = d.id "
          + "LEFT JOIN salaries s ON e.id = s.emp_id "
          + "WHERE e.created_date BETWEEN '2020-01-01' AND '2023-12-31' "
          + "AND d.active = true "
          + "ORDER BY e.name, s.salary DESC "
          + "FOR UPDATE";

  private static final String COMMENT_HEAVY_SQL =
      "SELECT /*+ HINT */ e.id, /* employee id */ e.name /* full name */ "
          + "FROM employees e /* main table */ "
          + "WHERE e.id = /*employeeId*/1 /* bind parameter */ "
          + "AND e.status = /*^status*/'ACTIVE' /* literal parameter */ "
          + "/*%if condition*/ AND e.dept_id = /*deptId*/10 /*%end*/ "
          + "-- line comment at end";

  private static final String KEYWORD_HEAVY_SQL =
      "SELECT DISTINCT col1, col2 FROM table1 "
          + "UNION SELECT col1, col2 FROM table2 "
          + "EXCEPT SELECT col1, col2 FROM table3 "
          + "INTERSECT SELECT col1, col2 FROM table4 "
          + "MINUS SELECT col1, col2 FROM table5 "
          + "ORDER BY col1 GROUP BY col2 HAVING count(*) > 1";

  private static final String DIRECTIVE_HEAVY_SQL =
      "/*%if useEmployees*/"
          + "SELECT /*%expand*/* FROM employees "
          + "/*%for item : items*/"
          + "WHERE id IN (/*item.id*/1) "
          + "/*%end*/"
          + "/*%else*/"
          + "SELECT /*%expand*/* FROM contractors "
          + "/*%end*/"
          + "/*%populate*/ SET updated_at = CURRENT_TIMESTAMP";

  @Param({"SIMPLE", "COMPLEX", "COMMENT_HEAVY", "KEYWORD_HEAVY", "DIRECTIVE_HEAVY"})
  private String sqlType;

  private String sql;

  @Setup
  public void setup() {
    switch (sqlType) {
      case "SIMPLE":
        sql = SIMPLE_SQL;
        break;
      case "COMPLEX":
        sql = COMPLEX_SQL;
        break;
      case "COMMENT_HEAVY":
        sql = COMMENT_HEAVY_SQL;
        break;
      case "KEYWORD_HEAVY":
        sql = KEYWORD_HEAVY_SQL;
        break;
      case "DIRECTIVE_HEAVY":
        sql = DIRECTIVE_HEAVY_SQL;
        break;
      default:
        throw new IllegalArgumentException("Unknown SQL type: " + sqlType);
    }
  }

  @Benchmark
  public void optimizedTokenizer(Blackhole bh) {
    SqlTokenizer tokenizer = new SqlTokenizer(sql);
    while (tokenizer.next() != EOF) {
      bh.consume(tokenizer.getToken());
    }
  }

  @Benchmark
  public void classicTokenizer(Blackhole bh) {
    @SuppressWarnings("deprecation")
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer(sql);
    while (tokenizer.next() != EOF) {
      bh.consume(tokenizer.getToken());
    }
  }
}
