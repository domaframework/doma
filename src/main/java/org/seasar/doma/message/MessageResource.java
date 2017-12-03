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
