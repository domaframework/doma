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
package org.seasar.doma.internal.jdbc.util;

import java.io.File;

import org.seasar.doma.internal.Constants;
import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * @author taedium
 * 
 */
public final class SqlFileUtil {

    private static final String PREFIX = Constants.SQL_PATH_PREFIX;

    private static final String SUFFIX = Constants.SQL_PATH_SUFFIX;

    public static String buildPath(String className, String methodName) {
        return FileUtil.buildPath(PREFIX, SUFFIX, className, methodName);
    }

    public static String buildPath(String className) {
        return FileUtil.buildPath(PREFIX, SUFFIX, className);
    }

    public static boolean isSqlFile(File file, String methodName) {
        return FileUtil.isFile(PREFIX, SUFFIX, file, methodName);
    }

    public static String convertToDbmsSpecificPath(String path, Dialect dialect) {
        return FileUtil
                .convertToDbmsSpecificPath(PREFIX, SUFFIX, path, dialect);
    }
}
