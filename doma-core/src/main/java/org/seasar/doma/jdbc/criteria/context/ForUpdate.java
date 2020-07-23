package org.seasar.doma.jdbc.criteria.context;

import java.util.Objects;
import org.seasar.doma.jdbc.criteria.option.ForUpdateOption;

public class ForUpdate {
  public final ForUpdateOption option;

  public ForUpdate(ForUpdateOption option) {
    Objects.requireNonNull(option);
    this.option = option;
  }
}
