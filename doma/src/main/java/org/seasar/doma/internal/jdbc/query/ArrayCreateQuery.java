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
package org.seasar.doma.internal.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;

import org.seasar.doma.domain.Domain;

/**
 * @author taedium
 * 
 */
public class ArrayCreateQuery<R extends Domain<Array, ?>> extends
        AbstractCreateQuery<Array, R> {

    protected String typeName;

    protected Object[] elements;

    public void compile() {
        assertNotNull(config, callerClassName, callerMethodName, result, typeName, elements);
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Object[] getElements() {
        return elements;
    }

    public void setElements(Object[] elements) {
        this.elements = elements;
    }

    @Override
    public R create(Connection connection) throws SQLException {
        Array array = connection.createArrayOf(typeName, elements);
        result.set(array);
        return result;
    }
}
