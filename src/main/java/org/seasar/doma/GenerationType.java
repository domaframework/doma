package org.seasar.doma;

/**
 * 識別子を生成する方法です。
 *
 * @author taedium
 * @see GeneratedValue
 */
public enum GenerationType {

  /** データベースのIDENTITYカラムを使って識別子を自動生成することを示します。 */
  IDENTITY,

  /** データベースのシーケンスを使って識別子を自動生成することを示します。 */
  SEQUENCE,

  /** データベースのテーブルを使って識別子を自動生成することを示します。 */
  TABLE
}
