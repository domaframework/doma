package org.seasar.doma.jdbc.entity;

import java.util.function.Supplier;
import org.seasar.doma.jdbc.domain.DomainType;
import org.seasar.doma.wrapper.NumberWrapper;
import org.seasar.doma.wrapper.NumberWrapperVisitor;
import org.seasar.doma.wrapper.Wrapper;

/**
 * バージョンのプロパティ型です。
 *
 * @author nakamura-to
 * @param <PARENT> 親エンティティの型
 * @param <ENTITY> エンティティの型
 * @param <BASIC> プロパティの基本型
 * @param <DOMAIN> プロパティのドメイン型
 */
public class VersionPropertyType<PARENT, ENTITY extends PARENT, BASIC extends Number, DOMAIN>
    extends DefaultPropertyType<PARENT, ENTITY, BASIC, DOMAIN> {

  /**
   * インスタンスを構築します。
   *
   * @param entityClass エンティティのクラス
   * @param entityPropertyClass プロパティのクラス
   * @param basicClass 基本型のクラス
   * @param wrapperSupplier ラッパーのサプライヤ
   * @param parentEntityPropertyType 親のエンティティのプロパティ型、親のエンティティを持たない場合 {@code null}
   * @param domainType ドメインのメタタイプ、ドメインでない場合 {@code null}
   * @param name プロパティの名前
   * @param columnName カラム名
   * @param namingType ネーミング規約
   * @param quoteRequired カラム名に引用符が必要とされるかどうか
   */
  public VersionPropertyType(
      Class<ENTITY> entityClass,
      Class<?> entityPropertyClass,
      Class<BASIC> basicClass,
      Supplier<Wrapper<BASIC>> wrapperSupplier,
      EntityPropertyType<PARENT, BASIC> parentEntityPropertyType,
      DomainType<BASIC, DOMAIN> domainType,
      String name,
      String columnName,
      NamingType namingType,
      boolean quoteRequired) {
    super(
        entityClass,
        entityPropertyClass,
        basicClass,
        wrapperSupplier,
        parentEntityPropertyType,
        domainType,
        name,
        columnName,
        namingType,
        true,
        true,
        quoteRequired);
  }

  @Override
  public boolean isVersion() {
    return true;
  }

  /**
   * 必要であればバージョンの値を設定します。
   *
   * @param entityType エンティティのタイプ
   * @param entity エンティティ
   * @param value バージョンの値
   * @return エンティティ
   */
  public ENTITY setIfNecessary(EntityType<ENTITY> entityType, ENTITY entity, Number value) {
    return modifyIfNecessary(entityType, entity, new ValueSetter(), value);
  }

  /**
   * バージョン番号をインクリメントします。
   *
   * @param entityType エンティティのタイプ
   * @param entity エンティティ
   * @return エンティティ
   */
  public ENTITY increment(EntityType<ENTITY> entityType, ENTITY entity) {
    return modifyIfNecessary(entityType, entity, new Incrementer(), null);
  }

  protected static class ValueSetter
      implements NumberWrapperVisitor<Boolean, Number, Void, RuntimeException> {

    @Override
    public <V extends Number> Boolean visitNumberWrapper(
        NumberWrapper<V> wrapper, Number value, Void q) {
      Number currentValue = wrapper.get();
      if (currentValue == null || currentValue.intValue() < 0) {
        wrapper.set(value);
        return true;
      }
      return false;
    }
  }

  protected static class Incrementer
      implements NumberWrapperVisitor<Boolean, Void, Void, RuntimeException> {

    @Override
    public <V extends Number> Boolean visitNumberWrapper(NumberWrapper<V> wrapper, Void p, Void q) {
      wrapper.increment();
      return true;
    }
  }
}
