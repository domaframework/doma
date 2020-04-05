package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/** A wrapper for the {@link Float} class. */
public class FloatWrapper extends AbstractWrapper<Float> implements NumberWrapper<Float> {

  public FloatWrapper() {
    super(Float.class);
  }

  public FloatWrapper(Float value) {
    super(Float.class, value);
  }

  @Override
  public void set(Number v) {
    super.set(v.floatValue());
  }

  @Override
  public void increment() {
    Float value = doGet();
    if (value != null) {
      doSet(value + 1f);
    }
  }

  @Override
  public void decrement() {
    Float value = doGet();
    if (value != null) {
      doSet(value - 1f);
    }
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitFloatWrapper(this, p, q);
  }
}
