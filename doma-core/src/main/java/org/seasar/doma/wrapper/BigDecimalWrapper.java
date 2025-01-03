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

import java.math.BigDecimal;
import java.math.BigInteger;
import org.seasar.doma.DomaNullPointerException;

/** A wrapper for the {@link BigDecimal} class. */
public class BigDecimalWrapper extends AbstractWrapper<BigDecimal>
    implements NumberWrapper<BigDecimal> {

  public BigDecimalWrapper() {
    super(BigDecimal.class);
  }

  public BigDecimalWrapper(BigDecimal value) {
    super(BigDecimal.class, value);
  }

  @Override
  public void set(Number v) {
    if (v == null) {
      super.set(null);
    } else if (v instanceof BigDecimal) {
      super.set((BigDecimal) v);
    } else if (v instanceof BigInteger) {
      super.set(new BigDecimal((BigInteger) v));
    } else {
      super.set(BigDecimal.valueOf(v.doubleValue()));
    }
  }

  @Override
  public void increment() {
    BigDecimal value = doGet();
    if (value != null) {
      doSet(value.add(BigDecimal.ONE));
    }
  }

  @Override
  public void decrement() {
    BigDecimal value = doGet();
    if (value != null) {
      doSet(value.subtract(BigDecimal.ONE));
    }
  }

  @Override
  protected boolean doHasEqualValue(Object otherValue) {
    BigDecimal value = doGet();
    if (value == null) {
      return otherValue == null;
    }
    if (otherValue instanceof BigDecimal) {
      return value.compareTo((BigDecimal) otherValue) == 0;
    }
    return false;
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitBigDecimalWrapper(this, p, q);
  }
}
