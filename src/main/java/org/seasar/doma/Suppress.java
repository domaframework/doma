package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.seasar.doma.message.Message;

/**
 * 警告メッセージを抑制することを示します。
 *
 * @author taedium
 * @since 1.10.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Suppress {

  /**
   * 抑制対象の警告メッセージを返します。
   *
   * @return 抑制対象の警告メッセージ
   */
  Message[] messages();
}
