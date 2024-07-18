package org.seasar.doma.internal.jdbc.sql.node;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.JdbcUnsupportedOperationException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

public class CommentNode extends AbstractSqlNode {

  protected final String comment;

  protected final CommentType commentType;

  public CommentNode(String comment, CommentType commentType) {
    assertNotNull(comment, commentType);
    this.comment = comment;
    this.commentType = commentType;
  }

  public String getComment() {
    return comment;
  }

  public CommentType getCommentType() {
    return commentType;
  }

  @Override
  public void appendNode(SqlNode child) {
    throw new JdbcUnsupportedOperationException(getClass().getName(), "addNode");
  }

  @Override
  public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitCommentNode(this, p);
  }
}
