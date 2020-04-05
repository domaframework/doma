package org.seasar.doma.internal.apt.processor.dao;

import org.seasar.doma.Entity;

@Entity
public class Issue214Entity extends Issue214AbstractEntity2 {

  public static final String SATIC_FIELD = "";

  public String instanceField = "";

  public static String staticMethod() {
    return null;
  }

  @Override
  public String instanceMethod() {
    return null;
  }
}
