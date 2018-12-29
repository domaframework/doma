package org.seasar.doma.jdbc.entity;

import java.lang.reflect.Method;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.Entity;
import org.seasar.doma.internal.Conventions;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.internal.util.MethodUtil;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.message.Message;

/**
 * {@link EntityType} のファクトリクラスです。
 *
 * @author taedium
 * @since 1.8.0
 */
public final class EntityTypeFactory {

  /**
   * {@link EntityType} のインスタンスを生成します。
   *
   * @param <E> エンティティの型
   * @param entityClass エンティティクラス
   * @param classHelper クラスヘルパー
   * @return {@link EntityType} のインスタンス
   * @throws DomaNullPointerException 引数が {@code null} の場合
   * @throws DomaIllegalArgumentException エンティティクラスに {@link Entity} が注釈されていない場合
   * @throws EntityTypeNotFoundException エンティティクラスに対応するメタクラスが見つからない場合
   * @since 1.27.0
   */
  public static <E> EntityType<E> getEntityType(Class<E> entityClass, ClassHelper classHelper) {
    if (entityClass == null) {
      throw new DomaNullPointerException("entityClass");
    }
    if (classHelper == null) {
      throw new DomaNullPointerException("classHelper");
    }
    if (!entityClass.isAnnotationPresent(Entity.class)) {
      throw new DomaIllegalArgumentException(
          "entityClass", Message.DOMA2206.getMessage("entityClass"));
    }
    String entityTypeClassName = Conventions.toFullMetaName(entityClass.getName());
    try {
      Class<E> clazz = classHelper.forName(entityTypeClassName);
      Method method = ClassUtil.getMethod(clazz, "getSingletonInternal");
      return MethodUtil.invoke(method, null);
    } catch (WrapException e) {
      throw new EntityTypeNotFoundException(
          e.getCause(), entityClass.getName(), entityTypeClassName);
    } catch (Exception e) {
      throw new EntityTypeNotFoundException(
          e.getCause(), entityClass.getName(), entityTypeClassName);
    }
  }
}
