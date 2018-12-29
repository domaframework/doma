package org.seasar.doma.jdbc.id;

import org.seasar.doma.jdbc.JdbcException;

/**
 * データベースのシーケンスを使用するジェネレータです。
 *
 * @author taedium
 */
public interface SequenceIdGenerator extends IdGenerator {

  /**
   * シーケンスの完全修飾名を設定します。
   *
   * @param qualifiedSequenceName シーケンスの完全修飾名
   */
  void setQualifiedSequenceName(String qualifiedSequenceName);

  /**
   * 初期値を設定します。
   *
   * @param initialValue 初期値
   */
  void setInitialValue(long initialValue);

  /**
   * 割り当てサイズを設定します。
   *
   * @param allocationSize 割り当てサイズ
   */
  void setAllocationSize(long allocationSize);

  /**
   * このジェネレータを初期化します。
   *
   * @throws JdbcException 初期化に失敗した場合
   */
  void initialize();
}
