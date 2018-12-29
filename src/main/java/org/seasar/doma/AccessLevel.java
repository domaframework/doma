package org.seasar.doma;

/**
 * Javaのアクセスレベルを表します。
 *
 * @author nakamura-to
 * @since 2.0.0
 */
public enum AccessLevel {

  /** {@code public} */
  PUBLIC("public"),

  /** {@code protected} */
  PROTECTED("protected"),

  /** パッケージプライベート */
  PACKAGE("");

  private final String modifier;

  private AccessLevel(String modifier) {
    this.modifier = modifier;
  }

  /**
   * 修飾子を返します。
   *
   * @return 修飾子
   */
  public String getModifier() {
    return modifier;
  }
}
