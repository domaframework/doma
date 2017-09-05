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

import java.util.List;
import java.util.function.Function;

/**
 * A prepared SQL.
 */
public class PreparedSql extends AbstractSql<InParameter<?>> {

    public PreparedSql(SqlKind kind, CharSequence rawSql, CharSequence formattedSql,
            String sqlFilePath, List<? extends InParameter<?>> parameters, SqlLogType sqlLogType) {
        this(kind, rawSql, formattedSql, sqlFilePath, parameters, sqlLogType, Function.identity());
    }

    public PreparedSql(SqlKind kind, CharSequence rawSql, CharSequence formattedSql,
            String sqlFilePath, List<? extends InParameter<?>> parameters, SqlLogType sqlLogType,
            Function<String, String> commenter) {
        super(kind, rawSql, formattedSql, sqlFilePath, parameters, sqlLogType, commenter);
    }

}
