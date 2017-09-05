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
package org.seasar.doma.jdbc;

/**
 * An accessor for {@link SelectOptions}.
 * <p>
 * This is used only by DOMA framework.
 */
public class SelectOptionsAccessor {

    public static boolean isCount(SelectOptions options) {
        return options.count;
    }

    public static void setCountSize(SelectOptions options, long countSize) {
        options.countSize = countSize;
    }

    public static SelectForUpdateType getForUpdateType(SelectOptions options) {
        return options.forUpdateType;
    }

    public static int getWaitSeconds(SelectOptions options) {
        return options.waitSeconds;
    }

    public static String[] getAliases(SelectOptions options) {
        return options.aliases;
    }

    public static long getOffset(SelectOptions options) {
        return options.offset;
    }

    public static long getLimit(SelectOptions options) {
        return options.limit;
    }
}
