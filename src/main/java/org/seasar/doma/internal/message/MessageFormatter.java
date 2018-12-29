package org.seasar.doma.internal.message;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import org.seasar.doma.message.MessageResource;

/** @author taedium */
public final class MessageFormatter {

  public static String getMessage(MessageResource messageResource, Object... args) {
    ResourceBundle bundle = ResourceBundle.getBundle(MessageResourceBundle.class.getName());
    String pattern = bundle.getString(messageResource.getCode());
    return MessageFormat.format("[" + messageResource.getCode() + "] " + pattern, args);
  }
}
