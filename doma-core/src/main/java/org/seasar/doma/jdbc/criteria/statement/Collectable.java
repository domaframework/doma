package org.seasar.doma.jdbc.criteria.statement;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

public interface Collectable<ELEMENT> extends Statement<List<ELEMENT>> {

  <RESULT> Statement<RESULT> stream(Function<Stream<ELEMENT>, RESULT> streamMapper);

  <RESULT> Statement<RESULT> collect(Collector<ELEMENT, ?, RESULT> collector);
}
