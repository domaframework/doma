package org.seasar.doma.internal.jdbc.sql.node;

import java.util.ArrayList;
import java.util.List;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.JdbcUnsupportedOperationException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

public class IfBlockNode extends AbstractSqlNode implements BlockNode {

  protected IfNode ifNode;

  protected final List<ElseifNode> elseifNodes = new ArrayList<>();

  protected ElseNode elseNode;

  protected EndNode endNode;

  public IfBlockNode() {}

  public void setIfNode(IfNode ifNode) {
    this.ifNode = ifNode;
    appendNodeInternal(ifNode);
  }

  public IfNode getIfNode() {
    return ifNode;
  }

  public void addElseifNode(ElseifNode elseIfNode) {
    elseifNodes.add(elseIfNode);
    appendNodeInternal(elseIfNode);
  }

  public List<ElseifNode> getElseifNodes() {
    return elseifNodes;
  }

  public void setElseNode(ElseNode elseNode) {
    this.elseNode = elseNode;
    appendNodeInternal(elseNode);
  }

  public ElseNode getElseNode() {
    return elseNode;
  }

  @Override
  public void setEndNode(EndNode endNode) {
    this.endNode = endNode;
    appendNodeInternal(endNode);
  }

  public EndNode getEndNode() {
    return endNode;
  }

  public boolean isElseNodeExistent() {
    return elseNode != null;
  }

  @Override
  public void appendNode(SqlNode child) {
    throw new JdbcUnsupportedOperationException(getClass().getName(), "addNode");
  }

  protected void appendNodeInternal(SqlNode child) {
    if (child != null) {
      super.appendNode(child);
    }
  }

  @Override
  public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitIfBlockNode(this, p);
  }
}
