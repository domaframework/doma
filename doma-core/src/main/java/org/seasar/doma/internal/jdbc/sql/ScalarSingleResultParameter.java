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

import java.util.Optional;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.jdbc.SingleResultParameter;
import org.seasar.doma.jdbc.SqlParameterVisitor;
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.wrapper.Wrapper;

public class ScalarSingleResultParameter<BASIC, CONTAINER>
    implements SingleResultParameter<BASIC, CONTAINER> {

  protected final Scalar<BASIC, CONTAINER> scalar;

  public ScalarSingleResultParameter(Scalar<BASIC, CONTAINER> scalar) {
    assertNotNull(scalar);
    this.scalar = scalar;
  }

  @Override
  public Wrapper<BASIC> getWrapper() {
    return scalar.getWrapper();
  }

  @Override
  public Object getValue() {
    return scalar.get();
  }

  @Override
  public CONTAINER getResult() {
    return scalar.get();
  }

  @Override
  public Optional<Class<?>> getDomainClass() {
    return scalar.getDomainClass();
  }

  @Override
  public Optional<JdbcType<Object>> getJdbcType() {
    return scalar.getJdbcType();
  }

  @Override
  public <R, P, TH extends Throwable> R accept(SqlParameterVisitor<R, P, TH> visitor, P p)
      throws TH {
    return visitor.visitSingleResultParameter(this, p);
  }
}
