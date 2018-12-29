package org.seasar.doma.jdbc;

import java.lang.reflect.Method;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.MapKeyNamingType;

/**
 * マップのキーのネーミング規約の適用を制御します。
 *
 * @author nakamura-to
 * @since 2.0.0
 */
public interface MapKeyNaming {

  /**
   * マップのキーのネーミング規約を適用します。
   *
   * @param method Daoのメソッド、クエリビルダを使った場合には {@code null}
   * @param mapKeyNamingType マップのキーのネーミング規約
   * @param text 規約が適用される文字列
   * @return 規約が適用された文字列
   * @throws DomaNullPointerException {@code mapKeyNamingType} もしくは {@code text} が {@code null} の場合
   */
  default String apply(Method method, MapKeyNamingType mapKeyNamingType, String text) {
    if (mapKeyNamingType == null) {
      throw new DomaNullPointerException("mapKeyNamingType");
    }
    if (text == null) {
      throw new DomaNullPointerException("text");
    }
    return mapKeyNamingType.apply(text);
  }
}
