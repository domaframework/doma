package org.seasar.doma.jdbc;

import java.lang.reflect.Method;
import java.util.Optional;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.builder.SelectBuilder;

/** A context for a comment. */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class CommentContext {

  protected final String className;

  protected final String methodName;

  protected final Config config;

  protected final Optional<Method> method;

  protected final Optional<String> message;

  /**
   * Creates an instance.
   *
   * @param className the class name that executes the SQL
   * @param methodName the method name that executes the SQL
   * @param config the configuration
   * @param method the DAO method
   * @param message the message
   */
  public CommentContext(
      String className, String methodName, Config config, Method method, String message) {
    if (className == null) {
      throw new DomaNullPointerException("className");
    }
    if (methodName == null) {
      throw new DomaNullPointerException("methodName");
    }
    if (config == null) {
      throw new DomaNullPointerException("config");
    }
    this.className = className;
    this.methodName = methodName;
    this.config = config;
    this.method = Optional.ofNullable(method);
    this.message = Optional.ofNullable(message);
  }

  /**
   * Returns the class name that executes the SQL.
   *
   * @return the class name
   */
  public String getClassName() {
    return className;
  }

  /**
   * Returns the method name that executes the SQL.
   *
   * @return the method name
   */
  public String getMethodName() {
    return methodName;
  }

  /**
   * Returns the configuration.
   *
   * @return the configuration
   */
  public Config getConfig() {
    return config;
  }

  /**
   * Returns the DAO method or {@link Optional#empty()} if the SQL is built by the builder classes
   * such as {@link SelectBuilder}.
   *
   * @return the DAO method or {@link Optional#empty()} if it does not exit
   */
  public Optional<Method> getMethod() {
    return method;
  }

  /**
   * Returns the message or {@link Optional#empty()}.
   *
   * @return the message or {@link Optional#empty()} if it does not exit
   */
  public Optional<String> getMessage() {
    return message;
  }
}
