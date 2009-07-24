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

import org.seasar.doma.DomaMessageCode;
import org.seasar.doma.MessageCode;

/**
 * @author taedium
 * 
 */
public class OptimisticLockException extends JdbcException {

    private static final long serialVersionUID = 1L;

    protected final String rawSql;

    protected final String formattedSql;

    public OptimisticLockException(Sql<?> sql) {
        this(sql.getRawSql(), sql.getFormattedSql());
    }

    public OptimisticLockException(String rawSql, String formattedSql) {
        super(DomaMessageCode.DOMA2003, formattedSql, rawSql);
        this.rawSql = rawSql;
        this.formattedSql = formattedSql;
    }

    protected OptimisticLockException(MessageCode messageCode, String rawSql) {
        super(messageCode, rawSql);
        this.rawSql = rawSql;
        this.formattedSql = null;
    }

    public String getRawSql() {
        return rawSql;
    }

    public String getFormattedSql() {
        return formattedSql;
    }

}
