package org.seasar.doma.internal.jdbc.sql;

import java.util.OptionalLong;
import org.seasar.doma.internal.jdbc.scalar.OptionalLongScalar;

public class OptionalLongSingleResultParameter
    extends ScalarSingleResultParameter<Long, OptionalLong> {

  public OptionalLongSingleResultParameter() {
    super(new OptionalLongScalar());
  }
}
