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

/**
 * An interface representing an operand in a database query.
 *
 * <p>This interface provides a way to represent different types of operands that can be used in
 * database queries, such as parameters and property references. It follows the visitor pattern to
 * allow operations on these operands without modifying their structure.
 */
public interface QueryOperand {

  /**
   * Returns the entity property type associated with this operand.
   *
   * @return the entity property type
   */
  EntityPropertyType<?, ?> getEntityPropertyType();

  /**
   * Accepts a visitor to perform operations on this operand.
   *
   * @param visitor the visitor to accept
   */
  void accept(QueryOperand.Visitor visitor);

  /**
   * A parameter operand that contains both a property type and an input parameter.
   *
   * <p>This class represents a parameter value that will be bound to a query.
   */
  final class Param implements QueryOperand {
    /** The entity property type. */
    public final EntityPropertyType<?, ?> propertyType;

    /** The input parameter to be bound to the query. */
    public final InParameter<?> inParameter;

    /**
     * Constructs a new parameter operand.
     *
     * @param propertyType the entity property type
     * @param inParameter the input parameter
     * @throws NullPointerException if either parameter is null
     */
    public Param(EntityPropertyType<?, ?> propertyType, InParameter<?> inParameter) {
      this.propertyType = Objects.requireNonNull(propertyType);
      this.inParameter = Objects.requireNonNull(inParameter);
    }

    /** {@inheritDoc} */
    @Override
    public EntityPropertyType<?, ?> getEntityPropertyType() {
      return propertyType;
    }

    /** {@inheritDoc} */
    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  /**
   * A property operand that references an entity property.
   *
   * <p>This class represents a reference to an entity property in a query.
   */
  final class Prop implements QueryOperand {
    /** The entity property type. */
    public final EntityPropertyType<?, ?> propertyType;

    /**
     * Constructs a new property operand.
     *
     * @param propertyType the entity property type
     * @throws NullPointerException if the property type is null
     */
    public Prop(EntityPropertyType<?, ?> propertyType) {
      this.propertyType = Objects.requireNonNull(propertyType);
    }

    /** {@inheritDoc} */
    @Override
    public EntityPropertyType<?, ?> getEntityPropertyType() {
      return propertyType;
    }

    /** {@inheritDoc} */
    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }
  }

  /**
   * A visitor interface for {@link QueryOperand} implementations.
   *
   * <p>This interface follows the visitor pattern to allow operations on different types of query
   * operands without modifying their structure.
   */
  interface Visitor {
    /**
     * Visits a parameter operand.
     *
     * @param param the parameter operand to visit
     */
    void visit(QueryOperand.Param param);

    /**
     * Visits a property operand.
     *
     * @param prop the property operand to visit
     */
    void visit(QueryOperand.Prop prop);
  }
}
