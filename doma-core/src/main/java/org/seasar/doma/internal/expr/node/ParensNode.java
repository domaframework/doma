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

public class ParensNode implements ExpressionNode {

  protected final ExpressionLocation location;

  protected final ExpressionNode node;

  public ParensNode(ExpressionLocation location, ExpressionNode node) {
    assertNotNull(location);
    this.location = location;
    this.node = node;
  }

  public ExpressionNode getNode() {
    return node;
  }

  @Override
  public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
    return visitor.visitParensNode(this, p);
  }

  @Override
  public ExpressionLocation getLocation() {
    return location;
  }

  @Override
  public String getExpression() {
    return "(";
  }

  @Override
  public String toString() {
    return "(" + node + ")";
  }
}
