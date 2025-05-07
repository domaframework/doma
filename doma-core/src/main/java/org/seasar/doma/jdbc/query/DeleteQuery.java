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
 * An interface for DELETE queries.
 *
 * <p>This interface represents a query that performs DELETE operations. It extends {@link
 * ModifyQuery} to inherit common data modification functionality while specializing for DELETE
 * operations.
 *
 * <p>Implementations of this interface handle the execution of DELETE statements, including the
 * construction of the WHERE clause and handling of optimistic concurrency control.
 */
public interface DeleteQuery extends ModifyQuery {}
