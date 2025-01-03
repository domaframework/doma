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
package org.seasar.doma.jdbc.query;

import java.util.Objects;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.entity.EntityPropertyType;

public interface QueryOperand {

  EntityPropertyType<?, ?> getEntityPropertyType();

  void accept(QueryOperand.Visitor visitor);

  final class Param implements QueryOperand {
    public final EntityPropertyType<?, ?> propertyType;
    public final InParameter<?> inParameter;

    public Param(EntityPropertyType<?, ?> propertyType, InParameter<?> inParameter) {
      this.propertyType = Objects.requireNonNull(propertyType);
      this.inParameter = Objects.requireNonNull(inParameter);
    }

    @Override
    public EntityPropertyType<?, ?> getEntityPropertyType() {
      return propertyType;
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  final class Prop implements QueryOperand {
    public final EntityPropertyType<?, ?> propertyType;

    public Prop(EntityPropertyType<?, ?> propertyType) {
      this.propertyType = Objects.requireNonNull(propertyType);
    }

    @Override
    public EntityPropertyType<?, ?> getEntityPropertyType() {
      return propertyType;
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  interface Visitor {
    void visit(QueryOperand.Param param);

    void visit(QueryOperand.Prop prop);
  }
}
