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

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

/**
 * JMH benchmark for comparing performance between the optimized SqlTokenUtil and the original
 * ClassicSqlTokenUtil.
 */
@SuppressWarnings("deprecation")
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class SqlTokenUtilBenchmark {

  private char[] testChars;

  @Setup
  public void setup() {
    // Create a representative mix of characters
    testChars = createTestData();
  }

  private char[] createTestData() {
    // Mix of word parts, whitespace, and special characters
    String data =
        "SELECT * FROM users WHERE id = 123 AND name = 'John' "
            + "OR (age > 18 AND city IN ('Tokyo', 'Osaka')) "
            + "ORDER BY created_at DESC; -- This is a comment\n"
            + "UPDATE products SET price = price * 1.1 WHERE category = 'electronics';\n"
            + "/* Multi-line\n   comment */ INSERT INTO logs (message) VALUES ('Hello, World!');\n"
            + "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_$#@";

    // Repeat to create a larger dataset
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 10; i++) {
      sb.append(data);
    }
    return sb.toString().toCharArray();
  }

  @Benchmark
  public void optimizedIsWordPart(Blackhole bh) {
    for (char c : testChars) {
      bh.consume(SqlTokenUtil.isWordPart(c));
    }
  }

  @Benchmark
  public void classicIsWordPart(Blackhole bh) {
    for (char c : testChars) {
      bh.consume(ClassicSqlTokenUtil.isWordPart(c));
    }
  }

  @Benchmark
  public void optimizedIsWhitespace(Blackhole bh) {
    for (char c : testChars) {
      bh.consume(SqlTokenUtil.isWhitespace(c));
    }
  }

  @Benchmark
  public void classicIsWhitespace(Blackhole bh) {
    for (char c : testChars) {
      bh.consume(ClassicSqlTokenUtil.isWhitespace(c));
    }
  }

  @Benchmark
  public void optimizedCombined(Blackhole bh) {
    // Test combined usage pattern
    for (char c : testChars) {
      boolean isWord = SqlTokenUtil.isWordPart(c);
      boolean isSpace = SqlTokenUtil.isWhitespace(c);
      bh.consume(isWord);
      bh.consume(isSpace);
    }
  }

  @Benchmark
  public void classicCombined(Blackhole bh) {
    // Test combined usage pattern
    for (char c : testChars) {
      boolean isWord = ClassicSqlTokenUtil.isWordPart(c);
      boolean isSpace = ClassicSqlTokenUtil.isWhitespace(c);
      bh.consume(isWord);
      bh.consume(isSpace);
    }
  }
}
