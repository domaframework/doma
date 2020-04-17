package org.seasar.doma.jdbc;

/**
 * A commenter for SQL strings.
 *
 * <p>The comment is added to the SQL string that may be built from a SQL template or may be auto
 * generated.
 *
 * <p>The implementation must not add anything except a comment.
 */
public interface Commenter {

  /**
   * Adds the comment to the SQL string.
   *
   * @param sql the SQL string
   * @param context the context
   * @return the SQL that the comment is added
   */
  default String comment(String sql, CommentContext context) {
    return sql;
  }
}
