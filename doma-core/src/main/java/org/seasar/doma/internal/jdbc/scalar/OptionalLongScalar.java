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
  public Object getAsNonOptional() {
    return get().orElse(0L);
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

  @Override
  public String toString() {
    return wrapper.toString();
  }
}
