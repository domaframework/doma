package org.seasar.doma.internal.apt.embeddable;

import org.seasar.doma.Embeddable;

@Embeddable
public class Derived extends Base {

  public final String bbb;

  public Derived(String aaa, String bbb) {
    super(aaa);
    this.bbb = bbb;
  }
}
