package org.seasar.doma.jdbc.entity;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

public class OriginalStatesAccessorTest {

  @Test
  public void testGet() throws Exception {
    Hoge states = new Hoge();
    Hoge hoge = new Hoge();
    hoge.setOriginalStates(states);
    OriginalStatesAccessor<Hoge> accessor =
        new OriginalStatesAccessor<OriginalStatesAccessorTest.Hoge>(Hoge.class, "originalStates");
    assertSame(states, accessor.get(hoge));
  }

  @Test
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
