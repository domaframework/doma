package org.seasar.doma.it.domain;

import java.sql.Date;

public class HiredateImpl implements Hiredate {

  private final Date value;

  public HiredateImpl(final Date value) {
    this.value = value;
  }

  @Override
  public Date getValue() {
    return this.value;
  }
}
