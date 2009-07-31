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
package org.seasar.doma.jdbc.dialect;

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.DomaMessageCode;

/**
 * @author taedium
 * 
 */
public class UnsupportedColumnTypeException extends JdbcException {

    private static final long serialVersionUID = 1L;

    protected final String typeName;

    protected final int sqlType;

    public UnsupportedColumnTypeException(String typeName, int sqlType) {
        super(DomaMessageCode.DOMA2031, typeName, sqlType);
        this.typeName = typeName;
        this.sqlType = sqlType;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getSqlType() {
        return sqlType;
    }

}
