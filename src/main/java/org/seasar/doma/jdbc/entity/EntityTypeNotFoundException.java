package org.seasar.doma.jdbc.entity;

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/**
 * エンティティクラスに対応するメタクラスが見つからない場合にスローされる例外です。
 *
 * @author taedium
 * @since 1.8.0
 */
public class EntityTypeNotFoundException extends JdbcException {

  private static final long serialVersionUID = 1L;

  private final String entityClassName;

  private final String entityTypeClassName;

  /**
   * インスタンスを構築します。
   *
   * @param cause 原因
   * @param entityClassName エンティティクラスの名前
   * @param entityTypeClassName エンティティタイプクラスの名前
   */
  public EntityTypeNotFoundException(
      Throwable cause, String entityClassName, String entityTypeClassName) {
    super(Message.DOMA2203, cause, entityClassName, entityTypeClassName, cause);
    this.entityClassName = entityClassName;
    this.entityTypeClassName = entityTypeClassName;
  }

  /**
   * エンティティクラスの名前を返します。
   *
   * @return エンティティクラスの名前
   */
  public String getEntityClassName() {
    return entityClassName;
  }

  /**
   * エンティティタイプクラスの名前を返します。
   *
   * @return エンティティタイプクラスの名前
   */
  public String getEntityTypeClassName() {
    return entityTypeClassName;
  }
}
