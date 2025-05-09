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
 * A provider for a {@link Config} object.
 *
 * <p>This interface defines a contract for objects that can provide access to a {@link Config}
 * instance, which contains all the configuration settings needed for database operations in Doma.
 *
 * <p>The {@link Config#get(Object)} method can be used to extract a {@link Config} instance from
 * any object that implements this interface.
 *
 * <p>Typically, applications don't need to implement this interface directly, as Doma automatically
 * generates implementations for DAO interfaces annotated with {@code @Dao}.
 *
 * @see Config
 * @see Config#get(Object)
 */
public interface ConfigProvider {

  /**
   * Returns the configuration.
   *
   * @return the configuration instance containing database operation settings
   */
  Config getConfig();
}
