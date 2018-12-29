package org.seasar.doma.jdbc.entity;

import org.seasar.doma.BatchDelete;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Update;

/**
 * エンティティのリスナーです。
 *
 * <p>このインタフェースの実装は、引数なしの {@code public} なコンストラクタを持たなければいけません。
 *
 * <p>このインタフェースの実装はスレッドセーフでなければいけません。
 *
 * @author taedium
 * @param <ENTITY> エンティティの型
 */
public interface EntityListener<ENTITY> {

  /**
   * 挿入処理の前処理を行います。
   *
   * <p>対象となるのは、{@link Insert} 、または {@link BatchInsert} のパラメータにエンティティを受け取るDaoメソッドの実行です。
   *
   * @param entity エンティティ
   * @param context コンテキスト
   * @since 1.11.0
   */
  default void preInsert(ENTITY entity, PreInsertContext<ENTITY> context) {}

  /**
   * 更新処理の前処理を行います。
   *
   * <p>対象となるのは、{@link Update} 、または {@link BatchUpdate} のパラメータにエンティティを受け取るDaoメソッドの実行です。
   *
   * @param entity エンティティ
   * @param context コンテキスト
   * @since 1.11.0
   */
  default void preUpdate(ENTITY entity, PreUpdateContext<ENTITY> context) {}

  /**
   * 削除処理の前処理を行います。
   *
   * <p>対象となるのは、{@link Delete} 、または {@link BatchDelete} のパラメータにエンティティを受け取るDaoメソッドの実行です。
   *
   * @param entity エンティティ
   * @param context コンテキスト
   * @since 1.11.0
   */
  default void preDelete(ENTITY entity, PreDeleteContext<ENTITY> context) {}

  /**
   * 挿入処理の後処理を行います。
   *
   * <p>対象となるのは、{@link Insert} 、または {@link BatchInsert} のパラメータにエンティティを受け取るDaoメソッドの実行です。
   *
   * @param entity エンティティ
   * @param context コンテキスト
   * @since 1.11.0
   */
  default void postInsert(ENTITY entity, PostInsertContext<ENTITY> context) {}

  /**
   * 更新処理の後処理を行います。
   *
   * <p>対象となるのは、{@link Update} 、または {@link BatchUpdate} のパラメータにエンティティを受け取るDaoメソッドの実行です。
   *
   * @param entity エンティティ
   * @param context コンテキスト
   * @since 1.11.0
   */
  default void postUpdate(ENTITY entity, PostUpdateContext<ENTITY> context) {}

  /**
   * 削除処理の後処理を行います。
   *
   * <p>対象となるのは、{@link Delete} 、または {@link BatchDelete} のパラメータにエンティティを受け取るDaoメソッドの実行です。
   *
   * @param entity エンティティ
   * @param context コンテキスト
   * @since 1.11.0
   */
  default void postDelete(ENTITY entity, PostDeleteContext<ENTITY> context) {}
}
