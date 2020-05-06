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
  protected static final String EXECUTE_METHOD_NAME = "execute";

  protected final Config config;

  protected AbstractStatement(Config config) {
    this.config = Objects.requireNonNull(config);
  }

  @Override
  public RESULT execute() {
    Command<RESULT> command = createCommand();
    return command.execute();
  }

  @Override
  public Sql<?> asSql() {
    Command<RESULT> command = createCommand();
    return command.getQuery().getSql();
  }

  protected Function<String, String> createCommenter(String comment) {
    return sql -> {
      CommentContext context =
          new CommentContext(getClass().getName(), EXECUTE_METHOD_NAME, config, null, comment);
      return config.getCommenter().comment(sql, context);
    };
  }

  protected abstract Command<RESULT> createCommand();
}
