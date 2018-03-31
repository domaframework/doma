package org.seasar.doma.jdbc.holder;

import java.util.Optional;
import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.internal.util.AssertionUtil;
import org.seasar.doma.wrapper.Wrapper;

public abstract class AbstractHolderDesc<BASIC, HOLDER> implements HolderDesc<BASIC, HOLDER> {

  protected final Supplier<Wrapper<BASIC>> wrapperSupplier;

  protected AbstractHolderDesc(Supplier<Wrapper<BASIC>> wrapperSupplier) {
    AssertionUtil.assertNotNull(wrapperSupplier);
    this.wrapperSupplier = wrapperSupplier;
  }

  protected abstract HOLDER newHolder(BASIC value);

  protected abstract BASIC getBasicValue(HOLDER holder);

  @Override
  public HolderScalar createScalar() {
    return new HolderScalar(wrapperSupplier.get());
  }

  @Override
  public HolderScalar createScalar(HOLDER value) {
    Wrapper<BASIC> wrapper = wrapperSupplier.get();
    wrapper.set(getBasicValue(value));
    return new HolderScalar(wrapper);
  }

  @Override
  public OptionalHolderScalar createOptionalScalar() {
    return new OptionalHolderScalar(wrapperSupplier.get());
  }

  @Override
  public OptionalHolderScalar createOptionalScalar(HOLDER value) {
    Wrapper<BASIC> wrapper = wrapperSupplier.get();
    wrapper.set(getBasicValue(value));
    return new OptionalHolderScalar(wrapper);
  }

  protected class HolderScalar implements Scalar<BASIC, HOLDER> {

    protected final Wrapper<BASIC> wrapper;

    protected HolderScalar(Wrapper<BASIC> wrapper) {
      this.wrapper = wrapper;
    }

    @Override
    public Optional<Class<?>> getHolderClass() {
      Class<?> c = AbstractHolderDesc.this.getHolderClass();
      return Optional.of(c);
    }

    @Override
    public HOLDER cast(Object value) {
      return AbstractHolderDesc.this.getHolderClass().cast(value);
    }

    @Override
    public HOLDER get() {
      return newHolder(wrapper.get());
    }

    @Override
    public HOLDER getDefault() {
      return null;
    }

    @Override
    public void set(HOLDER holder) {
      BASIC value = getBasicValue(holder);
      wrapper.set(value);
    }

    @Override
    public Wrapper<BASIC> getWrapper() {
      return wrapper;
    }
  }

  protected class OptionalHolderScalar implements Scalar<BASIC, Optional<HOLDER>> {

    protected final Wrapper<BASIC> wrapper;

    protected OptionalHolderScalar(Wrapper<BASIC> wrapper) {
      this.wrapper = wrapper;
    }

    @Override
    public Optional<Class<?>> getHolderClass() {
      Class<?> clazz = AbstractHolderDesc.this.getHolderClass();
      return Optional.of(clazz);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<HOLDER> cast(Object value) {
      return (Optional<HOLDER>) value;
    }

    @Override
    public Optional<HOLDER> get() {
      BASIC value = wrapper.get();
      if (value == null && !AbstractHolderDesc.this.getBasicClass().isPrimitive()) {
        return getDefaultInternal();
      }
      return Optional.of(newHolder(value));
    }

    @Override
    public Optional<HOLDER> getDefault() {
      return getDefaultInternal();
    }

    protected Optional<HOLDER> getDefaultInternal() {
      return Optional.empty();
    }

    @Override
    public void set(Optional<HOLDER> optional) {
      if (optional != null && optional.isPresent()) {
        wrapper.set(getBasicValue(optional.get()));
      } else {
        wrapper.set(null);
      }
    }

    @Override
    public Wrapper<BASIC> getWrapper() {
      return wrapper;
    }
  }
}
