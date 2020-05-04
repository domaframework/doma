package org.seasar.doma.jdbc.criteria.command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import org.seasar.doma.def.PropertyDef;
import org.seasar.doma.internal.jdbc.command.AbstractObjectProvider;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.criteria.statement.Row;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.jdbc.query.Query;

public class MappedObjectProvider<RESULT> extends AbstractObjectProvider<RESULT> {
  private final Function<Row, RESULT> mapper;
  private final JdbcMappingVisitor jdbcMappingVisitor;
  private final Map<PropertyDef<?>, Integer> indexMap;

  public MappedObjectProvider(
      Query query, List<PropertyDef<?>> propertyDefs, Function<Row, RESULT> mapper) {
    Objects.requireNonNull(query);
    Objects.requireNonNull(propertyDefs);
    Objects.requireNonNull(mapper);
    this.mapper = mapper;
    this.jdbcMappingVisitor = query.getConfig().getDialect().getJdbcMappingVisitor();
    indexMap = new HashMap<>(propertyDefs.size());
    int index = 1;
    for (PropertyDef<?> propertyDef : propertyDefs) {
      indexMap.put(propertyDef, index++);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public RESULT get(ResultSet resultSet) throws SQLException {
    Row row =
        new Row() {
          @Override
          public <PROPERTY> PROPERTY get(PropertyDef<PROPERTY> propertyDef) {
            Integer index = indexMap.get(propertyDef);
            if (index == null) {
              throw new IllegalArgumentException(
                  "The propertyDef is unknown. " + propertyDef.getName());
            }
            Property<?, ?> property = propertyDef.asType().createProperty();
            try {
              fetch(resultSet, property, index, jdbcMappingVisitor);
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
