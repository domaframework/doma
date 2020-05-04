package org.seasar.doma.jdbc.criteria.command;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.seasar.doma.def.PropertyDef;
import org.seasar.doma.internal.jdbc.command.AbstractIterationHandler;
import org.seasar.doma.internal.jdbc.command.ResultListCallback;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.criteria.statement.Row;
import org.seasar.doma.jdbc.query.SelectQuery;

public class MappedObjectIterationHandler<ELEMENT>
    extends AbstractIterationHandler<ELEMENT, List<ELEMENT>> {
  private final List<PropertyDef<?>> propertyDefs;
  private final Function<Row, ELEMENT> mapper;

  public MappedObjectIterationHandler(
      List<PropertyDef<?>> propertyDefs, Function<Row, ELEMENT> mapper) {
    super(new ResultListCallback<>());
    Objects.requireNonNull(propertyDefs);
    Objects.requireNonNull(mapper);
    this.propertyDefs = propertyDefs;
    this.mapper = mapper;
  }

  @Override
  protected ObjectProvider<ELEMENT> createObjectProvider(SelectQuery query) {
    return new MappedObjectProvider<>(query, propertyDefs, mapper);
  }
}
