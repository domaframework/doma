package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;

public class Outer_nonPublicMiddle {

  static class Middle {
    @Entity
    public static class Inner {}
  }
}
