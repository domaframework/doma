package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.internal.apt.lombok.Value;

@Entity(immutable = true)
@Value
public class LombokValue {

  @SuppressWarnings("unused")
  private int id;

  @SuppressWarnings("unused")
  private String name;

  public LombokValue(int id, String name) {}
}
