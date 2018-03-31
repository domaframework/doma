package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/** A wrapper for the {@link Double} class. */
public class DoubleWrapper extends AbstractWrapper<Double> implements NumberWrapper<Double> {

  public DoubleWrapper() {
    super(Double.class);
  }

  public DoubleWrapper(Double value) {
    super(Double.class, value);
  }

  @Override
  public void set(Number v) {
    super.set(v.doubleValue());
  }

  @Override
  public Double getDefault() {
    return 0d;
  }

  @Override
  public void increment() {
    var value = doGet();
    if (value != null) {
      doSet(value + 1d);
    }
  }

  @Override
  public void decrement() {
    var value = doGet();
    if (value != null) {
      doSet(value - 1d);
    }
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitDoubleWrapper(this, p, q);
  }
}
