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
