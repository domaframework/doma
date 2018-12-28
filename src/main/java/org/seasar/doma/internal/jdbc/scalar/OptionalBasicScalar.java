package org.seasar.doma.internal.jdbc.scalar;

import java.util.Optional;
import java.util.function.Supplier;
import org.seasar.doma.internal.util.AssertionUtil;
import org.seasar.doma.wrapper.Wrapper;

public class OptionalBasicScalar<BASIC> implements Scalar<BASIC, Optional<BASIC>> {

  protected final Wrapper<BASIC> wrapper;

  public OptionalBasicScalar(Supplier<Wrapper<BASIC>> supplier) {
    AssertionUtil.assertNotNull(supplier);
    this.wrapper = supplier.get();
    AssertionUtil.assertNotNull(wrapper);
  }

  @Override
  public Optional<Class<?>> getDomainClass() {
    return Optional.empty();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Optional<BASIC> cast(Object value) {
    return (Optional<BASIC>) value;
  }

  @Override
  public Optional<BASIC> get() {
    return Optional.ofNullable(wrapper.get());
  }

  @Override
  public Optional<BASIC> getDefault() {
    return Optional.empty();
  }

  @Override
  public void set(Optional<BASIC> optional) {
    if (optional == null) {
      wrapper.set(null);
    } else {
      wrapper.set(optional.orElse(null));
    }
  }

  @Override
  public Wrapper<BASIC> getWrapper() {
    return wrapper;
  }
}
