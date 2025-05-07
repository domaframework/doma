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

import java.io.Reader;
import java.net.URL;
import java.util.function.Supplier;
import org.seasar.doma.jdbc.SqlLogType;

/**
 * A query that executes SQL scripts.
 *
 * <p>This interface defines operations for executing SQL scripts from files or other sources.
 */
public interface ScriptQuery extends Query {

  /**
   * Returns the URL of the script file.
   *
   * @return the URL of the script file
   */
  URL getScriptFileUrl();

  /**
   * Returns a supplier that provides a reader for the script content.
   *
   * @return the reader supplier
   */
  Supplier<Reader> getReaderSupplier();

  /**
   * Returns the path of the script file.
   *
   * @return the script file path
   */
  String getScriptFilePath();

  /**
   * Returns the delimiter that separates SQL statements in the script.
   *
   * @return the block delimiter
   */
  String getBlockDelimiter();

  /**
   * Returns whether execution should halt on error.
   *
   * @return {@code true} if execution should halt on error
   */
  boolean getHaltOnError();

  /**
   * Returns the SQL log type for this script query.
   *
   * @return the SQL log type
   */
  SqlLogType getSqlLogType();
}
