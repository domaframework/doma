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
package org.seasar.doma.internal.expr;

import static org.seasar.doma.internal.expr.ExpressionTokenType.EOE;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

/**
 * JMH benchmark comparing performance between ExpressionTokenizer and ClassicExpressionTokenizer.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class ExpressionTokenizerBenchmark {

  // Test expression samples representing different complexity levels
  private static final String SIMPLE_EXPRESSION = "employee.name";

  private static final String COMPLEX_EXPRESSION =
      "employee.getName().trim().toUpperCase() == \"JOHN\" && "
          + "(employee.getAge() >= 18 && employee.getAge() <= 65) || "
          + "employee.getStatus() != null && employee.getStatus().isActive()";

  private static final String LITERAL_HEAVY_EXPRESSION =
      "\"Hello World\" + 123 + 45.67F + 89.01D + 123L + 456B + "
          + "true && false || null != \"test string with spaces\"";

  private static final String FUNCTION_HEAVY_EXPRESSION =
      "@prefix(name, \"Mr.\") + @suffix(name, \"Jr.\") + "
          + "@java.lang.String@valueOf(age) + @java.lang.Math@max(10, 20) + "
          + "@isEmpty(list) || @isNotEmpty(collection)";

  private static final String OPERATOR_HEAVY_EXPRESSION =
      "!active && (x > 0 || y < 0) && z >= 10 && w <= 20 && "
          + "a == b && c != d && (e + f - g * h / i % j) > 0";

  @Param({
    "SIMPLE",
    "COMPLEX",
    "LITERAL_HEAVY",
    "FUNCTION_HEAVY",
    "OPERATOR_HEAVY",
  })
  private String expressionType;

  private String expression;

  @Setup
  public void setup() {
    switch (expressionType) {
      case "SIMPLE":
        expression = SIMPLE_EXPRESSION;
        break;
      case "COMPLEX":
        expression = COMPLEX_EXPRESSION;
        break;
      case "LITERAL_HEAVY":
        expression = LITERAL_HEAVY_EXPRESSION;
        break;
      case "FUNCTION_HEAVY":
        expression = FUNCTION_HEAVY_EXPRESSION;
        break;
      case "OPERATOR_HEAVY":
        expression = OPERATOR_HEAVY_EXPRESSION;
        break;
      default:
        throw new IllegalArgumentException("Unknown expression type: " + expressionType);
    }
  }

  @Benchmark
  public void optimizedTokenizer(Blackhole bh) {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer(expression);
    while (tokenizer.next() != EOE) {
      bh.consume(tokenizer.getToken());
    }
  }

  @Benchmark
  public void classicTokenizer(Blackhole bh) {
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer(expression);
    while (tokenizer.next() != EOE) {
      bh.consume(tokenizer.getToken());
    }
  }
}
