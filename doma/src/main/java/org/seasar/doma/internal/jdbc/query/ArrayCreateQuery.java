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
package org.seasar.doma.internal.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author taedium
 * 
 */
public class ArrayCreateQuery extends AbstractCreateQuery<Array> {

    protected String typeName;

    protected Object[] elements;

    @Override
    public void prepare() {
        assertNotNull(config, callerClassName, callerMethodName, typeName,
                elements);
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
    public Array create(Connection connection) throws SQLException {
        return connection.createArrayOf(typeName, elements);
    }
}
