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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.seasar.doma.internal.jdbc.command.JdbcMappable;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogFormattingFunction;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.wrapper.Wrapper;

public class PreparedSqlBuilder implements SqlContext {

    protected final List<BasicInParameter<?>> parameters = new ArrayList<>();

    protected final StringBuilder rawSql = new StringBuilder(200);

    protected final StringBuilder formattedSql = new StringBuilder(200);

    protected final Config config;

    protected final SqlKind kind;

    protected final SqlLogFormattingFunction formattingFunction;

    protected final SqlLogType sqlLogType;

    public PreparedSqlBuilder(Config config, SqlKind kind, SqlLogType sqlLogType) {
        assertNotNull(config, kind, sqlLogType);
        this.config = config;
        this.kind = kind;
        this.sqlLogType = sqlLogType;
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

    public <BASIC> void appendParameter(JdbcMappable<BASIC> parameter) {
        rawSql.append("?");
        Wrapper<BASIC> wrapper = parameter.getWrapper();
        formattedSql.append(wrapper.accept(config.getDialect()
                .getSqlLogFormattingVisitor(), formattingFunction, null));
        parameters.add(new BasicInParameter<BASIC>(() -> wrapper));
    }

    public PreparedSql build(Function<String, String> commenter) {
        assertNotNull(commenter);
        return new PreparedSql(kind, rawSql, formattedSql, null, parameters,
                sqlLogType, commenter);
    }

}
