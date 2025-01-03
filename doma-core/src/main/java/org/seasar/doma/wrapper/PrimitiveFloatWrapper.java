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
package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/** A wrapper for the {@code float} class. */
public class PrimitiveFloatWrapper extends FloatWrapper {

  private static final float defaultValue = 0f;

  public PrimitiveFloatWrapper() {
    this(defaultValue);
  }

  public PrimitiveFloatWrapper(float value) {
    super(value);
  }

  @Override
  protected void doSet(Float value) {
    this.value = value == null ? defaultValue : value;
  }

  @Override
  public void set(Number v) {
    set(v == null ? defaultValue : v.floatValue());
  }

  @Override
  public Float getDefault() {
    return defaultValue;
  }

  @Override
  public boolean isPrimitiveWrapper() {
    return true;
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitPrimitiveFloatWrapper(this, p, q);
  }
}
