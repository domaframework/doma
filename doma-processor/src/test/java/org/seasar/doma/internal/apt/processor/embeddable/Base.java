package org.seasar.doma.internal.apt.processor.embeddable;

import org.seasar.doma.Embeddable;

@Embeddable
public class Base {

  public String aaa;

  public Base(String aaa) {
    this.aaa = aaa;
  }
}
