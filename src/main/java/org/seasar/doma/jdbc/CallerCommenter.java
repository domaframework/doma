package org.seasar.doma.jdbc;

/**
 * SQLの呼び出し元に関するコメンターです。
 *
 * @author nakamura-to
 * @since 2.1.0
 */
public class CallerCommenter implements Commenter {

  @Override
  public String comment(String sql, CommentContext context) {
    return String.format("/** %s.%s */%n%s", context.getClassName(), context.getMethodName(), sql);
  }
}
