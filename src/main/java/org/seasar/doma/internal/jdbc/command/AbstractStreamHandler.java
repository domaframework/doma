/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.seasar.doma.FetchType;
import org.seasar.doma.internal.jdbc.command.ResultSetIterator.SQLRuntimeException;
import org.seasar.doma.internal.util.IteratorUtil;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.command.ResultSetRowIndexConsumer;
import org.seasar.doma.jdbc.query.SelectQuery;

/**
 * @author nakamura-to
 * @param <TARGET> 処理対象
 * @param <RESULT> 結果
 */
public abstract class AbstractStreamHandler<TARGET, RESULT> implements ResultSetHandler<RESULT> {

  protected final Function<Stream<TARGET>, RESULT> mapper;

  public AbstractStreamHandler(Function<Stream<TARGET>, RESULT> mapper) {
    assertNotNull(mapper);
    this.mapper = mapper;
  }

  @Override
  public Supplier<RESULT> handle(
      ResultSet resultSet, SelectQuery query, ResultSetRowIndexConsumer consumer)
      throws SQLException {
    ObjectProvider<TARGET> provider = createObjectProvider(query);
    Iterator<TARGET> iterator = new ResultSetIterator<>(resultSet, query, consumer, provider);
    try {
      if (query.getFetchType() == FetchType.EAGER) {
        // consume ResultSet
        List<TARGET> list = IteratorUtil.toList(iterator);
        return () -> mapper.apply(list.stream());
      } else {
        Spliterator<TARGET> spliterator = Spliterators.spliteratorUnknownSize(iterator, 0);
        Stream<TARGET> stream = StreamSupport.stream(spliterator, false);
        RESULT result = mapper.apply(stream);
        return () -> result;
      }
    } catch (SQLRuntimeException e) {
      throw e.getCause();
    }
  }

  protected abstract ObjectProvider<TARGET> createObjectProvider(SelectQuery query);
}
