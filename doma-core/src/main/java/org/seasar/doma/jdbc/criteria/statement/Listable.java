package org.seasar.doma.jdbc.criteria.statement;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.seasar.doma.jdbc.Sql;

/**
 * Represents that the implementation can fetch data from database and list them.
 *
 * @param <ELEMENT> the listable element type
 */
public interface Listable<ELEMENT> extends Statement<List<ELEMENT>> {

  /**
   * Returns data as a list.
   *
   * @return data
   */
  default List<ELEMENT> fetch() {
    return execute();
  }

  /**
   * Returns data as a stream.
   *
   * @return data
   */
  default Stream<ELEMENT> stream() {
    return execute().stream();
  }

  /**
   * Returns the first element of data as an optional.
   *
   * @return the first element of data
   */
  default Optional<ELEMENT> fetchOptional() {
    Iterator<ELEMENT> iterator = stream().iterator();
    if (iterator.hasNext()) {
      return Optional.ofNullable(iterator.next());
    } else {
      return Optional.empty();
    }
  }

  /**
   * Returns the first element of data.
   *
   * @return the first element of data
   */
  default ELEMENT fetchOne() {
    return fetchOptional().orElse(null);
  }

  @Override
  Listable<ELEMENT> peek(Consumer<Sql<?>> consumer);
}
