package org.seasar.doma.jdbc.criteria.statement;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.seasar.doma.jdbc.Sql;

public interface Listable<ELEMENT> extends Statement<List<ELEMENT>> {

  default List<ELEMENT> getResultList() {
    return execute();
  }

  default Optional<ELEMENT> getSingleResult() {
    return execute().stream().findFirst();
  }

  default Stream<ELEMENT> stream() {
    return execute().stream();
  }

  default Listable<ELEMENT> peek(Consumer<Sql<?>> consumer) {
    consumer.accept(asSql());
    return this;
  }
}
