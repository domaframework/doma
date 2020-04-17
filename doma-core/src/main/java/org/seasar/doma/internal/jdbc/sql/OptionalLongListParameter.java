package org.seasar.doma.internal.jdbc.sql;

import java.util.List;
import java.util.OptionalLong;
import org.seasar.doma.internal.jdbc.scalar.OptionalLongScalar;

public class OptionalLongListParameter extends ScalarListParameter<Long, OptionalLong> {

  public OptionalLongListParameter(List<OptionalLong> list, String name) {
    super(() -> new OptionalLongScalar(), list, name);
  }
}
