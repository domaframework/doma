package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Entity;

/** @author nakamura-to */
public class Outer_nonPublicMiddle {

  static class Middle {
    @Entity
    public static class Inner {}
  }
}
