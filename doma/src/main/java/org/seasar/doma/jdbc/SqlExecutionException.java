/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import org.seasar.doma.MessageCode;
import org.seasar.doma.message.DomaMessageCode;

/**
 * @author taedium
 * 
 */
public class SqlExecutionException extends JdbcException {

    private static final long serialVersionUID = 1L;

    protected final String rawSql;

    protected final String formattedSql;

    protected final Throwable rootCause;

    public SqlExecutionException(Sql<?> sql, Throwable cause,
            Throwable rootCause) {
        this(sql.getRawSql(), sql.getFormattedSql(), cause, rootCause);
    }

    public SqlExecutionException(String rawSql, String formattedSql,
            Throwable cause, Throwable rootCause) {
        super(DomaMessageCode.DOMA2009, cause, formattedSql, rawSql, cause,
                rootCause);
        this.rawSql = rawSql;
        this.formattedSql = formattedSql;
        this.rootCause = rootCause;
    }

    protected SqlExecutionException(MessageCode messageCode, String rawSql,
            String formattedSql, Throwable cause, Throwable rootCause) {
        super(messageCode, cause, rawSql, cause, rootCause);
        this.rawSql = rawSql;
        this.formattedSql = null;
        this.rootCause = rootCause;
    }

    public String getRawSql() {
        return rawSql;
    }

    public String getFormattedSql() {
        return formattedSql;
    }

    public Throwable getRootCause() {
        return rootCause;
    }

}
