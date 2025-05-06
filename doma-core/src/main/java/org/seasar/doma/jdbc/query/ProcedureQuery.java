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

/**
 * An interface for database stored procedure queries.
 *
 * <p>This interface represents a query that calls a database stored procedure. It extends {@link
 * ModuleQuery} to inherit common database module functionality while specializing for procedure
 * calls that don't return a result.
 *
 * <p>Implementations of this interface handle the execution of database procedure calls, including
 * parameter binding and handling of OUT and INOUT parameters. Unlike {@link FunctionQuery},
 * procedures typically don't return a value directly, but may modify OUT parameters or perform
 * database operations.
 */
public interface ProcedureQuery extends ModuleQuery {}
