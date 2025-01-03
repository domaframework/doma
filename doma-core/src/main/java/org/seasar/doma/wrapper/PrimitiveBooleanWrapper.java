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

/** A wrapper for the {@code boolean} class. */
public class PrimitiveBooleanWrapper extends BooleanWrapper {

  private static final boolean defaultValue = false;

  public PrimitiveBooleanWrapper() {
    this(defaultValue);
  }

  public PrimitiveBooleanWrapper(boolean value) {
    super(value);
  }

  @Override
  public Boolean getDefault() {
    return false;
  }

  @Override
  protected void doSet(Boolean value) {
    this.value = value == null ? defaultValue : value;
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
    return visitor.visitPrimitiveBooleanWrapper(this, p, q);
  }
}
