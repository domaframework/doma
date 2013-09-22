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
package org.seasar.doma.internal.jdbc.util;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executor;

import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.internal.util.MethodUtil;

/**
 * @author taedium
 * 
 */
public final class ConnectionUtil {

    public static void setSchema(Connection connection, String schema)
            throws SQLException {
        try {
            Method method = ClassUtil.getMethod(Connection.class, "setSchema",
                    String.class);
            MethodUtil.invoke(method, connection, schema);
        } catch (WrapException e) {
            throw new SQLException(e.getCause());
        }
    }

    public static String getSchema(Connection connection) throws SQLException {
        try {
            Method method = ClassUtil.getMethod(Connection.class, "getSchema");
            return MethodUtil.invoke(method, connection);
        } catch (WrapException e) {
            throw new SQLException(e.getCause());
        }
    }

    public static void abort(Connection connection, Executor executor)
            throws SQLException {
        try {
            Method method = ClassUtil.getMethod(Connection.class, "abort",
                    Executor.class);
            MethodUtil.invoke(method, connection, executor);
        } catch (WrapException e) {
            throw new SQLException(e.getCause());
        }
    }

    public static void setNetworkTimeout(Connection connection,
            Executor executor, int milliseconds) throws SQLException {
        try {
            Method method = ClassUtil.getMethod(Connection.class,
                    "setNetworkTimeout", Executor.class, int.class);
            MethodUtil.invoke(method, connection, executor, milliseconds);
        } catch (WrapException e) {
            throw new SQLException(e.getCause());
        }
    }

    public static int getNetworkTimeout(Connection connection)
            throws SQLException {
        try {
            Method method = ClassUtil.getMethod(Connection.class,
                    "getNetworkTimeout");
            Integer result = (Integer) MethodUtil.invoke(method, connection);
            return result.intValue();
        } catch (WrapException e) {
            throw new SQLException(e.getCause());
        }
    }
}
