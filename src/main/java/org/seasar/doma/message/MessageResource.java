package org.seasar.doma.message;

/**
 * メッセージのリソースを表します。
 *
 * <p>このインタフェースの実装はスレッドセーフでなければいけません。
 *
 * <p>
 *
 * @author taedium
 */
public interface MessageResource {

  /**
   * 一意のコードを返します。
   *
   * @return コード
   */
  String getCode();

  /**
   * メッセージパターンの文字列を返します。
   *
   * <p>この文字列は{0}や{1}といった置換パラメータを含みます。
   *
   * @return メッセージパターンの文字列
   */
  String getMessagePattern();

  /**
   * メッセージコードを含んだメッセージを返します。
   *
   * <p>メッセージパターンに含まれる置換パラメータは引数により解決されます。
   *
   * @param args 置換パラメータに対応する引数
   * @return メッセージ
   */
  String getMessage(Object... args);

  /**
   * メッセージコードを含まないメッセージを返します。
   *
   * <p>メッセージパターンに含まれる置換パラメータは引数により解決されます。
   *
   * @param args 置換パラメータに対応する引数
   * @return メッセージ
   */
  String getSimpleMessage(Object... args);
}
