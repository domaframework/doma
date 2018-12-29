package org.seasar.doma.jdbc.entity;

import junit.framework.TestCase;

public class OriginalStatesAccessorTest extends TestCase {

  public void testGet() throws Exception {
    Hoge states = new Hoge();
    Hoge hoge = new Hoge();
    hoge.setOriginalStates(states);
    OriginalStatesAccessor<Hoge> accessor =
        new OriginalStatesAccessor<OriginalStatesAccessorTest.Hoge>(Hoge.class, "originalStates");
    assertSame(states, accessor.get(hoge));
  }

  public void testSet() throws Exception {
    Hoge states = new Hoge();
    Hoge hoge = new Hoge();
    OriginalStatesAccessor<Hoge> accessor =
        new OriginalStatesAccessor<OriginalStatesAccessorTest.Hoge>(Hoge.class, "originalStates");
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
