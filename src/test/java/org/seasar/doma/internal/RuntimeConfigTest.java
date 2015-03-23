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
package org.seasar.doma.internal;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.ConfigException;
import org.seasar.doma.jdbc.EntityListenerProvider;
import org.seasar.doma.jdbc.SimpleDataSource;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;
import org.seasar.doma.jdbc.entity.EntityListener;

/**
 * @author backpaper0
 *
 */
public class RuntimeConfigTest extends TestCase {

    public void testGetEntityListener() throws Exception {
        Config originalConfig = new MockConfig() {

            @Override
            public EntityListenerProvider getEntityListenerProvider() {
                return new EntityListenerProvider() {
                };
            }
        };

        RuntimeConfig runtimeConfig = new RuntimeConfig(originalConfig);

        MockEntityListener entityListener = runtimeConfig
                .getEntityListenerProvider().get(MockEntityListener.class,
                        MockEntityListener::new);
        assertNotNull(entityListener);
    }

    public void testGetEntityListenerNullCheck() throws Exception {
        Config originalConfig = new MockConfig() {

            @Override
            public EntityListenerProvider getEntityListenerProvider() {
                return null;
            }
        };

        RuntimeConfig runtimeConfig = new RuntimeConfig(originalConfig);

        try {
            runtimeConfig.getEntityListenerProvider().get(
                    MockEntityListener.class, MockEntityListener::new);
            fail();
        } catch (ConfigException e) {
            assertEquals(originalConfig.getClass().getName(), e.getClassName());
            assertEquals("getEntityListenerProvider", e.getMethodName());
        }
    }

    private interface MockConfig extends Config {

        @Override
        default Dialect getDialect() {
            return new StandardDialect();
        }

        @Override
        default DataSource getDataSource() {
            return new SimpleDataSource();
        }
    }

    private static class MockEntityListener implements EntityListener<Object> {
    }
}
