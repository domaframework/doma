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
package org.seasar.doma.jdbc.criteria.declaration;

import java.util.function.Consumer;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.dialect.Dialect;

/** Represents a user-defined comparison criteria declaration. */
@FunctionalInterface
public interface UserDefinedCriteriaContext {
  /**
   * Adds a user-defined operation to the criteria declaration by accepting a function that
   * configures the provided {@code Builder}.
   *
   * @param builderBlock a {@link Consumer} used to configure the {@code Builder} for appending SQL
   *     code, expressions, or other operations
   */
  void add(Consumer<Builder> builderBlock);

  interface Builder {
    /**
     * Appends SQL code to the declaration.
     *
     * @param sql the SQL code to be appended
     */
    void appendSql(String sql);

    /**
     * cutback SQL.
     *
     * @param length the length to cutback the SQL code
     */
    void cutBackSql(int length);

    /**
     * Append a expression.
     *
     * @param propertyMetamodel the {@link PropertyMetamodel} to be added as a expression in the
     *     declaration
     */
    void appendExpression(PropertyMetamodel<?> propertyMetamodel);

    /**
     * Appends a parameter.
     *
     * @param propertyMetamodel the {@link PropertyMetamodel} to be added as a expression in the
     *     declaration
     * @param value the value of the property
     * @param <PROPERTY> the type of the property
     */
    <PROPERTY> void appendParameter(PropertyMetamodel<PROPERTY> propertyMetamodel, PROPERTY value);

    /**
     * Represents the specific database dialect. This is utilized to modify user-defined expressions
     * based on the dialect in use.
     */
    Dialect getDialect();
  }
}
