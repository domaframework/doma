package org.seasar.doma.jdbc.criteria.statement;

import java.util.function.Consumer;
import java.util.function.Function;
import org.seasar.doma.jdbc.Sql;

public interface Mappable<ELEMENT> extends SetOperand<ELEMENT>, Buildable {

  Collectable<ELEMENT> map(Function<Row, ELEMENT> mapper);

  default Mappable<ELEMENT> peek(Consumer<Sql<?>> consumer) {
    consumer.accept(asSql());
    return this;
  }
}
