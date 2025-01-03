/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.jdbc.domain;

import java.util.Optional;
import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.internal.util.AssertionUtil;
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.wrapper.Wrapper;

public abstract class AbstractDomainType<BASIC, DOMAIN> implements DomainType<BASIC, DOMAIN> {

  protected final Supplier<Wrapper<BASIC>> wrapperSupplier;

  protected final Supplier<JdbcType<?>> jdbcTypeSupplier;

  protected AbstractDomainType(Supplier<Wrapper<BASIC>> wrapperSupplier) {
    this(wrapperSupplier, () -> null);
  }

  protected AbstractDomainType(
      Supplier<Wrapper<BASIC>> wrapperSupplier, Supplier<JdbcType<?>> jdbcTypeSupplier) {
    AssertionUtil.assertNotNull(wrapperSupplier, jdbcTypeSupplier);
    this.wrapperSupplier = wrapperSupplier;
    this.jdbcTypeSupplier = jdbcTypeSupplier;
  }

  protected abstract DOMAIN newDomain(BASIC value);

  protected abstract BASIC getBasicValue(DOMAIN domain);

  @Override
  public DomainScalar createScalar() {
    return new DomainScalar(wrapperSupplier.get(), jdbcTypeSupplier.get());
  }

  @Override
  public DomainScalar createScalar(DOMAIN value) {
    Wrapper<BASIC> wrapper = wrapperSupplier.get();
    wrapper.set(getBasicValue(value));
    return new DomainScalar(wrapper, jdbcTypeSupplier.get());
  }

  @Override
  public OptionalDomainScalar createOptionalScalar() {
    return new OptionalDomainScalar(wrapperSupplier.get(), jdbcTypeSupplier.get());
  }

  @Override
  public OptionalDomainScalar createOptionalScalar(DOMAIN value) {
    Wrapper<BASIC> wrapper = wrapperSupplier.get();
    wrapper.set(getBasicValue(value));
    return new OptionalDomainScalar(wrapper, jdbcTypeSupplier.get());
  }

  protected class DomainScalar implements Scalar<BASIC, DOMAIN> {

    protected final Wrapper<BASIC> wrapper;
    protected final JdbcType<?> jdbcType;

    protected DomainScalar(Wrapper<BASIC> wrapper, JdbcType<?> jdbcType) {
      this.wrapper = wrapper;
      this.jdbcType = jdbcType;
    }

    @Override
    public Optional<Class<?>> getDomainClass() {
      Class<?> c = AbstractDomainType.this.getDomainClass();
      return Optional.of(c);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<JdbcType<Object>> getJdbcType() {
      return Optional.ofNullable((JdbcType<Object>) jdbcType);
    }

    @Override
    public DOMAIN cast(Object value) {
      return AbstractDomainType.this.getDomainClass().cast(value);
    }

    @Override
    public DOMAIN get() {
      return newDomain(wrapper.get());
    }

    @Override
    public Object getAsNonOptional() {
      return get();
    }

    @Override
    public DOMAIN getDefault() {
      return null;
    }

    @Override
    public void set(DOMAIN domain) {
      BASIC value = getBasicValue(domain);
      wrapper.set(value);
    }

    @Override
    public Wrapper<BASIC> getWrapper() {
      return wrapper;
    }

    @Override
    public String toString() {
      return wrapper.toString();
    }
  }

  protected class OptionalDomainScalar implements Scalar<BASIC, Optional<DOMAIN>> {

    protected final Wrapper<BASIC> wrapper;
    protected final JdbcType<?> jdbcType;

    protected OptionalDomainScalar(Wrapper<BASIC> wrapper, JdbcType<?> jdbcType) {
      this.wrapper = wrapper;
      this.jdbcType = jdbcType;
    }

    @Override
    public Optional<Class<?>> getDomainClass() {
      Class<?> clazz = AbstractDomainType.this.getDomainClass();
      return Optional.of(clazz);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<JdbcType<Object>> getJdbcType() {
      return Optional.ofNullable((JdbcType<Object>) jdbcType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<DOMAIN> cast(Object value) {
      return (Optional<DOMAIN>) value;
    }

    @Override
    public Optional<DOMAIN> get() {
      BASIC value = wrapper.get();
      if (value == null) {
        return getDefaultInternal();
      }
      return Optional.of(newDomain(value));
    }

    @Override
    public Object getAsNonOptional() {
      return get().orElse(null);
    }

    @Override
    public Optional<DOMAIN> getDefault() {
      return getDefaultInternal();
    }

    protected Optional<DOMAIN> getDefaultInternal() {
      return Optional.empty();
    }

    @Override
    public void set(Optional<DOMAIN> optional) {
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

    @Override
    public String toString() {
      return wrapper.toString();
    }
  }
}
