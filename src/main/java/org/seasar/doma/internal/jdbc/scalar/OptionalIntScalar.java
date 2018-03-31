package org.seasar.doma.internal.jdbc.scalar;

import java.util.Optional;
import java.util.OptionalInt;
import org.seasar.doma.wrapper.IntegerWrapper;
import org.seasar.doma.wrapper.Wrapper;

public class OptionalIntScalar implements Scalar<Integer, OptionalInt> {

  protected final Wrapper<Integer> wrapper;

  public OptionalIntScalar() {
    this.wrapper = new IntegerWrapper();
  }

  @Override
  public Optional<Class<?>> getHolderClass() {
    return Optional.empty();
  }

  @Override
  public OptionalInt cast(Object value) {
    return (OptionalInt) value;
  }

  @Override
  public OptionalInt get() {
    var value = wrapper.get();
    return value != null ? OptionalInt.of(value) : OptionalInt.empty();
  }

  @Override
  public OptionalInt getDefault() {
    return OptionalInt.empty();
  }

  @Override
  public void set(OptionalInt optional) {
    if (optional != null && optional.isPresent()) {
      wrapper.set(optional.getAsInt());
    } else {
      wrapper.set(null);
    }
  }

  @Override
  public Wrapper<Integer> getWrapper() {
    return wrapper;
  }
}
