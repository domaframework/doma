package org.seasar.doma.jdbc.criteria.statement;

import java.util.function.Function;

public interface Mappable<ELEMENT> extends SetOperand<ELEMENT>, Buildable {

  Collectable<ELEMENT> map(Function<Row, ELEMENT> mapper);
}
