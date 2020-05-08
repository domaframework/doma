package org.seasar.doma.jdbc.criteria.command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Function;
import org.seasar.doma.internal.jdbc.command.AbstractObjectProvider;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.criteria.def.PropertyDef;
import org.seasar.doma.jdbc.criteria.statement.Row;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.jdbc.query.Query;

public class MappedResultProvider<RESULT> extends AbstractObjectProvider<RESULT> {
  private final Function<Row, RESULT> mapper;
  private final JdbcMappingVisitor jdbcMappingVisitor;

  public MappedResultProvider(Query query, Function<Row, RESULT> mapper) {
    Objects.requireNonNull(query);
    Objects.requireNonNull(mapper);
    this.mapper = mapper;
    this.jdbcMappingVisitor = query.getConfig().getDialect().getJdbcMappingVisitor();
  }

  @Override
  @SuppressWarnings("unchecked")
  public RESULT get(ResultSet resultSet) throws SQLException {
    Row row =
        new Row() {
          int index = 1;

          @Override
          public <PROPERTY> PROPERTY get(PropertyDef<PROPERTY> propertyDef) {
            Property<?, ?> property = propertyDef.asType().createProperty();
            try {
              fetch(resultSet, property, index++, jdbcMappingVisitor);
            } catch (SQLException e) {
              throw new UncheckedSQLException(e);
            }
            return (PROPERTY) property.get();
          }
        };
    try {
      return mapper.apply(row);
    } catch (UncheckedSQLException e) {
      throw e.getCause();
    }
  }
}
