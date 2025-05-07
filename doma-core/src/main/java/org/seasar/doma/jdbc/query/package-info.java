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
 * Provides query objects that build and execute SQL statements.
 *
 * <p>This package contains interfaces and classes that represent various types of database queries,
 * such as SELECT, INSERT, UPDATE, DELETE, batch operations, and stored procedure/function calls.
 * These query objects are responsible for building SQL statements, binding parameters, and
 * executing the statements against a database.
 *
 * <p>The main interfaces in this package include:
 *
 * <ul>
 *   <li>{@link org.seasar.doma.jdbc.query.Query} - The base interface for all query types
 *   <li>{@link org.seasar.doma.jdbc.query.SelectQuery} - For SELECT operations
 *   <li>{@link org.seasar.doma.jdbc.query.ModifyQuery} - For data modification operations
 *   <li>{@link org.seasar.doma.jdbc.query.InsertQuery} - For INSERT operations
 *   <li>{@link org.seasar.doma.jdbc.query.UpdateQuery} - For UPDATE operations
 *   <li>{@link org.seasar.doma.jdbc.query.DeleteQuery} - For DELETE operations
 *   <li>{@link org.seasar.doma.jdbc.query.BatchModifyQuery} - For batch operations
 *   <li>{@link org.seasar.doma.jdbc.query.CreateQuery} - For creating database resources
 *   <li>{@link org.seasar.doma.jdbc.query.ModuleQuery} - For calling database modules
 * </ul>
 *
 * <p>The implementation classes in this package provide concrete functionality for these
 * interfaces, handling SQL generation, parameter binding, and execution according to the specific
 * requirements of each query type.
 */
package org.seasar.doma.jdbc.query;
