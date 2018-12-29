package org.seasar.doma.internal.message;

import org.seasar.doma.message.Message;

/**
 * {@link Message} を扱うリソースバンドルです。
 *
 * @author taedium
 */
public class MessageResourceBundle extends AbstractMessageResourceBundle<Message> {

  /** インスタンスを構築します。 */
  public MessageResourceBundle() {
    super(Message.class);
  }
}
