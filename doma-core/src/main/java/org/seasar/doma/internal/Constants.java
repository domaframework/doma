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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class Constants {

  public static final String ROWNUMBER_COLUMN_NAME = "doma_rownumber_";

  public static final Charset UTF_8 = StandardCharsets.UTF_8;

  public static final String SQL_PATH_PREFIX = "META-INF/";

  public static final String SQL_PATH_SUFFIX = ".sql";

  public static final String SCRIPT_PATH_PREFIX = "META-INF/";

  public static final String SCRIPT_PATH_SUFFIX = ".script";

  public static final String TYPE_PREFIX = "_";

  public static final String EXTERNAL_DOMAIN_TYPE_ROOT_PACKAGE = "__";

  public static final String EXTERNAL_DOMAIN_TYPE_ARRAY_SUFFIX = "__ARRAY__";

  public static final String TYPE_NAME_DELIMITER = "__";

  public static final String BINARY_NAME_DELIMITER = "$";

  public static final String RESERVED_IDENTIFIER_PREFIX = "__";
}
