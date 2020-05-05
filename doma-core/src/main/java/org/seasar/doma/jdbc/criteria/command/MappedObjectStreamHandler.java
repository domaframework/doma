package org.seasar.doma.jdbc.criteria.command;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.internal.jdbc.command.AbstractStreamHandler;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.criteria.def.PropertyDef;
import org.seasar.doma.jdbc.criteria.statement.Row;
import org.seasar.doma.jdbc.query.SelectQuery;

public class MappedObjectStreamHandler<ELEMENT, RESULT>
    extends AbstractStreamHandler<ELEMENT, RESULT> {
  private final List<PropertyDef<?>> propertyDefs;
  private final Function<Row, ELEMENT> mapper;

  public MappedObjectStreamHandler(
      Function<Stream<ELEMENT>, RESULT> streamMapper,
      List<PropertyDef<?>> propertyDefs,
      Function<Row, ELEMENT> rowMapper) {
    super(Objects.requireNonNull(streamMapper));
    this.propertyDefs = Objects.requireNonNull(propertyDefs);
    this.mapper = Objects.requireNonNull(rowMapper);
  }

  @Override
  protected ObjectProvider<ELEMENT> createObjectProvider(SelectQuery query) {
    return new MappedObjectProvider<>(query, propertyDefs, mapper);
  }
}
