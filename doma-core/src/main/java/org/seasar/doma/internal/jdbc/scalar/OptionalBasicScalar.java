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
package org.seasar.doma.internal.jdbc.scalar;

import java.util.Optional;
import java.util.function.Supplier;
import org.seasar.doma.internal.util.AssertionUtil;
import org.seasar.doma.wrapper.Wrapper;

public class OptionalBasicScalar<BASIC> implements Scalar<BASIC, Optional<BASIC>> {

  protected final Wrapper<BASIC> wrapper;

  public OptionalBasicScalar(Wrapper<BASIC> wrapper) {
    AssertionUtil.assertNotNull(wrapper);
    this.wrapper = wrapper;
    AssertionUtil.assertNotNull(wrapper);
  }

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
  public Object getAsNonOptional() {
    return get().orElse(null);
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

  @Override
  public String toString() {
    return wrapper.toString();
  }
}
