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
package org.seasar.doma.internal;

import org.seasar.doma.DomaException;
import org.seasar.doma.message.Message;

public final class Artifact {

  private static final String NAME = "Doma";

  private static final String VERSION = "3.9.1";

  public static String getName() {
    return NAME;
  }

  public static String getVersion() {
    return VERSION;
  }

  public static void validateVersion(String compileTimeVersion) {
    if (!VERSION.equals(compileTimeVersion)) {
      throw new DomaException(Message.DOMA0003, VERSION, compileTimeVersion);
    }
  }
}
