package org.seasar.doma.jdbc.entity;

import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.JdbcMappable;

/**
 * プロパティです。
 *
 * @author nakamura-to
 * @param <ENTITY> エンティティの型
 * @param <BASIC> 基本型
 * @since 2.0.0
 */
public interface Property<ENTITY, BASIC> extends JdbcMappable<BASIC> {

  /**
   * プロパティの値を返します。
   *
   * @return プロパティの値
   */
  Object get();

  /**
   * エンティティからこのインスタンスへ値を読み込みます。
   *
   * @param entity エンティティ
   * @return このインスタンス
   */
  Property<ENTITY, BASIC> load(ENTITY entity);

  /**
   * エンティティへこのインスタンスが保持する値を保存します。
   *
   * @param entity エンティティ
   * @return このインスタンス
   */
  Property<ENTITY, BASIC> save(ENTITY entity);

  /**
   * このプロパティを入力用パラメータとして返します。
   *
   * @return 入力用パラメータ
   * @since 2.4.0
   */
  InParameter<BASIC> asInParameter();
}
