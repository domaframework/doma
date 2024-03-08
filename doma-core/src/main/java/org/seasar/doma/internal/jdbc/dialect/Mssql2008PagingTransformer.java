package org.seasar.doma.internal.jdbc.dialect;

import java.util.Optional;
import org.seasar.doma.internal.jdbc.sql.SimpleSqlNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.AnonymousNode;
import org.seasar.doma.internal.jdbc.sql.node.DistinctNode;
import org.seasar.doma.internal.jdbc.sql.node.FragmentNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNode;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

public class Mssql2008PagingTransformer extends StandardPagingTransformer {

  public Mssql2008PagingTransformer(long offset, long limit) {
    super(offset, limit);
  }

  @Override
  public SqlNode transform(SqlNode sqlNode) {
    AnonymousNode result = new AnonymousNode();
    for (SqlNode child : sqlNode.getChildren()) {
      result.appendNode(child.accept(this, null));
    }
    return result;
  }

  @Override
  public SqlNode visitSelectStatementNode(SelectStatementNode node, Void p) {
    if (processed) {
      return node;
    }
    if (offset > 0) {
      return super.visitSelectStatementNode(node, p);
    }
    processed = true;

    return appendTopNode(node);
  }

  protected SqlNode appendTopNode(SelectStatementNode node) {
    SelectClauseNode select = new SelectClauseNode(node.getSelectClauseNode().getWordNode());
    FragmentNode top = new FragmentNode(" top (" + limit + ")");
    Optional<SqlNode> optionalDistinctNode = getDistinctNode(node.getSelectClauseNode());

    if (optionalDistinctNode.isPresent()) {
      SqlNode distinctNode = optionalDistinctNode.get();
      for (SqlNode child : node.getSelectClauseNode().getChildren()) {
        select.appendNode(child);
        if (child == distinctNode) {
          select.appendNode(top);
        }
      }
    } else {
      select.appendNode(top);
      for (SqlNode child : node.getSelectClauseNode().getChildren()) {
        select.appendNode(child);
      }
    }

    return createSelectStatementNode(node, select);
  }

  private static SelectStatementNode createSelectStatementNode(
      SelectStatementNode node, SelectClauseNode select) {
    SelectStatementNode result = new SelectStatementNode();
    result.setSelectClauseNode(select);
    result.setFromClauseNode(node.getFromClauseNode());
    result.setWhereClauseNode(node.getWhereClauseNode());
    result.setGroupByClauseNode(node.getGroupByClauseNode());
    result.setHavingClauseNode(node.getHavingClauseNode());
    result.setOrderByClauseNode(node.getOrderByClauseNode());
    result.setForUpdateClauseNode(node.getForUpdateClauseNode());
    result.setOptionClauseNode(node.getOptionClauseNode());
    return result;
  }

  private Optional<SqlNode> getDistinctNode(SelectClauseNode node) {
    SqlNodeVisitor<Boolean, Void> visitor =
        new SimpleSqlNodeVisitor<Boolean, Void>() {

          @Override
          protected Boolean defaultAction(SqlNode node, Void o) {
            return false;
          }

          @Override
          public Boolean visitDistinctNode(DistinctNode node, Void o) {
            return true;
          }
        };

    return node.getChildren().stream().filter(child -> child.accept(visitor, null)).findFirst();
  }
}
