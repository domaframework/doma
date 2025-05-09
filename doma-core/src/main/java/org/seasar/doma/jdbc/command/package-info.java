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
 * Provides classes and interfaces to execute SQL statements using the command pattern.
 *
 * <p>This package implements the command pattern for database operations, where each command
 * encapsulates a specific database operation (such as select, insert, update, delete) along with
 * its execution logic. Commands are responsible for executing queries and processing their results.
 *
 * <p>Commands work with query objects that contain SQL statements and parameters. They handle the
 * execution of SQL, processing of results, and resource management.
 */
package org.seasar.doma.jdbc.command;
