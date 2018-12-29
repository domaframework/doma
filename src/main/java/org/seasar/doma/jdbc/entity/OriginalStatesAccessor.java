package org.seasar.doma.jdbc.entity;

import java.lang.reflect.Field;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.OriginalStates;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.internal.util.FieldUtil;

/**
 * {@link OriginalStates} が注釈されたフィールドへアクセスするクラスです。
 *
 * @param <E> エンティティの型
 * @author taedium
 * @since 1.20.0
 */
public class OriginalStatesAccessor<E> {

  /** エンティティクラス */
  protected final Class<? super E> entityClass;

  /** {@link OriginalStates} が注釈されたフィールドの名前 */
  protected final String name;

  /** {@link OriginalStates} が注釈されたフィールド */
  protected final Field field;

  /**
   * インスタンスを構築します
   *
   * @param entityClass エンティティクラス
   * @param name {@link OriginalStates} が注釈されたフィールドの名前
   */
  public OriginalStatesAccessor(Class<? super E> entityClass, String name) {
    if (entityClass == null) {
      throw new DomaNullPointerException("entityClass");
    }
    if (name == null) {
      throw new DomaNullPointerException("name");
    }
    this.entityClass = entityClass;
    this.name = name;
    this.field = getField();
  }

  private Field getField() {
    Field field;
    try {
      field = ClassUtil.getDeclaredField(entityClass, name);
    } catch (WrapException wrapException) {
      throw new OriginalStatesNotFoundException(
          wrapException.getCause(), entityClass.getName(), name);
    }
    if (!FieldUtil.isPublic(field)) {
      try {
        FieldUtil.setAccessible(field, true);
      } catch (WrapException wrapException) {
        throw new OriginalStatesNotFoundException(
            wrapException.getCause(), entityClass.getName(), name);
      }
    }
    return field;
  }

  /**
   * エンティティからOriginalStatesを取得します。
   *
   * @param entity エンティティ
   * @return OriginalStates
   */
  @SuppressWarnings("unchecked")
  public E get(E entity) {
    if (entity == null) {
      throw new DomaNullPointerException("entity");
    }
    try {
      return (E) FieldUtil.get(field, entity);
    } catch (WrapException wrapException) {
      throw new OriginalStatesAccessException(
          wrapException.getCause(), entityClass.getName(), name);
    }
  }

  /**
   * エンティティにOriginalStatesを設定します。
   *
   * @param entity エンティティ
   * @param states OriginalStates
   */
  public void set(E entity, E states) {
    if (entity == null) {
      throw new DomaNullPointerException("entity");
    }
    try {
      FieldUtil.set(field, entity, states);
    } catch (WrapException wrapException) {
      throw new OriginalStatesAccessException(
          wrapException.getCause(), entityClass.getName(), name);
    }
  }
}
