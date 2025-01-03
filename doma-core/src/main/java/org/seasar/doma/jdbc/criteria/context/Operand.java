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
package org.seasar.doma.jdbc.criteria.context;

import java.util.Objects;
import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.internal.jdbc.scalar.Scalars;
import org.seasar.doma.internal.jdbc.sql.ScalarInParameter;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

public interface Operand {

  PropertyMetamodel<?> getPropertyMetamodel();

  <R> R accept(Visitor<R> visitor);

  final class Param implements Operand {
    private final PropertyMetamodel<?> propertyMetamodel;
    private final Object value;

    public Param(PropertyMetamodel<?> propertyMetamodel, Object value) {
      this.propertyMetamodel = Objects.requireNonNull(propertyMetamodel);
      this.value = value;
    }

    public InParameter<?> createInParameter(Config config) {
      Class<?> clazz = propertyMetamodel.asClass();
      Supplier<Scalar<?, ?>> supplier = Scalars.wrap(value, clazz, false, config.getClassHelper());
      return new ScalarInParameter<>(supplier.get());
    }

    @Override
    public PropertyMetamodel<?> getPropertyMetamodel() {
      return propertyMetamodel;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Param)) return false;
      Param param = (Param) o;
      return propertyMetamodel.equals(param.propertyMetamodel)
          && Objects.equals(value, param.value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(propertyMetamodel, value);
    }
  }

  final class Prop implements Operand {
    public final PropertyMetamodel<?> value;

    public Prop(PropertyMetamodel<?> value) {
      this.value = Objects.requireNonNull(value);
    }

    @Override
    public PropertyMetamodel<?> getPropertyMetamodel() {
      return value;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Prop)) return false;
      Prop prop = (Prop) o;
      return value.equals(prop.value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }
  }

  @SuppressWarnings("SameReturnValue")
  interface Visitor<R> {

    R visit(Param operand);

    R visit(Prop operand);
  }
}
