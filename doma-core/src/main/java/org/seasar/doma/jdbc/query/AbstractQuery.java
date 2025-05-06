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
package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.Method;
import org.seasar.doma.jdbc.CommentContext;
import org.seasar.doma.jdbc.Config;

/**
 * The base abstract class for all query implementations.
 *
 * <p>This class provides common functionality for all query types.
 */
public abstract class AbstractQuery implements Query {

  /** The class name of the caller. */
  protected String callerClassName;

  /** The method name of the caller. */
  protected String callerMethodName;

  /** The configuration. */
  protected Config config;

  /** The method that defines the query. */
  protected Method method;

  /** The query timeout in seconds. */
  protected int queryTimeout;

  /** The message to be included in SQL comments. */
  protected String message;

  /** The context for SQL comments. */
  private CommentContext commentContext;

  /** Creates a new instance. */
  protected AbstractQuery() {}

  /** {@inheritDoc} */
  @Override
  public String getClassName() {
    return callerClassName;
  }

  /**
   * Sets the class name of the caller.
   *
   * @param callerClassName the class name
   */
  public void setCallerClassName(String callerClassName) {
    this.callerClassName = callerClassName;
  }

  /** {@inheritDoc} */
  @Override
  public String getMethodName() {
    return callerMethodName;
  }

  /**
   * Sets the method name of the caller.
   *
   * @param callerMethodName the method name
   */
  public void setCallerMethodName(String callerMethodName) {
    this.callerMethodName = callerMethodName;
  }

  /** {@inheritDoc} */
  @Override
  public Config getConfig() {
    return config;
  }

  /**
   * Sets the configuration.
   *
   * @param config the configuration
   */
  public void setConfig(Config config) {
    this.config = config;
  }

  /** {@inheritDoc} */
  @Override
  public Method getMethod() {
    return method;
  }

  /**
   * Sets the method that defines the query.
   *
   * @param method the method
   */
  public void setMethod(Method method) {
    this.method = method;
  }

  /** {@inheritDoc} */
  @Override
  public int getQueryTimeout() {
    return queryTimeout;
  }

  /**
   * Sets the query timeout in seconds.
   *
   * @param queryTimeout the query timeout
   */
  public void setQueryTimeout(int queryTimeout) {
    this.queryTimeout = queryTimeout;
  }

  /**
   * Sets the message to be included in SQL comments.
   *
   * @param message the message
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /** {@inheritDoc} */
  @Override
  public void prepare() {
    assertNotNull(callerClassName, callerMethodName, config);
    commentContext = new CommentContext(callerClassName, callerMethodName, config, method, message);
  }

  /** {@inheritDoc} */
  @Override
  public String comment(String sql) {
    assertNotNull(sql, config, commentContext);
    return config.getCommenter().comment(sql, commentContext);
  }
}
