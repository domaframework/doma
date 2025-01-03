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
package org.seasar.doma.internal.expr.node;

import static org.seasar.doma.internal.util.AssertionUtil.*;

public abstract class AbstractArithmeticOperatorNode implements ArithmeticOperatorNode {

  protected final ExpressionLocation location;

  protected final String expression;

  protected final int priority;

  protected ExpressionNode leftNode;

  protected ExpressionNode rightNode;

  protected AbstractArithmeticOperatorNode(
      ExpressionLocation location, String expression, int priority) {
    assertNotNull(location, expression);
    this.location = location;
    this.expression = expression;
    this.priority = priority;
  }

  @Override
  public ExpressionNode getLeftNode() {
    return leftNode;
  }

  public void setLeftNode(ExpressionNode leftNode) {
    this.leftNode = leftNode;
  }

  @Override
  public ExpressionNode getRightNode() {
    return rightNode;
  }

  public void setRightNode(ExpressionNode rightNode) {
    this.rightNode = rightNode;
  }

  @Override
  public ExpressionLocation getLocation() {
    return location;
  }

  @Override
  public String getExpression() {
    return expression;
  }

  @Override
  public int getPriority() {
    return priority;
  }

  @Override
  public String toString() {
    return leftNode + " " + expression + " " + rightNode;
  }
}
