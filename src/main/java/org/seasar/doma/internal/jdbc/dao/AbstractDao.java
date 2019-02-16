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

public abstract class AbstractDao implements ConfigProvider {

  protected final Config __config;

  protected AbstractDao(Config config) {
    if (config == null) {
      throw new DomaNullPointerException("config");
    }
    validateConfig(config, null);
    this.__config = new RuntimeConfig(config, config.getDataSource());
  }

  protected AbstractDao(Config config, Connection connection) {
    if (config == null) {
      throw new DomaNullPointerException("config");
    }
    if (connection == null) {
      throw new DomaNullPointerException("connection");
    }
    DataSource dataSource = null;
    if (connection instanceof NeverClosedConnection) {
      dataSource = new NeverClosedConnectionProvider((NeverClosedConnection) connection);
    } else {
      dataSource = new NeverClosedConnectionProvider(new NeverClosedConnection(connection));
    }
    validateConfig(config, dataSource);
    this.__config = new RuntimeConfig(config, dataSource);
  }

  protected AbstractDao(Config config, DataSource dataSource) {
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
  }

  @Override
  public Config getConfig() {
    return __config;
  }

  protected DataSource getDataSource() {
    return __config.getDataSource();
  }

  protected CommandImplementors getCommandImplementors() {
    return __config.getCommandImplementors();
  }

  protected QueryImplementors getQueryImplementors() {
    return __config.getQueryImplementors();
  }

  protected void entering(String callerClassName, String callerMethodName, Object... args) {
    __config.getJdbcLogger().logDaoMethodEntering(callerClassName, callerMethodName, args);
  }

  protected void exiting(String callerClassName, String callerMethodName, Object result) {
    __config.getJdbcLogger().logDaoMethodExiting(callerClassName, callerMethodName, result);
  }

  protected void throwing(String callerClassName, String callerMethodName, RuntimeException e) {
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
