package org.seasar.doma.internal.jdbc.sql;

import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.wrapper.Wrapper;

/** @author taedium */
public class BasicInOutParameter<BASIC> extends ScalarInOutParameter<BASIC, BASIC> {

  public BasicInOutParameter(Supplier<Wrapper<BASIC>> supplier, Reference<BASIC> reference) {
    super(new BasicScalar<BASIC>(supplier, false), reference);
  }
}
