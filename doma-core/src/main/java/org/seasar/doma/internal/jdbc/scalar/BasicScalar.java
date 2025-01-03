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

public class BasicScalar<BASIC> implements Scalar<BASIC, BASIC> {

  protected final Wrapper<BASIC> wrapper;

  public BasicScalar(Wrapper<BASIC> wrapper) {
    AssertionUtil.assertNotNull(wrapper);
    this.wrapper = wrapper;
  }

  public BasicScalar(Supplier<Wrapper<BASIC>> supplier) {
    AssertionUtil.assertNotNull(supplier);
    this.wrapper = supplier.get();
    AssertionUtil.assertNotNull(this.wrapper);
  }

  @Override
  public Optional<Class<?>> getDomainClass() {
    return Optional.empty();
  }

  @Override
  public BASIC cast(Object value) {
    return wrapper.getBasicClass().cast(value);
  }

  @Override
  public BASIC get() {
    return wrapper.get();
  }

  @Override
  public Object getAsNonOptional() {
    return get();
  }

  @Override
  public BASIC getDefault() {
    return wrapper.getDefault();
  }

  @Override
  public void set(BASIC value) {
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
