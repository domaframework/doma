package org.seasar.doma.jdbc.entity;

import java.lang.reflect.Method;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.Update;
import org.seasar.doma.jdbc.Config;

/**
 * 更新処理の後処理のコンテキストです。
 *
 * @param <E> エンティティの型
 * @author taedium
 * @since 1.11.0
 */
public interface PostUpdateContext<E> {

  /**
   * プロパティが変更されたかどうかを返します。
   *
   * <p>{@link Update#sqlFile()} に {@code false} が指定されたDaoメソッドによる実行でない場合、常に {@code true}を返します。
   *
   * @param propertyName プロパティ名
   * @return プロパティが変更されているかどうか
   * @exception EntityPropertyNotDefinedException プロパティがエンティティに定義されていない場合
   */
  public boolean isPropertyChanged(String propertyName);

  /**
   * エンティティのメタタイプを返します。
   *
   * @return エンティティのメタタイプ
   */
  public EntityType<E> getEntityType();

  /**
   * {@link Update} が注釈されたメソッドを返します。
   *
   * @return メソッド
   * @since 1.27.0
   */
  public Method getMethod();

  /**
   * JDBCに関する設定を返します。
   *
   * @return JDBCに関する設定
   * @since 1.27.0
   */
  public Config getConfig();

  /**
   * 新しいエンティティを返します。
   *
   * @return 新しいエンティティ
   * @since 1.35.0
   */
  public E getNewEntity();

  /**
   * 新しいエンティティを設定します。
   *
   * <p>このメソッドは、 {@link PostUpdateContext#getEntityType()} に対応するエンティティがイミュータブルである場合にのみ利用してください。
   *
   * @param newEntity エンティティ
   * @throws DomaNullPointerException 引数が {@code null} の場合
   * @since 1.34.0
   */
  public void setNewEntity(E newEntity);
}
