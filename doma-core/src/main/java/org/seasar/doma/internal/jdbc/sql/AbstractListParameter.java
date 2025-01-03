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
package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import org.seasar.doma.jdbc.ListParameter;
import org.seasar.doma.jdbc.SqlParameterVisitor;

public abstract class AbstractListParameter<ELEMENT> implements ListParameter<ELEMENT> {

  protected final List<ELEMENT> list;

  protected final String name;

  public AbstractListParameter(List<ELEMENT> list, String name) {
    assertNotNull(list, name);
    this.list = list;
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void add(ELEMENT element) {
    list.add(element);
  }

  @Override
  public Object getValue() {
    return list;
  }

  @Override
  public <R, P, TH extends Throwable> R accept(SqlParameterVisitor<R, P, TH> visitor, P p)
      throws TH {
    return visitor.visitListParameter(this, p);
  }
}
