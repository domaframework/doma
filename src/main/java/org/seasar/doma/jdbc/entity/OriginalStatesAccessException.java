package org.seasar.doma.jdbc.entity;

import org.seasar.doma.OriginalStates;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/**
 * {@link OriginalStates}が注釈されたフィールドへのアクセスに失敗した場合にスローされる例外です。
 *
 * @author taedium
 * @since 1.20.0
 */
public class OriginalStatesAccessException extends JdbcException {

  private static final long serialVersionUID = 1L;

  private final String entityClassName;

  private final String fieldName;

  /**
   * インスタンスを構築します。
   *
   * @param cause 原因
   * @param entityClassName エンティティクラスの名前
   * @param fieldName フィールドの名前
   */
  public OriginalStatesAccessException(Throwable cause, String entityClassName, String fieldName) {
    super(Message.DOMA2212, cause, entityClassName, fieldName, cause);
    this.entityClassName = entityClassName;
    this.fieldName = fieldName;
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
   * フィールドの名前を返します。
   *
   * @return フィールドの名前
   */
  public String getFieldName() {
    return fieldName;
  }
}
