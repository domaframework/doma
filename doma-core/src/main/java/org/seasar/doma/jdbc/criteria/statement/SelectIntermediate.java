package org.seasar.doma.jdbc.criteria.statement;

import java.util.function.Function;

public interface SelectIntermediate<ELEMENT> extends Buildable {

  SelectStatement<ELEMENT> map(Function<Row, ELEMENT> mapper);
}
