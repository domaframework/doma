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

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.expr.EvaluationResult;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;

public class MethodOperatorNodeTest {

  protected final ExpressionLocation location = new ExpressionLocation("", 0);

  @Test
  public void testOneParameter() {
    MethodOperatorNode node = new MethodOperatorNode(location, ".equals", "equals");
    node.setTargetObjectNode(new VariableNode(location, "hoge"));
    CommaOperatorNode commaOperatorNode = new CommaOperatorNode(location, ",");
    commaOperatorNode.addNode(new LiteralNode(location, "\"aaa\"", "aaa", String.class));
    node.setParametersNode(commaOperatorNode);
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("hoge", new Value(String.class, "aaa"));
    EvaluationResult evaluationResult = evaluator.evaluate(node);
    assertTrue(evaluationResult.getBooleanValue());
  }
}
