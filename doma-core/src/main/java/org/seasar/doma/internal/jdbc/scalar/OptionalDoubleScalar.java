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
