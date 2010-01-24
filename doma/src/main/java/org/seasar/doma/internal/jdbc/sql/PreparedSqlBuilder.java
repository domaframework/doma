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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.ArrayList;
import java.util.List;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogFormattingFunction;
import org.seasar.doma.wrapper.Wrapper;

/**
 * @author taedium
 * 
 */
public class PreparedSqlBuilder {

    protected final List<BasicInParameter> parameters = new ArrayList<BasicInParameter>();

    protected final StringBuilder rawSql = new StringBuilder(200);

    protected final StringBuilder formattedSql = new StringBuilder(200);

    protected final Config config;

    protected final SqlKind kind;

    protected final SqlLogFormattingFunction formattingFunction;

    public PreparedSqlBuilder(Config config, SqlKind kind) {
        assertNotNull(config, kind);
        this.config = config;
        this.kind = kind;
        this.formattingFunction = new ConvertToLogFormatFunction();
    }

    public void appendSql(String sql) {
        rawSql.append(sql);
        formattedSql.append(sql);
    }

    public void cutBackSql(int length) {
        rawSql.setLength(rawSql.length() - length);
        formattedSql.setLength(formattedSql.length() - length);
    }

    public void appendWrapper(Wrapper<?> wrapper) {
        rawSql.append("?");
        formattedSql.append(wrapper.accept(config.getDialect()
                .getSqlLogFormattingVisitor(), formattingFunction));
        parameters.add(new BasicInParameter(wrapper));
    }

    public PreparedSql build() {
        return new PreparedSql(kind, rawSql, formattedSql, null, parameters);
    }
}
