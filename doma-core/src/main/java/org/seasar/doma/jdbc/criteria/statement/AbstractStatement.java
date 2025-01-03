/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.jdbc.criteria.statement;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import org.seasar.doma.DomaException;
import org.seasar.doma.jdbc.CommentContext;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.message.Message;

public abstract class AbstractStatement<STMT extends AbstractStatement<STMT, RESULT>, RESULT>
    implements Statement<RESULT> {

  protected static final String EXECUTE_METHOD_NAME = "execute";
  protected static final Method EXECUTE_METHOD;

  static {
    try {
      EXECUTE_METHOD = Statement.class.getMethod(EXECUTE_METHOD_NAME);
    } catch (NoSuchMethodException e) {
      throw new DomaException(Message.DOMA6005, e, EXECUTE_METHOD_NAME);
    }
  }

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

  @Override
  @SuppressWarnings("unchecked")
  public STMT peek(Consumer<Sql<?>> consumer) {
    return (STMT) Statement.super.peek(consumer);
  }

  protected Function<String, String> createCommenter(String comment) {
    return sql -> {
      CommentContext context =
          new CommentContext(
              getClass().getName(), EXECUTE_METHOD_NAME, config, EXECUTE_METHOD, comment);
      return config.getCommenter().comment(sql, context);
    };
  }

  protected abstract Command<RESULT> createCommand();
}
