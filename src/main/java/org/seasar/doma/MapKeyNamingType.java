package org.seasar.doma;

import org.seasar.doma.internal.util.StringUtil;

/**
 * マップのキーのネーミング規約を表します。
 *
 * <p>カラム名に規約を適用し、マップのキー名を求めます。
 *
 * @author taedium
 * @since 1.17.0
 */
public enum MapKeyNamingType {

  /** 何も行いません。 */
  NONE {

    @Override
    public String apply(String text) {
      if (text == null) {
        throw new DomaNullPointerException("text");
      }
      return text;
    }
  },

  /**
   * アンダースコア区切りの文字列をキャメルケースに変換します。
   *
   * <p>たとえば、<code>AAA_BBB</code> を <code>aaaBbb</code> に変換します。
   */
  CAMEL_CASE {

    @Override
    public String apply(String text) {
      if (text == null) {
        throw new DomaNullPointerException("text");
      }
      return StringUtil.fromSnakeCaseToCamelCase(text);
    }
  },

  /**
   * 大文字に変換します。
   *
   * <p>たとえば、<code>aaaBbb</code> を <code>AAABBB</code> に変換します。
   */
  UPPER_CASE {

    @Override
    public String apply(String text) {
      if (text == null) {
        throw new DomaNullPointerException("text");
      }
      return text.toUpperCase();
    }
  },

  /**
   * 小文字に変換します。
   *
   * <p>たとえば、<code>aaaBbb</code> を <code>aaabbb</code> に変換します。
   */
  LOWER_CASE {

    @Override
    public String apply(String text) {
      if (text == null) {
        throw new DomaNullPointerException("text");
      }
      return text.toLowerCase();
    }
  };

  /**
   * ネーミング規約を適用します。
   *
   * @param text 規約が適用される文字列
   * @return 規約が適用された文字列
   */
  public abstract String apply(String text);
}
