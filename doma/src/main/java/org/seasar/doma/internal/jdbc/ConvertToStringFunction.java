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
package org.seasar.doma.internal.jdbc;

import static org.seasar.doma.internal.util.Assertions.*;

import org.seasar.doma.domain.Domain;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcType;
import org.seasar.doma.jdbc.SqlLogFormattingFunction;

/**
 * @author taedium
 * 
 */
public class ConvertToStringFunction implements SqlLogFormattingFunction {

    protected final Config config;

    public ConvertToStringFunction(Config config) {
        assertNotNull(config);
        this.config = config;
    }

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public <V> String apply(Domain<V, ?> domain, JdbcType<V> jdbcType) {
        return jdbcType.convertToLogFormat(domain.get());
    }

}
