package org.seasar.doma.message;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.seasar.doma.DomaIllegalArgumentException;


/**
 * @author taedium
 * 
 */
public class MessageResource extends ResourceBundle {

    @Override
    public Enumeration<String> getKeys() {
        List<String> keys = new LinkedList<String>();
        for (MessageCode messageCode : EnumSet.allOf(MessageCode.class)) {
            keys.add(messageCode.message);
        }
        return Collections.enumeration(keys);
    }

    @Override
    protected Object handleGetObject(String key) {
        if (key == null) {
            new DomaIllegalArgumentException("key", key);
        }
        MessageCode m = Enum.valueOf(MessageCode.class, key);
        return m.message;
    }
}
