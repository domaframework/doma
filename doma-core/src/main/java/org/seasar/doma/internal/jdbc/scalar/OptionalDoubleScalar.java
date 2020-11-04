package org.seasar.doma.internal.jdbc.scalar;

import java.util.Optional;
import java.util.OptionalDouble;
import org.seasar.doma.wrapper.DoubleWrapper;
import org.seasar.doma.wrapper.Wrapper;

public class OptionalDoubleScalar implements Scalar<Double, OptionalDouble> {

  protected final Wrapper<Double> wrapper;

  public OptionalDoubleScalar() {
    this.wrapper = new DoubleWrapper();
  }

  @Override
  public Optional<Class<?>> getDomainClass() {
    return Optional.empty();
  }

  @Override
  public OptionalDouble cast(Object value) {
    return (OptionalDouble) value;
  }

  @Override
  public OptionalDouble get() {
    Double value = wrapper.get();
    return value != null ? OptionalDouble.of(value) : OptionalDouble.empty();
  }

  @Override
  public Object getAsNonOptional() {
    return get().orElse(0d);
  }

  @Override
  public OptionalDouble getDefault() {
    return OptionalDouble.empty();
  }

  @Override
  public void set(OptionalDouble optional) {
    if (optional != null && optional.isPresent()) {
      wrapper.set(optional.getAsDouble());
    } else {
      wrapper.set(null);
    }
  }

  @Override
  public Wrapper<Double> getWrapper() {
    return wrapper;
  }

  @Override
  public String toString() {
    return wrapper.toString();
  }
}
