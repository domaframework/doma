package org.seasar.doma.jdbc.criteria.statement;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.seasar.doma.jdbc.Sql;

public interface Listable<ELEMENT> extends Statement<List<ELEMENT>> {

  default List<ELEMENT> fetch() {
    return execute();
  }

  default Stream<ELEMENT> stream() {
    return execute().stream();
  }

  default Optional<ELEMENT> fetchOptional() {
    return stream().findFirst();
  }

  default ELEMENT fetchOne() {
    return fetchOptional().orElse(null);
  }

  @Override
  Listable<ELEMENT> peek(Consumer<Sql<?>> consumer);
}
