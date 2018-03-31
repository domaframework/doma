package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.internal.apt.lombok.AllArgsConstructor;

@Entity(immutable = true)
@AllArgsConstructor
public class LombokAllArgsConstructor {

  @SuppressWarnings("unused")
  private int id;

  @SuppressWarnings("unused")
  private String name;

  public LombokAllArgsConstructor(int id, String name) {}
}
