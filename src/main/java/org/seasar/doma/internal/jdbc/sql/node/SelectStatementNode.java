package org.seasar.doma.internal.jdbc.sql.node;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.JdbcUnsupportedOperationException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/** @author taedium */
public class SelectStatementNode extends AbstractSqlNode implements WhereClauseAwareNode {

  protected SelectClauseNode selectClauseNode;

  protected FromClauseNode fromClauseNode;

  protected WhereClauseNode whereClauseNode;

  protected GroupByClauseNode groupByClauseNode;

  protected HavingClauseNode havingClauseNode;

  protected OrderByClauseNode orderByClauseNode;

  protected ForUpdateClauseNode forUpdateClauseNode;

  protected OptionClauseNode optionClauseNode;

  public SelectClauseNode getSelectClauseNode() {
    return selectClauseNode;
  }

  public void setSelectClauseNode(SelectClauseNode selectClauseNode) {
    this.selectClauseNode = selectClauseNode;
    appendNodeInternal(selectClauseNode);
  }

  public FromClauseNode getFromClauseNode() {
    return fromClauseNode;
  }

  public void setFromClauseNode(FromClauseNode fromClauseNode) {
    this.fromClauseNode = fromClauseNode;
    appendNodeInternal(fromClauseNode);
  }

  @Override
  public WhereClauseNode getWhereClauseNode() {
    return whereClauseNode;
  }

  @Override
  public void setWhereClauseNode(WhereClauseNode whereClauseNode) {
    this.whereClauseNode = whereClauseNode;
    appendNodeInternal(whereClauseNode);
  }

  public GroupByClauseNode getGroupByClauseNode() {
    return groupByClauseNode;
  }

  public void setGroupByClauseNode(GroupByClauseNode groupByClauseNode) {
    this.groupByClauseNode = groupByClauseNode;
    appendNodeInternal(groupByClauseNode);
  }

  public HavingClauseNode getHavingClauseNode() {
    return havingClauseNode;
  }

  public void setHavingClauseNode(HavingClauseNode havingClauseNode) {
    this.havingClauseNode = havingClauseNode;
    appendNodeInternal(havingClauseNode);
  }

  public OrderByClauseNode getOrderByClauseNode() {
    return orderByClauseNode;
  }

  public void setOrderByClauseNode(OrderByClauseNode orderByClauseNode) {
    this.orderByClauseNode = orderByClauseNode;
    appendNodeInternal(orderByClauseNode);
  }

  public ForUpdateClauseNode getForUpdateClauseNode() {
    return forUpdateClauseNode;
  }

  public void setForUpdateClauseNode(ForUpdateClauseNode forUpdateClauseNode) {
    this.forUpdateClauseNode = forUpdateClauseNode;
    appendNodeInternal(forUpdateClauseNode);
  }

  public OptionClauseNode getOptionClauseNode() {
    return optionClauseNode;
  }

  public void setOptionClauseNode(OptionClauseNode optionClauseNode) {
    this.optionClauseNode = optionClauseNode;
    appendNodeInternal(optionClauseNode);
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
    return visitor.visitSelectStatementNode(this, p);
  }
}
