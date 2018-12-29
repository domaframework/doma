package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.internal.apt.lombok.Value;

/** @author nakamura-to */
@Entity(immutable = true)
@Value
public class LombokValue {

  @SuppressWarnings("unused")
  private int id;

  @SuppressWarnings("unused")
  private String name;

  public LombokValue(int id, String name) {}
}
