package org.seasar.doma.internal.apt.processor.embeddable;

import org.seasar.doma.Embeddable;

@Embeddable
public abstract class AbstractEmbeddable {

  private final String aaa;

  public AbstractEmbeddable(String aaa) {
    this.aaa = aaa;
  }

  public String getAaa() {
    return aaa;
  }
}
