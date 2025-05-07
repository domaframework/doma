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
package org.seasar.doma.jdbc.query;

import java.util.Objects;

/**
 * Represents a pair of query operands.
 *
 * <p>This class is used to associate two operands together, typically for operations that require
 * comparing or mapping values between them, such as in UPDATE statements where columns are set to
 * new values.
 */
public class QueryOperandPair {
  /** The left operand in the pair. */
  private final QueryOperand left;

  /** The right operand in the pair. */
  private final QueryOperand right;

  /**
   * Constructs a new {@code QueryOperandPair} with the specified operands.
   *
   * @param left the left operand
   * @param right the right operand
   * @throws NullPointerException if either operand is null
   */
  public QueryOperandPair(QueryOperand left, QueryOperand right) {
    this.left = Objects.requireNonNull(left);
    this.right = Objects.requireNonNull(right);
  }

  /**
   * Returns the left operand.
   *
   * @return the left operand
   */
  public QueryOperand getLeft() {
    return left;
  }

  /**
   * Returns the right operand.
   *
   * @return the right operand
   */
  public QueryOperand getRight() {
    return right;
  }
}
