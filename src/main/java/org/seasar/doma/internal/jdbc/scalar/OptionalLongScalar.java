package org.seasar.doma.internal.jdbc.scalar;

import java.util.Optional;
import java.util.OptionalLong;
import org.seasar.doma.wrapper.LongWrapper;
import org.seasar.doma.wrapper.Wrapper;

public class OptionalLongScalar implements Scalar<Long, OptionalLong> {

  protected final Wrapper<Long> wrapper;

  public OptionalLongScalar() {
    this.wrapper = new LongWrapper();
  }

  @Override
  public Optional<Class<?>> getDomainClass() {
    return Optional.empty();
  }

  @Override
  public OptionalLong cast(Object value) {
    return (OptionalLong) value;
  }

  @Override
  public OptionalLong get() {
    Long value = wrapper.get();
    return value != null ? OptionalLong.of(value) : OptionalLong.empty();
  }

  @Override
  public OptionalLong getDefault() {
    return OptionalLong.empty();
  }

  @Override
  public void set(OptionalLong optional) {
    if (optional != null && optional.isPresent()) {
      wrapper.set(optional.getAsLong());
    } else {
      wrapper.set(null);
    }
  }

  @Override
  public Wrapper<Long> getWrapper() {
    return wrapper;
  }
}
