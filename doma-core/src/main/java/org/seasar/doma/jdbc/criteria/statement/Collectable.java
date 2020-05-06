package org.seasar.doma.jdbc.criteria.statement;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

public interface Collectable<ELEMENT> extends Statement<List<ELEMENT>>, Listable<ELEMENT> {

  <RESULT> RESULT mapStream(Function<Stream<ELEMENT>, RESULT> streamMapper);

  <RESULT> RESULT collect(Collector<ELEMENT, ?, RESULT> collector);
}
