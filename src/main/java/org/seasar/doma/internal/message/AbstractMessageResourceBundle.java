package org.seasar.doma.internal.message;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.message.MessageResource;

/**
 * {@link Enum} で表現されたメッセージコードを扱うリソースバンドルです。
 *
 * @author taedium
 */
public abstract class AbstractMessageResourceBundle<M extends Enum<M> & MessageResource>
    extends ResourceBundle {

  protected final Class<M> messageCodeClass;

  protected AbstractMessageResourceBundle(Class<M> messageCodeClass) {
    if (messageCodeClass == null) {
      throw new DomaNullPointerException("messageCodeClass");
    }
    this.messageCodeClass = messageCodeClass;
  }

  @Override
  public Enumeration<String> getKeys() {
    List<String> keys = new LinkedList<String>();
    for (M messageCode : EnumSet.allOf(messageCodeClass)) {
      keys.add(messageCode.getCode());
    }
    return Collections.enumeration(keys);
  }

  @Override
  protected Object handleGetObject(String key) {
    if (key == null) {
      throw new DomaNullPointerException("key");
    }
    M messageCode = Enum.valueOf(messageCodeClass, key);
    return messageCode.getMessagePattern();
  }
}
