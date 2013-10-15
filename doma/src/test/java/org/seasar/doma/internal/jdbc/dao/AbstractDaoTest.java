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
package org.seasar.doma.internal.jdbc.dao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.Config;

/**
 * @author backpaper0
 * 
 */
public class AbstractDaoTest extends TestCase {

    public void testConstructorParameter1() throws Exception {
        Config config = null;
        try {
            new AbstractDao(config) {
            };
            fail();
        } catch (DomaNullPointerException expected) {
            assertEquals("config", expected.getParameterName());
        }
    }

    public void testConstructorParameter2() throws Exception {
        Config config = null;
        Connection connection = mock(Connection.class);
        try {
            new AbstractDao(config, connection) {
            };
            fail();
        } catch (DomaNullPointerException expected) {
            assertEquals("config", expected.getParameterName());
        }
    }

    public void testConstructorParameter3() throws Exception {
        Config config = mock(Config.class);
        Connection connection = null;
        try {
            new AbstractDao(config, connection) {
            };
            fail();
        } catch (DomaNullPointerException expected) {
            assertEquals("connection", expected.getParameterName());
        }
    }

    public void testConstructorParameter4() throws Exception {
        Config config = null;
        DataSource dataSource = mock(DataSource.class);
        try {
            new AbstractDao(config, dataSource) {
            };
            fail();
        } catch (DomaNullPointerException expected) {
            assertEquals("config", expected.getParameterName());
        }
    }

    public void testConstructorParameter5() throws Exception {
        Config config = mock(Config.class);
        DataSource dataSource = null;
        try {
            new AbstractDao(config, dataSource) {
            };
            fail();
        } catch (DomaNullPointerException expected) {
            assertEquals("dataSource", expected.getParameterName());
        }
    }

    private <T> T mock(Class<T> aClass) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Class<?>[] interfaces = { aClass };
        InvocationHandler h = new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args)
                    throws Throwable {
                return null;
            }
        };
        return (T) Proxy.newProxyInstance(loader, interfaces, h);
    }
}
