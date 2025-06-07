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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Performance test class for SqlTokenizer implementations. Tests and compares performance between
 * the refactored SqlTokenizer and ClassicSqlTokenizer.
 */
public class SqlTokenizerPerformanceTest {

  private static final int WARMUP_ITERATIONS = 50000;
  private static final int BENCHMARK_ITERATIONS = 20000;
  private static final int STATISTICAL_RUNS = 15;

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

  @Test
  public void testPerformanceComparison() {
    System.out.println("=== SqlTokenizer Performance Test ===\n");

    // Test different SQL patterns
    runPerformanceTest("Simple SQL", SIMPLE_SQL);
    runPerformanceTest("Complex SQL", COMPLEX_SQL);
    runPerformanceTest("Comment Heavy SQL", COMMENT_HEAVY_SQL);
    runPerformanceTest("Keyword Heavy SQL", KEYWORD_HEAVY_SQL);
    runPerformanceTest("Directive Heavy SQL", DIRECTIVE_HEAVY_SQL);

    System.out.println("=== Performance Test Complete ===");
  }

  private void runPerformanceTest(String testName, String sql) {
    System.out.println("--- " + testName + " ---");
    System.out.println("SQL: " + (sql.length() > 80 ? sql.substring(0, 80) + "..." : sql));
    System.out.println("SQL Length: " + sql.length() + " characters");

    // Count tokens for reference
    int tokenCount = countTokens(sql);
    System.out.println("Token Count: " + tokenCount + " tokens");

    // Run multiple alternating rounds to reduce order dependency
    List<PerformanceResult> refactoredResults = new ArrayList<>();
    List<PerformanceResult> classicResults = new ArrayList<>();

    // Perform multiple rounds with alternating order
    for (int round = 0; round < 3; round++) {
      // Warmup before each round
      warmup(sql);

      if (round % 2 == 0) {
        // Round 1, 3: Refactored first
        refactoredResults.add(runStatisticalBenchmark("Refactored SqlTokenizer", sql, false));
        cooldown();
        classicResults.add(runStatisticalBenchmark("Classic SqlTokenizer", sql, true));
      } else {
        // Round 2: Classic first
        classicResults.add(runStatisticalBenchmark("Classic SqlTokenizer", sql, true));
        cooldown();
        refactoredResults.add(runStatisticalBenchmark("Refactored SqlTokenizer", sql, false));
      }
      cooldown();
    }

    // Aggregate results from multiple rounds
    PerformanceResult refactoredResult =
        aggregateResults("Refactored SqlTokenizer", refactoredResults);
    PerformanceResult classicResult = aggregateResults("Classic SqlTokenizer", classicResults);

    // Print results
    printResults(refactoredResult, classicResult, tokenCount);
    System.out.println();
  }

  private void warmup(String sql) {
    // Alternate warmup to reduce bias
    for (int i = 0; i < WARMUP_ITERATIONS / 2; i++) {
      if (i % 2 == 0) {
        tokenizeWithRefactored(sql);
        tokenizeWithClassic(sql);
      } else {
        tokenizeWithClassic(sql);
        tokenizeWithRefactored(sql);
      }
    }

    cooldown();
  }

  private void cooldown() {
    // Give GC a chance and let JVM settle
    System.gc();
    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  private PerformanceResult runStatisticalBenchmark(
      String implementationName, String sql, boolean useClassic) {
    List<Long> executionTimes = new ArrayList<>();

    for (int run = 0; run < STATISTICAL_RUNS; run++) {
      // Small warmup for this specific run
      for (int w = 0; w < 1000; w++) {
        if (useClassic) {
          tokenizeWithClassic(sql);
        } else {
          tokenizeWithRefactored(sql);
        }
      }

      long startTime = System.nanoTime();

      for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
        if (useClassic) {
          tokenizeWithClassic(sql);
        } else {
          tokenizeWithRefactored(sql);
        }
      }

      long endTime = System.nanoTime();
      executionTimes.add(endTime - startTime);
    }

    // Remove outliers (top and bottom 10%)
    Collections.sort(executionTimes);
    int removeCount = Math.max(1, STATISTICAL_RUNS / 10);
    List<Long> filteredTimes =
        executionTimes.subList(removeCount, executionTimes.size() - removeCount);

    return new PerformanceResult(implementationName, filteredTimes, BENCHMARK_ITERATIONS);
  }

  private void tokenizeWithRefactored(String sql) {
    SqlTokenizer tokenizer = new SqlTokenizer(sql);
    while (tokenizer.next() != EOF) {
      // Process token
      tokenizer.getToken();
    }
  }

  private void tokenizeWithClassic(String sql) {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer(sql);
    while (tokenizer.next() != EOF) {
      // Process token
      tokenizer.getToken();
    }
  }

