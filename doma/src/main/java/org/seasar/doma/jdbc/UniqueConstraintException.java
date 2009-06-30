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

import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
public class UniqueConstraintException extends JdbcException {

    private static final long serialVersionUID = 1L;

    protected final String rawSql;

    protected final String formattedSql;

    public UniqueConstraintException(Sql<?> sql, Throwable cause) {
        this(sql.getRawSql(), sql.getFormattedSql(), cause);
    }

    public UniqueConstraintException(String rawSql, String formattedSql,
            Throwable cause) {
        super(MessageCode.DOMA2004, cause, rawSql, formattedSql, cause);
        this.rawSql = rawSql;
        this.formattedSql = formattedSql;
    }

    public String getRawSql() {
        return rawSql;
    }

    public String getFormattedSql() {
        return formattedSql;
    }

}
