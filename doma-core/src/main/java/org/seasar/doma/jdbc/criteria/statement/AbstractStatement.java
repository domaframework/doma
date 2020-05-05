package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import java.util.function.Function;
import org.seasar.doma.jdbc.CommentContext;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.command.Command;

public abstract class AbstractStatement<RESULT> implements Statement<RESULT> {

  private static final SqlLogType defaultSqlLogType = SqlLogType.FORMATTED;
  private static final String defaultComment = "";
  protected static String EXECUTE_METHOD_NAME = "execute";

  @Override
  public RESULT execute(Config config) {
    Objects.requireNonNull(config, "config");
    return execute(config, defaultSqlLogType);
  }

  @Override
  public RESULT execute(Config config, SqlLogType sqlLogType) {
    Objects.requireNonNull(config, "config");
    Objects.requireNonNull(sqlLogType, "sqlLogType");
    return execute(config, sqlLogType, defaultComment);
  }

  @Override
  public RESULT execute(Config config, SqlLogType sqlLogType, String comment) {
    Objects.requireNonNull(config, "config");
    Objects.requireNonNull(sqlLogType, "sqlLogType");
    Objects.requireNonNull(comment, "comment");
    return execute(config, comment, sqlLogType);
  }

  @Override
  public RESULT execute(Config config, String comment) {
    Objects.requireNonNull(config, "config");
    Objects.requireNonNull(comment, "comment");
    return execute(config, comment, defaultSqlLogType);
  }

  @Override
  public RESULT execute(Config config, String comment, SqlLogType sqlLogType) {
    Objects.requireNonNull(config, "config");
    Objects.requireNonNull(sqlLogType, "sqlLogType");
    Objects.requireNonNull(comment, "comment");
    Command<RESULT> command = createCommand(config, commenter(config, comment), sqlLogType);
    return command.execute();
  }

  @Override
  public Sql<?> asSql(Config config) {
    Objects.requireNonNull(config, "config");
    Command<RESULT> command =
        createCommand(config, commenter(config, defaultComment), defaultSqlLogType);
    return command.getQuery().getSql();
  }

  private Function<String, String> commenter(Config config, String comment) {
    return sql -> {
      CommentContext context =
          new CommentContext(getClass().getName(), EXECUTE_METHOD_NAME, config, null, comment);
      return config.getCommenter().comment(sql, context);
    };
  }

  protected abstract Command<RESULT> createCommand(
      Config config, Function<String, String> commenter, SqlLogType sqlLogType);
}
