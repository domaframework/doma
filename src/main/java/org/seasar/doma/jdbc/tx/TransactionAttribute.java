package org.seasar.doma.jdbc.tx;

/**
 * トランザクション属性を示します。
 *
 * @author nakamura-to
 * @since 2.0.0
 */
public enum TransactionAttribute {

  /** 既存のトランザクションが存在する場合はそのトランザクションに参加し、存在しない場合は新規のトランザクションを作成します。 */
  REQURED,

  /**
   * 新規のトランザクションを作成します。
   *
   * <p>既存のトランザクションが存在する場合、そのトランザクションを中断し後で再開させます。
   */
  REQURES_NEW,

  /**
   * トランザクションに参加しません。
   *
   * <p>既存のトランザクションが存在する場合、そのトランザクションを中断し後で再開させます。
   */
  NOT_SUPPORTED
}
