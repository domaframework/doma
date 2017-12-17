package org.seasar.doma.message;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.seasar.doma.DomaNullPointerException;

/**
 * A resource bundle for {@link MessageResource}.
 */
public abstract class AbstractMessageResourceBundle<M extends Enum<M> & MessageResource>
        extends ResourceBundle {

    protected final Class<M> messageResourceClass;

    protected AbstractMessageResourceBundle(Class<M> messageResourceClass) {
        if (messageResourceClass == null) {
            throw new DomaNullPointerException("messageResourceClass");
        }
        this.messageResourceClass = messageResourceClass;
    }

    @Override
    public Enumeration<String> getKeys() {
        List<String> keys = new LinkedList<String>();
        for (M messageResource : EnumSet.allOf(messageResourceClass)) {
            keys.add(messageResource.getCode());
        }
        return Collections.enumeration(keys);
    }

    @Override
    protected Object handleGetObject(String key) {
        if (key == null) {
            throw new DomaNullPointerException("key");
        }
        M messageResource;
        try {
            messageResource = Enum.valueOf(messageResourceClass, key);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
        return messageResource.getMessagePattern();
    }
}
