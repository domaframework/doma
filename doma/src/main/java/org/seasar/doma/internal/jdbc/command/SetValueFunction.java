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
package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.Assertions.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.domain.Domain;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcMappingFunction;
import org.seasar.doma.jdbc.type.JdbcType;

/**
 * 
 * @author taedium
 * 
 */
public class SetValueFunction implements JdbcMappingFunction {

    protected final Config config;

    protected final PreparedStatement preparedStatement;

    protected final int index;

    public SetValueFunction(Config config, PreparedStatement preparedStatement,
            int index) {
        assertNotNull(config, preparedStatement);
        assertTrue(index > 0, index);
        this.config = config;
        this.preparedStatement = preparedStatement;
        this.index = index;
    }

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public <R, V> R apply(Domain<V, ?> domain, JdbcType<V> jdbcType)
            throws SQLException {
        if (domain == null) {
            throw new DomaIllegalArgumentException("domain", domain);
        }
        if (jdbcType == null) {
            throw new DomaIllegalArgumentException("jdbcType", jdbcType);
        }
        jdbcType.setValue(preparedStatement, index, domain.get());
        return null;
    }
}
