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
package org.seasar.doma.jdbc.criteria.command;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.internal.jdbc.command.AbstractStreamHandler;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.query.SelectQuery;

public class MappedResultStreamHandler<ELEMENT, RESULT>
    extends AbstractStreamHandler<ELEMENT, RESULT> {
  private final Function<SelectQuery, ObjectProvider<ELEMENT>> objectProviderFactory;

  public MappedResultStreamHandler(
      Function<Stream<ELEMENT>, RESULT> streamMapper,
      Function<SelectQuery, ObjectProvider<ELEMENT>> objectProviderFactory) {
    super(Objects.requireNonNull(streamMapper));
    this.objectProviderFactory = Objects.requireNonNull(objectProviderFactory);
  }

  @Override
  protected ObjectProvider<ELEMENT> createObjectProvider(SelectQuery query) {
    return objectProviderFactory.apply(query);
  }
}
