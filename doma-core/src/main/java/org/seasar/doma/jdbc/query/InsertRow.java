package org.seasar.doma.jdbc.query;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class InsertRow implements Iterable<QueryOperand> {
  private final List<QueryOperand> values;

  public InsertRow(List<QueryOperand> values) {
    this.values = Objects.requireNonNull(values);
  }

  @Override
  public Iterator<QueryOperand> iterator() {
    return values.iterator();
  }
}
