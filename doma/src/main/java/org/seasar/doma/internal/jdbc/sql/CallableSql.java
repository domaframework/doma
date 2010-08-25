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
package org.seasar.doma.internal.jdbc.sql;

import java.util.Collections;
import java.util.List;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlKind;

/**
 * 
 * @author taedium
 * 
 */
public class CallableSql implements Sql<CallableSqlParameter> {

    protected final SqlKind kind;

    protected final String rawSql;

    protected final String formattedSql;

    protected final List<CallableSqlParameter> parameters;

    public CallableSql(SqlKind kind, CharSequence rawSql,
            CharSequence formattedSql,
            List<? extends CallableSqlParameter> parameters) {
        if (kind == null) {
            throw new DomaNullPointerException("kind");
        }
        if (rawSql == null) {
            throw new DomaNullPointerException("rawSql");
        }
        if (formattedSql == null) {
            throw new DomaNullPointerException("formattedSql");
        }
        if (parameters == null) {
            throw new DomaNullPointerException("parameters");
        }
        this.kind = kind;
        this.rawSql = rawSql.toString().trim();
        this.formattedSql = formattedSql.toString().trim();
        this.parameters = Collections.unmodifiableList(parameters);
    }

    @Override
    public SqlKind getKind() {
        return kind;
    }

    @Override
    public String getRawSql() {
        return rawSql;
    }

    @Override
    public String getFormattedSql() {
        return formattedSql;
    }

    @Override
    public String getSqlFilePath() {
        return null;
    }

    @Override
    public List<CallableSqlParameter> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return rawSql;
    }

}
