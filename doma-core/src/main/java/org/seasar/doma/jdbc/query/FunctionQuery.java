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
 * An interface for database function queries.
 *
 * <p>This interface represents a query that calls a database function. It extends {@link
 * ModuleQuery} to inherit common database module functionality while specializing for function
 * calls that return a result.
 *
 * <p>Implementations of this interface handle the execution of database function calls, including
 * parameter binding and result retrieval.
 *
 * @param <RESULT> the type of the function result
 */
public interface FunctionQuery<RESULT> extends ModuleQuery {

  /**
   * Returns the result of the function call.
   *
   * <p>This method is called after executing the function to retrieve the result value. The result
   * type depends on the function's return type and the Java type mapping.
   *
   * @return the function result
   */
  RESULT getResult();
}
