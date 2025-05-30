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
package org.seasar.doma.jdbc;

/**
 * Defines the types of SQL operations supported by the Doma framework.
 *
 * <p>This enum categorizes SQL statements by their operation type (SELECT, INSERT, UPDATE, etc.)
 * and execution mode (single, batch, or multi-row).
 *
 * @see Sql#getKind()
 */
public enum SqlKind {

  /** select */
  SELECT,

  /** insert */
  INSERT,

  /** update */
  UPDATE,

  /** delete */
  DELETE,

  /** batch insert */
  BATCH_INSERT,

  /** batch update */
  BATCH_UPDATE,

  /** batch delete */
  BATCH_DELETE,

  /** multi-row insert */
  MULTI_INSERT,

  /** stored procedure */
  PROCEDURE,

  /** stored function */
  FUNCTION,

  /** script */
  SCRIPT,

  /** sql processor */
  SQL_PROCESSOR,
}
