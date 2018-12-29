package org.seasar.doma.jdbc.entity;

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/**
 * エンティティプロパティがエンティティクラスで見つからない場合にスローされる例外です。
 *
 * @author taedium
 * @since 1.20.0
 */
public class EntityPropertyNotFoundException extends JdbcException {

  private static final long serialVersionUID = 1L;

  private final String entityClassName;

  private final String entityPropertyName;

  /**
   * インスタンスを構築します。
   *
   * @param entityClassName エンティティクラスの名前
   * @param entityPropertyName エンティティプロパティの名前
   */
  public EntityPropertyNotFoundException(String entityClassName, String entityPropertyName) {
    super(Message.DOMA2211, entityClassName, entityPropertyName);
    this.entityClassName = entityClassName;
    this.entityPropertyName = entityPropertyName;
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
   * エンティティプロパティの名前を返します。
   *
   * @return エンティティプロパティの名前
   */
  public String getEntityPropertyName() {
    return entityPropertyName;
  }
}
