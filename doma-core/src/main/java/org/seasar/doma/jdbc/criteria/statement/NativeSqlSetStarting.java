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

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.criteria.command.MappedResultStreamHandler;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;
import org.seasar.doma.jdbc.criteria.declaration.OrderByIndexDeclaration;
import org.seasar.doma.jdbc.query.SelectQuery;

public class NativeSqlSetStarting<ELEMENT>
    extends AbstractSetOperand<NativeSqlSetStarting<ELEMENT>, ELEMENT>
    implements SetOperator<ELEMENT> {

  private final SetOperationContext<ELEMENT> context;

  public NativeSqlSetStarting(
      Config config,
      SetOperationContext<ELEMENT> context,
      Function<SelectQuery, ObjectProvider<ELEMENT>> objectProviderFactory) {
    super(Objects.requireNonNull(config), Objects.requireNonNull(objectProviderFactory));
    this.context = Objects.requireNonNull(context);
  }

  @Override
  public SetOperand<ELEMENT> orderBy(Consumer<OrderByIndexDeclaration> block) {
    Objects.requireNonNull(block);
    OrderByIndexDeclaration declaration = new OrderByIndexDeclaration(context);
    block.accept(declaration);
    return this;
  }

  @Override
  public SetOperationContext<ELEMENT> getContext() {
    return context;
  }

  @Override
  public Stream<ELEMENT> openStream() {
    NativeSqlSetTerminal<Stream<ELEMENT>> terminal =
        createNativeSqlSetTerminal(Function.identity(), true);
    return terminal.execute();
  }

  @Override
  public <RESULT> RESULT mapStream(Function<Stream<ELEMENT>, RESULT> streamMapper) {
    Objects.requireNonNull(streamMapper);
    NativeSqlSetTerminal<RESULT> terminal = createNativeSqlSetTerminal(streamMapper, false);
    return terminal.execute();
  }

  @Override
  protected Command<List<ELEMENT>> createCommand() {
    NativeSqlSetTerminal<List<ELEMENT>> terminal =
        createNativeSqlSetTerminal(stream -> stream.collect(toList()), false);
    return terminal.createCommand();
  }

  private <RESULT> NativeSqlSetTerminal<RESULT> createNativeSqlSetTerminal(
      Function<Stream<ELEMENT>, RESULT> streamMapper, boolean returnsStream) {
    ResultSetHandler<RESULT> handler =
        new MappedResultStreamHandler<>(streamMapper, objectProviderFactory);
    return new NativeSqlSetTerminal<>(config, context, handler, returnsStream);
  }
}
