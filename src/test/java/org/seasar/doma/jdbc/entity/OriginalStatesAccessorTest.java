package org.seasar.doma.jdbc.entity;

import junit.framework.TestCase;

public class OriginalStatesAccessorTest extends TestCase {

  public void testGet() throws Exception {
    var states = new Hoge();
    var hoge = new Hoge();
    hoge.setOriginalStates(states);
    var accessor = new OriginalStatesAccessor<>(Hoge.class, "originalStates");
    assertSame(states, accessor.get(hoge));
  }

  public void testSet() throws Exception {
    var states = new Hoge();
    var hoge = new Hoge();
    var accessor = new OriginalStatesAccessor<>(Hoge.class, "originalStates");
    accessor.set(hoge, states);
    assertSame(states, hoge.getOriginalStates());
  }

  public static class Hoge {

    private Hoge originalStates;

    public Hoge getOriginalStates() {
      return originalStates;
    }

    public void setOriginalStates(Hoge originalStates) {
      this.originalStates = originalStates;
    }
  }
}
