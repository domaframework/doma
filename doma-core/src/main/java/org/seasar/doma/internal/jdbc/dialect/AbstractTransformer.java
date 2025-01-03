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
package org.seasar.doma.internal.jdbc.dialect;

import org.seasar.doma.internal.jdbc.sql.SimpleSqlNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.ElseifNode;
import org.seasar.doma.internal.jdbc.sql.node.EndNode;
import org.seasar.doma.internal.jdbc.sql.node.ForBlockNode;
import org.seasar.doma.internal.jdbc.sql.node.IfBlockNode;
import org.seasar.doma.jdbc.SqlNode;

abstract class AbstractTransformer extends SimpleSqlNodeVisitor<SqlNode, Void> {

  @Override
  protected SqlNode defaultAction(SqlNode node, Void p) {
    return node;
  }

  @Override
  public SqlNode visitForBlockNode(ForBlockNode node, Void unused) {
    ForBlockNode result = new ForBlockNode();
    result.setForNode(node.getForNode());
    result.setEndNode(visitEndNode(node.getEndNode(), unused));
    return result;
  }

  @Override
  public SqlNode visitIfBlockNode(IfBlockNode node, Void unused) {
    IfBlockNode result = new IfBlockNode();
    result.setIfNode(node.getIfNode());
    for (ElseifNode elseifNode : node.getElseifNodes()) {
      result.addElseifNode(elseifNode);
    }
    if (node.isElseNodeExistent()) {
      result.setElseNode(node.getElseNode());
    }
    result.setEndNode(visitEndNode(node.getEndNode(), unused));
    return result;
  }

  @Override
  public EndNode visitEndNode(EndNode node, Void unused) {
    EndNode result = new EndNode(node.getText());
    for (SqlNode child : node.getChildren()) {
      result.appendNode(child.accept(this, unused));
    }
    return result;
  }
}
