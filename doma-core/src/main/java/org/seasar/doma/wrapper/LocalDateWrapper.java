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

import java.time.LocalDate;
import org.seasar.doma.DomaNullPointerException;

/** A wrapper for the {@link LocalDate} class. */
public class LocalDateWrapper extends AbstractWrapper<LocalDate> {

  public LocalDateWrapper() {
    super(LocalDate.class);
  }

  public LocalDateWrapper(LocalDate value) {
    super(LocalDate.class, value);
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitLocalDateWrapper(this, p, q);
  }
}
