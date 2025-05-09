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

import java.net.URL;
import org.seasar.doma.internal.util.ResourceUtil;

/**
 * An interface for loading SQL script files.
 *
 * <p>This interface is responsible for loading SQL script files that contain multiple SQL
 * statements for batch execution. These scripts are typically used for database initialization,
 * schema creation, or test data setup.
 *
 * <p>The default implementation uses {@link ResourceUtil} to load script files from the classpath,
 * but custom implementations can be provided to load scripts from different sources or with
 * different loading strategies.
 *
 * <p>Implementations of this interface can be configured through the {@link
 * Config#getScriptFileLoader()} method.
 */
public interface ScriptFileLoader {

  /**
   * Loads a script file and returns its URL.
   *
   * <p>This method takes a path to a script file and returns a URL that can be used to access the
   * file's content. The default implementation uses {@link ResourceUtil#getResource(String)} to
   * load the file from the classpath.
   *
   * @param path the path to the script file
   * @return the URL to the script file, or null if the file cannot be found
   */
  default URL loadAsURL(String path) {
    return ResourceUtil.getResource(path);
  }
}
