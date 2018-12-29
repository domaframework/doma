package org.seasar.doma;

import org.seasar.doma.message.Message;

/**
 * {@code null} でないことを期待されたパラメータへの引数が {@code null} の場合にスローされる例外です。
 *
 * <p>{@link NullPointerException} とは別にこの例外を定義しているのは、 {@literal Doma}のバグによる例外なのか、 {@literal
 * Doma}のAPIの事前条件を満たしていないことによる例外なのかを判別しやすくするためです。
 *
 * @author taedium
 */
public class DomaNullPointerException extends DomaException {

  private static final long serialVersionUID = 1L;

  /** {@code null} であるパラメータの名前 */
  protected final String parameterName;

  /**
   * インスタンスを構築します。
   *
   * @param parameterName {@code null} であるパラメータの名前
   */
  public DomaNullPointerException(String parameterName) {
    super(Message.DOMA0001, parameterName);
    this.parameterName = parameterName;
  }

  /**
   * {@code null} であるパラメータの名前を返します。
   *
   * @return {@code null} であるパラメータの名前
   */
  public String getParameterName() {
    return parameterName;
  }
}
