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
package org.seasar.doma;

/**
 * Defines strategies to generate identifiers for entity primary keys.
 *
 * <p>This enum is used with the {@link GeneratedValue} annotation to specify how database
 * identifiers should be automatically generated.
 *
 * @see GeneratedValue
 * @see SequenceGenerator
 * @see TableGenerator
 */
public enum GenerationType {

  /**
   * Uses a database IDENTITY column for ID generation. The database automatically assigns a unique
   * value when a row is inserted.
   */
  IDENTITY,

  /**
   * Uses a database sequence for ID generation. Requires additional configuration via {@link
   * SequenceGenerator}.
   */
  SEQUENCE,

  /**
   * Uses a database table for ID generation. Requires additional configuration via {@link
   * TableGenerator}.
   */
  TABLE
}