  private int countTokens(String sql) {
    SqlTokenizer tokenizer = new SqlTokenizer(sql);
    int count = 0;
    while (tokenizer.next() != EOF) {
      count++;
    }
    return count;
  }

  private void printResults(
      PerformanceResult refactored, PerformanceResult classic, int tokenCount) {
    System.out.println("\nResults:");
    System.out.printf(
        "%-25s %12s %12s %15s %15s%n",
        "Implementation", "Avg (ms)", "Min (ms)", "Max (ms)", "Tokens/sec");
    System.out.println(
        "--------------------------------------------------------------------------------");

    printResult(refactored, tokenCount);
    printResult(classic, tokenCount);

    // Performance comparison
    double speedupRatio = (double) classic.averageTimeNanos / refactored.averageTimeNanos;
    if (speedupRatio > 1.0) {
      System.out.printf("Refactored is %.2fx faster than Classic%n", speedupRatio);
    } else {
      System.out.printf("Classic is %.2fx faster than Refactored%n", 1.0 / speedupRatio);
    }

    double throughputDiff =
        ((double) refactored.tokensPerSecond - classic.tokensPerSecond)
            / classic.tokensPerSecond
            * 100;
    System.out.printf("Throughput difference: %+.1f%%%n", throughputDiff);
  }

  private PerformanceResult aggregateResults(
      String implementationName, List<PerformanceResult> results) {
    List<Long> allExecutionTimes = new ArrayList<>();

    // Collect average times from each round
    for (PerformanceResult result : results) {
      allExecutionTimes.add(result.averageTimeNanos);
      allExecutionTimes.add(result.minTimeNanos);
      allExecutionTimes.add(result.maxTimeNanos);
    }

    return new PerformanceResult(implementationName, allExecutionTimes, BENCHMARK_ITERATIONS);
  }

  private void printResult(PerformanceResult result, int tokenCount) {
    System.out.printf(
        "%-25s %12.3f %12.3f %15.3f %15.0f%n",
        result.implementationName,
        result.averageTimeNanos / 1_000_000.0,
        result.minTimeNanos / 1_000_000.0,
        result.maxTimeNanos / 1_000_000.0,
        result.tokensPerSecond);
  }

  @Test
  public void testMemoryUsage() {
    System.out.println("=== Memory Usage Test ===\n");

    String sql = COMPLEX_SQL;
    int iterations = 100000;

    System.out.println("Testing memory usage with " + iterations + " iterations");
    System.out.println("SQL: " + sql.substring(0, Math.min(80, sql.length())) + "...");

    // Test refactored implementation
    Runtime runtime = Runtime.getRuntime();
    System.gc();
    long beforeRefactored = runtime.totalMemory() - runtime.freeMemory();

    for (int i = 0; i < iterations; i++) {
      tokenizeWithRefactored(sql);
    }

    System.gc();
    long afterRefactored = runtime.totalMemory() - runtime.freeMemory();

    // Test classic implementation
    System.gc();
    long beforeClassic = runtime.totalMemory() - runtime.freeMemory();

    for (int i = 0; i < iterations; i++) {
      tokenizeWithClassic(sql);
    }

    System.gc();
    long afterClassic = runtime.totalMemory() - runtime.freeMemory();

    long refactoredMemory = afterRefactored - beforeRefactored;
    long classicMemory = afterClassic - beforeClassic;

    System.out.printf("Refactored memory usage: %,d bytes%n", refactoredMemory);
    System.out.printf("Classic memory usage: %,d bytes%n", classicMemory);
    System.out.printf(
        "Memory difference: %,d bytes (%+.1f%%)%n",
        refactoredMemory - classicMemory,
        ((double) refactoredMemory - classicMemory) / classicMemory * 100);
  }

  private static class PerformanceResult {
    final String implementationName;
    final long averageTimeNanos;
    final long minTimeNanos;
    final long maxTimeNanos;
    final double tokensPerSecond;

    PerformanceResult(String implementationName, List<Long> executionTimes, int iterationsPerRun) {
      this.implementationName = implementationName;

      this.averageTimeNanos =
          (long) executionTimes.stream().mapToLong(Long::longValue).average().orElse(0);
      this.minTimeNanos = executionTimes.stream().mapToLong(Long::longValue).min().orElse(0);
      this.maxTimeNanos = executionTimes.stream().mapToLong(Long::longValue).max().orElse(0);

      // Calculate tokens per second based on average time per iteration
      double avgTimePerIterationSeconds =
          (double) averageTimeNanos / 1_000_000_000.0 / iterationsPerRun;
      this.tokensPerSecond = 1.0 / avgTimePerIterationSeconds;
    }
  }
}
