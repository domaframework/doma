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

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * @author taedium
 * 
 */
public class StandardNameConvention implements NameConvention {

    @Override
    public String fromEntityToTable(String entityName, Dialect dialect) {
        if (entityName == null) {
            throw new DomaIllegalArgumentException("entityName", entityName);
        }
        if (dialect == null) {
            throw new DomaIllegalArgumentException("dialect", dialect);
        }
        return entityName;
    }

    @Override
    public String fromPropertyToColumn(String propertyName, Dialect dialect) {
        if (propertyName == null) {
            throw new DomaIllegalArgumentException("propertyName", propertyName);
        }
        if (dialect == null) {
            throw new DomaIllegalArgumentException("dialect", dialect);
        }
        return propertyName;
    }

}
