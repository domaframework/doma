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
package org.seasar.doma.internal.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Performance comparison test between the optimized SqlTokenUtil and the original
 * ClassicSqlTokenUtil. This test measures the performance improvements from the bitmask
 * optimization while eliminating execution order effects.
 */
public class SqlTokenUtilPerformanceTest {

  private static final int WARMUP_ITERATIONS = 10000;
  private static final int BENCHMARK_ITERATIONS = 100000;

  @Test
  public void testPerformanceComparison() {
    // Test data: mix of common ASCII characters
    char[] testChars = createTestData();

    System.out.println("=== SqlTokenUtil vs ClassicSqlTokenUtil Performance Comparison ===");
    System.out.println("Test data size: " + testChars.length + " characters");
    System.out.println("Warmup iterations: " + WARMUP_ITERATIONS);
    System.out.println("Benchmark iterations: " + BENCHMARK_ITERATIONS);
    System.out.println();

    // Run multiple rounds to eliminate execution order effects
    int rounds = 5;
    long[] optimizedTimes = new long[rounds];
    long[] classicTimes = new long[rounds];

    for (int round = 0; round < rounds; round++) {
      System.out.printf("Round %d/%d...%n", round + 1, rounds);

      // Warmup both implementations
      warmupBothImplementations(testChars);

      // Alternate execution order to eliminate bias
      if (round % 2 == 0) {
        optimizedTimes[round] = benchmarkOptimized(testChars);
        classicTimes[round] = benchmarkClassic(testChars);
      } else {
        classicTimes[round] = benchmarkClassic(testChars);
        optimizedTimes[round] = benchmarkOptimized(testChars);
      }
    }

    // Calculate averages excluding first round (warmup effect)
    long avgOptimized = calculateAverage(optimizedTimes, 1);
    long avgClassic = calculateAverage(classicTimes, 1);

    double speedupRatio = (double) avgClassic / avgOptimized;

    System.out.println();
    System.out.println("=== Results (average of rounds 2-5) ===");
    System.out.printf("Optimized SqlTokenUtil: %,d ns total%n", avgOptimized);
    System.out.printf("Classic SqlTokenUtil: %,d ns total%n", avgClassic);
    System.out.printf("Speedup: %.2fx faster%n", speedupRatio);

    long totalCalls = (long) BENCHMARK_ITERATIONS * testChars.length * 2;
    System.out.printf("Optimized: %.2f ns/call%n", (double) avgOptimized / totalCalls);
    System.out.printf("Classic: %.2f ns/call%n", (double) avgClassic / totalCalls);

    // Performance assertions
    assertTrue(avgOptimized > 0, "Optimized benchmark should take measurable time");
    assertTrue(avgClassic > 0, "Classic benchmark should take measurable time");
    assertTrue(
        speedupRatio > 1.0,
        "Optimized version should be faster than classic, speedup: " + speedupRatio);
  }

  @Test
  public void testAsciiOptimizationEffectiveness() {
    // Test specifically ASCII characters to verify fast path effectiveness
    char[] asciiChars = new char[128];
    for (int i = 0; i < 128; i++) {
      asciiChars[i] = (char) i;
    }

    System.out.println("=== ASCII Character Optimization Test ===");

    // Warmup both implementations
    for (int i = 0; i < 1000; i++) {
      for (char c : asciiChars) {
        SqlTokenUtil.isWhitespace(c);
        SqlTokenUtil.isWordPart(c);
        ClassicSqlTokenUtil.isWhitespace(c);
        ClassicSqlTokenUtil.isWordPart(c);
      }
    }

    // Benchmark optimized version
    long optimizedStart = System.nanoTime();
    for (int i = 0; i < 50000; i++) {
      for (char c : asciiChars) {
        SqlTokenUtil.isWhitespace(c);
        SqlTokenUtil.isWordPart(c);
      }
    }
    long optimizedTime = System.nanoTime() - optimizedStart;

    // Benchmark classic version
    long classicStart = System.nanoTime();
    for (int i = 0; i < 50000; i++) {
      for (char c : asciiChars) {
        ClassicSqlTokenUtil.isWhitespace(c);
        ClassicSqlTokenUtil.isWordPart(c);
      }
    }
    long classicTime = System.nanoTime() - classicStart;

    long totalCalls = 50000L * 128 * 2;
    double optimizedNsPerCall = (double) optimizedTime / totalCalls;
    double classicNsPerCall = (double) classicTime / totalCalls;
    double asciiSpeedup = classicNsPerCall / optimizedNsPerCall;

    System.out.printf("Optimized ASCII: %.2f ns/call%n", optimizedNsPerCall);
    System.out.printf("Classic ASCII: %.2f ns/call%n", classicNsPerCall);
    System.out.printf("ASCII speedup: %.2fx%n", asciiSpeedup);

    // ASCII calls should be extremely fast with bitmask lookup
    assertTrue(
        optimizedNsPerCall < 5.0,
        "Optimized ASCII character classification should be under 5ns per call, was: "
            + optimizedNsPerCall);
    assertTrue(
        asciiSpeedup > 1.0,
        "Optimized version should be faster for ASCII chars, speedup: " + asciiSpeedup);
  }

  @Test
  public void testUnicodeCompatibility() {
    // Test Unicode characters to ensure fallback works correctly
    char[] unicodeChars = {'α', 'β', 'あ', '漢', '€', '©', '\u2000', '\u2001'};

    // These should all work correctly even though they use fallback logic
    for (char c : unicodeChars) {
      // Should not throw exceptions
      boolean isWhitespace = SqlTokenUtil.isWhitespace(c);
      boolean isWordPart = SqlTokenUtil.isWordPart(c);

      // Basic consistency check
      if (Character.isWhitespace(c)) {
        assertFalse(isWordPart, "Unicode whitespace should not be word part: " + c);
      }

      // SqlTokenUtil.isWhitespace only returns true for specific ASCII chars
      if (c >= 128) {
        assertFalse(isWhitespace, "Non-ASCII chars should not be SqlTokenUtil whitespace: " + c);
      }
    }
  }

  private char[] createTestData() {
    // Create realistic test data: mix of letters, digits, operators, whitespace
    String testString =
        "SELECT name, age FROM users WHERE age > 21 AND name LIKE 'John%' ORDER BY name;";
    return testString.toCharArray();
  }

  private void warmupBothImplementations(char[] testChars) {
    // Warmup both implementations equally
    for (int i = 0; i < WARMUP_ITERATIONS / 2; i++) {
      for (char c : testChars) {
        SqlTokenUtil.isWhitespace(c);
        SqlTokenUtil.isWordPart(c);
        ClassicSqlTokenUtil.isWhitespace(c);
        ClassicSqlTokenUtil.isWordPart(c);
      }
    }
  }

  private long benchmarkOptimized(char[] testChars) {
    long startTime = System.nanoTime();

    for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
      for (char c : testChars) {
        SqlTokenUtil.isWhitespace(c);
        SqlTokenUtil.isWordPart(c);
      }
    }

    return System.nanoTime() - startTime;
  }

  private long benchmarkClassic(char[] testChars) {
    long startTime = System.nanoTime();

    for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
      for (char c : testChars) {
        ClassicSqlTokenUtil.isWhitespace(c);
        ClassicSqlTokenUtil.isWordPart(c);
      }
    }

    return System.nanoTime() - startTime;
  }

  private long calculateAverage(long[] times, int startIndex) {
    long sum = 0;
    int count = 0;
    for (int i = startIndex; i < times.length; i++) {
      sum += times[i];
      count++;
    }
    return count > 0 ? sum / count : 0;
  }
}
