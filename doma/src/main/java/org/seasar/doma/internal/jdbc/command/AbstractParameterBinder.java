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
package org.seasar.doma.internal.jdbc.command;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.SqlParameter;
import org.seasar.doma.wrapper.Wrapper;

/**
 * 
 * @author nakamura-to
 * 
 * @param <S>
 * @param <P>
 */
public abstract class AbstractParameterBinder<S extends PreparedStatement, P extends SqlParameter>
        implements ParameterBinder<S, P> {

    protected <BASIC> void bindInParameter(S statement,
            JdbcMappable<BASIC> parameter, int index,
            JdbcMappingVisitor jdbcMappingVisitor) throws SQLException {
        Wrapper<?> wrapper = parameter.getWrapper();
        wrapper.accept(jdbcMappingVisitor, new JdbcValueSetter(statement,
                index), parameter);
    }
}
