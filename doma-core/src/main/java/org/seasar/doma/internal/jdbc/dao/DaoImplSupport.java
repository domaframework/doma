/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.jdbc.dao;

import java.lang.reflect.Method;
import java.sql.Connection;
import javax.sql.DataSource;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.RuntimeConfig;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.internal.util.MethodUtil;
import org.seasar.doma.jdbc.CommandImplementors;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.ConfigException;
import org.seasar.doma.jdbc.ConfigProvider;
import org.seasar.doma.jdbc.DaoMethodNotFoundException;
import org.seasar.doma.jdbc.QueryImplementors;

public class DaoImplSupport implements ConfigProvider {

  private final Config __config;

  public DaoImplSupport(Config config) {
    if (config == null) {
      throw new DomaNullPointerException("config");
    }
    validateConfig(config, null);
    this.__config = new RuntimeConfig(config, config.getDataSource());
  }

  @Deprecated(forRemoval = true)
  public DaoImplSupport(Config config, Connection connection) {
    if (config == null) {
      throw new DomaNullPointerException("config");
    }
    if (connection == null) {
      throw new DomaNullPointerException("connection");
    }
    DataSource dataSource;
    if (connection instanceof NeverClosedConnection) {
      dataSource = new NeverClosedConnectionProvider((NeverClosedConnection) connection);
    } else {
      dataSource = new NeverClosedConnectionProvider(new NeverClosedConnection(connection));
    }
    validateConfig(config, dataSource);
    this.__config = new RuntimeConfig(config, dataSource);
  }

  @Deprecated(forRemoval = true)
  public DaoImplSupport(Config config, DataSource dataSource) {
    if (config == null) {
      throw new DomaNullPointerException("config");
    }
    if (dataSource == null) {
      throw new DomaNullPointerException("dataSource");
    }
    validateConfig(config, dataSource);
    this.__config = new RuntimeConfig(config, dataSource);
  }

  private void validateConfig(Config config, DataSource dataSource) {
    if (dataSource == null) {
      if (config.getDataSource() == null) {
        throw new ConfigException(config.getClass().getName(), "getDataSource");
      }
    }
    if (config.getDialect() == null) {
      throw new ConfigException(config.getClass().getName(), "getDialect");
    }
    if (config.getSqlFileRepository() == null) {
      throw new ConfigException(config.getClass().getName(), "getSqlFileRepository");
    }
    if (config.getJdbcLogger() == null) {
      throw new ConfigException(config.getClass().getName(), "getJdbcLogger");
    }
    if (config.getCommandImplementors() == null) {
      throw new ConfigException(config.getClass().getName(), "getCommandImplementors");
    }
    if (config.getQueryImplementors() == null) {
      throw new ConfigException(config.getClass().getName(), "getQueryImplementors");
    }
    if (config.getExceptionSqlLogType() == null) {
      throw new ConfigException(config.getClass().getName(), "getExceptionSqlLogType");
    }
    if (config.getRequiresNewController() == null) {
      throw new ConfigException(config.getClass().getName(), "getRequiresNewController");
    }
    if (config.getClassHelper() == null) {
      throw new ConfigException(config.getClass().getName(), "getClassHelper");
    }
    if (config.getUnknownColumnHandler() == null) {
      throw new ConfigException(config.getClass().getName(), "getUnknownColumnHandler");
    }
    if (config.getNaming() == null) {
      throw new ConfigException(config.getClass().getName(), "getNaming");
    }
    if (config.getMapKeyNaming() == null) {
      throw new ConfigException(config.getClass().getName(), "getMapKeyNaming");
    }
    if (config.getCommenter() == null) {
      throw new ConfigException(config.getClass().getName(), "getCommenter");
    }
    if (config.getEntityListenerProvider() == null) {
      throw new ConfigException(config.getClass().getName(), "getEntityListenerProvider");
    }
    if (config.getSqlBuilderSettings() == null) {
      throw new ConfigException(config.getClass().getName(), "getSqlBuilderSettings");
    }
    if (config.getStatisticManager() == null) {
      throw new ConfigException(config.getClass().getName(), "getStatisticManager");
    }
  }

  @Override
  public Config getConfig() {
    return __config;
  }

  public DataSource getDataSource() {
    return __config.getDataSource();
  }

  public CommandImplementors getCommandImplementors() {
    return __config.getCommandImplementors();
  }

  public QueryImplementors getQueryImplementors() {
    return __config.getQueryImplementors();
  }

  public void entering(String callerClassName, String callerMethodName, Object... args) {
    __config.getJdbcLogger().logDaoMethodEntering(callerClassName, callerMethodName, args);
  }

  public void exiting(String callerClassName, String callerMethodName, Object result) {
    __config.getJdbcLogger().logDaoMethodExiting(callerClassName, callerMethodName, result);
  }

  public void throwing(String callerClassName, String callerMethodName, RuntimeException e) {
    __config.getJdbcLogger().logDaoMethodThrowing(callerClassName, callerMethodName, e);
  }

  public static <T> Method getDeclaredMethod(
      Class<T> clazz, String name, Class<?>... parameterTypes) {
    try {
      return ClassUtil.getDeclaredMethod(clazz, name, parameterTypes);
    } catch (WrapException e) {
      String signature = MethodUtil.createSignature(name, parameterTypes);
      throw new DaoMethodNotFoundException(e.getCause(), clazz.getName(), signature);
    }
  }
}
