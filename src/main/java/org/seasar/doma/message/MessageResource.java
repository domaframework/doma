package org.seasar.doma.message;

/**
 * A message resource.
 *
 * <p>This interface implementation instance must be thread safe.
 */
public interface MessageResource {

  /**
   * Returns a message code.
   *
   * @return a message code
   */
  String getCode();

  /**
   * Returns the string that contains replacement parameters such as {0} and {1}.
   *
   * @return the string that contains replacement parameters
   */
  String getMessagePattern();

  /**
   * Returns the message that contains a message code.
   *
   * @param args the arguments that corresponds to replacement parameters
   * @return the message
   */
  String getMessage(Object... args);

  /**
   * Returns the message that does not contains a message code.
   *
   * @param args the arguments that corresponds to replacement parameters
   * @return the message
   */
  String getSimpleMessage(Object... args);
}
