package org.seasar.doma.jdbc.domain;

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/**
 * ドメインクラスに対応するメタクラスが見つからない場合にスローされる例外です。
 *
 * @author taedium
 * @since 1.8.0
 */
public class DomainTypeNotFoundException extends JdbcException {

  private static final long serialVersionUID = 1L;

  private final String domainClassName;

  private final String domainTypeClassName;

  /**
   * インスタンスを構築します。
   *
   * @param cause 原因
   * @param domainClassName ドメインクラスの名前
   * @param domainTypeClassName ドメインタイプクラスの名前
   */
  public DomainTypeNotFoundException(
      Throwable cause, String domainClassName, String domainTypeClassName) {
    super(Message.DOMA2202, cause, domainClassName, domainTypeClassName, cause);
    this.domainClassName = domainClassName;
    this.domainTypeClassName = domainTypeClassName;
  }

  /**
   * ドメインクラスの名前を返します。
   *
   * @return ドメインクラスの名前
   */
  public String getDomainClassName() {
    return domainClassName;
  }

  /**
   * ドメインタイプクラスの名前を返します。
   *
   * @return ドメインタイプクラスの名前
   */
  public String getDomainTypeClassName() {
    return domainTypeClassName;
  }
}
