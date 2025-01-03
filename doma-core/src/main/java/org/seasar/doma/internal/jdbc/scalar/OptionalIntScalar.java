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
import java.util.OptionalInt;
import org.seasar.doma.wrapper.IntegerWrapper;
import org.seasar.doma.wrapper.Wrapper;

public class OptionalIntScalar implements Scalar<Integer, OptionalInt> {

  protected final Wrapper<Integer> wrapper;

  public OptionalIntScalar() {
    this.wrapper = new IntegerWrapper();
  }

  @Override
  public Optional<Class<?>> getDomainClass() {
    return Optional.empty();
  }

  @Override
  public OptionalInt cast(Object value) {
    return (OptionalInt) value;
  }

  @Override
  public OptionalInt get() {
    Integer value = wrapper.get();
    return value != null ? OptionalInt.of(value) : OptionalInt.empty();
  }

  @Override
  public Object getAsNonOptional() {
    return get().orElse(0);
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

  @Override
  public String toString() {
    return wrapper.toString();
  }
}
