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
/**
 * Provides tuple classes that represent rows returned from criteria query executions.
 *
 * <p>The tuple classes in this package allow for type-safe access to multiple columns in query
 * results. Each tuple class (Tuple2, Tuple3, etc.) holds a specific number of elements of
 * potentially different types, corresponding to columns in the query result.
 *
 * <p>These classes are particularly useful when working with the Criteria API to select specific
 * columns rather than entire entity objects.
 *
 * @see org.seasar.doma.jdbc.criteria.query.CriteriaBuilder
 */
package org.seasar.doma.jdbc.criteria.tuple;
