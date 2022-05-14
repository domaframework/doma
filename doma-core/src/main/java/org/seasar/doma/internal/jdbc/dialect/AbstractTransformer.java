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
