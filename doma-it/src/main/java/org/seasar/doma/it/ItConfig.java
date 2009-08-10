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
package org.seasar.doma.it;

import javax.sql.DataSource;

import org.seasar.doma.jdbc.DomaAbstractConfig;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.NamingConvention;
import org.seasar.doma.jdbc.RequiresNewController;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.framework.container.SingletonS2Container;

public class ItConfig extends DomaAbstractConfig {

    protected static final JdbcLogger jdbcLogger = new ItLogger();

    protected static final NamingConvention namingConvention = new ItNamingConvention();

    protected static final RequiresNewController requiresNewController = new S2RequiresNewController();

    @Override
    public DataSource dataSource() {
        return SingletonS2Container.getComponent(DataSource.class);
    }

    @Override
    public Dialect dialect() {
        return SingletonS2Container.getComponent(Dialect.class);
    }

    @Override
    public JdbcLogger jdbcLogger() {
        return jdbcLogger;
    }

    @Override
    public NamingConvention namingConvention() {
        return namingConvention;
    }

    @Override
    public RequiresNewController requiresNewController() {
        return requiresNewController;
    }

}
