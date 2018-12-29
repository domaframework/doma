package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Entity;

/** @author taedium */
public class NotTopLevelImmutableEntity {

  @Entity(immutable = true)
  public static class Hoge {
    public final String name;

    public Hoge(String name) {
      this.name = name;
    }
  }
}
