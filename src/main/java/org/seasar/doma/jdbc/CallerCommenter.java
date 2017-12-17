package org.seasar.doma.jdbc;

/**
 * A commenter that appends information about the caller class and the caller
 * method.
 */
public class CallerCommenter implements Commenter {

    @Override
    public String comment(String sql, CommentContext context) {
        return String.format("/** %s.%s */%n%s", context.getClassName(), context.getMethodName(),
                sql);
    }
}
