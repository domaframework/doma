package org.seasar.doma.jdbc.id;

import org.seasar.doma.jdbc.JdbcException;

/**
 * データベースのテーブルを使用するジェネレータです。
 *
 * <p>
 *
 * @author taedium
 */
public interface TableIdGenerator extends IdGenerator {

  /**
   * テーブルの完全修飾名を設定します。
   *
   * @param qualifiedTableName テーブルの完全修飾名
   */
  void setQualifiedTableName(String qualifiedTableName);

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
   * 主キーのカラム名を設定します。
   *
   * @param pkColumnName 主キーのカラム名
   */
  void setPkColumnName(String pkColumnName);

  /**
   * 主キーのカラムの値を設定します。
   *
   * @param pkColumnValue 主キーのカラムの値
   */
  void setPkColumnValue(String pkColumnValue);

  /**
   * 生成される識別子を保持するカラム名を設定します。
   *
   * @param valueColumnName 生成される識別子を保持するカラム名
   */
  void setValueColumnName(String valueColumnName);

  /**
   * このジェネレータを初期化します。
   *
   * @throws JdbcException 初期化に失敗した場合
   */
  void initialize();
}
