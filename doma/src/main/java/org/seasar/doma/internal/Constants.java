/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal;

import java.nio.charset.Charset;

/**
 * @author taedium
 * 
 */
public final class Constants {

    public static final String ROWNUMBER_COLUMN_NAME = "doma_rownumber_";

    public static final Charset UTF_8 = Charset.forName("UTF-8");

    public static final String SQL_PATH_PREFIX = "META-INF/";

    public static final String SQL_PATH_SUFFIX = ".sql";

    public static final String SCRIPT_PATH_PREFIX = "META-INF/";

    public static final String SCRIPT_PATH_SUFFIX = ".script";

    public static final String METATYPE_PREFIX = "_";

    public static final String EXTERNAL_DOMAIN_METATYPE_ROOT_PACKAGE = "__";

}
