package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/** A wrapper for the {@link Short} class. */
public class ShortWrapper extends AbstractWrapper<Short> implements NumberWrapper<Short> {

  public ShortWrapper() {
    super(Short.class);
  }

  public ShortWrapper(Short value) {
    super(Short.class, value);
  }

  @Override
  public void set(Number v) {
    super.set(v.shortValue());
  }

  @Override
  public void increment() {
    Short value = doGet();
    if (value != null) {
      doSet((short) (value + 1));
    }
  }

  @Override
  public void decrement() {
    Short value = doGet();
    if (value != null) {
      doSet((short) (value - 1));
    }
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitShortWrapper(this, p, q);
  }
}
