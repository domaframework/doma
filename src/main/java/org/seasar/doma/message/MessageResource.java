package org.seasar.doma.message;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * A message resource.
 * <p>
 * This interface implementation instance must be thread safe.
 */
public interface MessageResource {

    /**
     * Returns a message code.
     * 
     * @return a message code
     */
    String getCode();

    /**
     * Returns the string that contains replacement parameters such as {0} and
     * {1}.
     * 
     * @return the string that contains replacement parameters
     */
    String getMessagePattern();

    /**
     * Returns the message that contains a message code.
     * 
     * @param args
     *            the arguments that corresponds to replacement parameters
     * @return the message
     */
    default String getMessage(Object... args) {
        String simpleMessage = getSimpleMessageInternal(args);
        String code = getCode();
        return "[" + code + "] " + simpleMessage;
    }

    /**
     * Returns the message that does not contains a message code.
     * 
     * @param args
     *            the arguments that corresponds to replacement parameters
     * @return the message
     */
    default String getSimpleMessage(Object... args) {
        return getSimpleMessageInternal(args);
    }

    private String getSimpleMessageInternal(Object... args) {
        try {
            boolean fallback = false;
            ResourceBundle bundle;
            try {
                bundle = ResourceBundle.getBundle(MessageResourceBundle.class.getName());
            } catch (MissingResourceException ignored) {
                fallback = true;
                bundle = new MessageResourceBundle();
            }
            String code = getCode();
            String pattern = bundle.getString(code);
            String message = MessageFormat.format(pattern, args);
            return fallback ? "(This is a fallback message) " + message : message;
        } catch (Throwable throwable) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            StringBuilder arguments = new StringBuilder();
            for (Object a : args) {
                arguments.append(a);
                arguments.append(", ");
            }
            return "[DOMA9001] Failed to get a message because of following error : " + sw + " : "
                    + arguments;
        }
    }

}
