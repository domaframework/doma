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

public class NewOperatorNode implements OperatorNode {

  protected static final int PRIORITY = 0;

  protected final ExpressionLocation location;

  protected final String expression;

  protected final String className;

  protected ExpressionNode parametersNode;

  public NewOperatorNode(ExpressionLocation location, String expression, String className) {
    assertNotNull(location, expression, className);
    this.location = location;
    this.expression = expression;
    this.className = className;
  }

  public String getClassName() {
    return className;
  }

  public ExpressionNode getParametersNode() {
    return parametersNode;
  }

  public void setParametersNode(ExpressionNode parametersNode) {
    this.parametersNode = parametersNode;
  }

  @Override
  public int getPriority() {
    return PRIORITY;
  }

  @Override
  public <R, P> R accept(ExpressionNodeVisitor<R, P> visitor, P p) {
    return visitor.visitNewOperatorNode(this, p);
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
  public String toString() {
    return expression + " " + className + parametersNode;
  }
}
