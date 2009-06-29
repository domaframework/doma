package org.seasar.doma.message;

import static org.seasar.doma.internal.util.Assertions.*;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.seasar.doma.DomaIllegalArgumentException;


/**
 * @author taedium
 * 
 */
public final class Messages {

    protected static ResourceBundle bundle = ResourceBundle
            .getBundle(MessageResource.class.getName());

    public static String getMessage(MessageCode messageCode, Object... args) {
        if (messageCode == null) {
            new DomaIllegalArgumentException("messageCode", messageCode);
        }
        if (args == null) {
            new DomaIllegalArgumentException("args", args);
        }
        String pattern = bundle.getString(messageCode.name());
        assertNotNull(pattern);
        return MessageFormat
                .format("[" + messageCode.name() + "] " + pattern, args);
    }
}
