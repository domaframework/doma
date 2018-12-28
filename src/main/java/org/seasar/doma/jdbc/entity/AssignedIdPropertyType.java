/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.jdbc.entity;

import java.util.function.Supplier;
import org.seasar.doma.jdbc.domain.DomainType;
import org.seasar.doma.wrapper.Wrapper;

/**
 * アプリケーションにより割り当てられる識別子のプロパティ型です。
 *
 * @author nakamura-to
 * @param <PARENT> 親エンティティの型
 * @param <ENTITY> エンティティの型
 * @param <BASIC> プロパティの基本型
 * @param <DOMAIN> プロパティのドメイン型
 */
public class AssignedIdPropertyType<PARENT, ENTITY extends PARENT, BASIC, DOMAIN>
    extends DefaultPropertyType<PARENT, ENTITY, BASIC, DOMAIN> {

  /**
   * インスタンスを構築します。
   *
   * @param entityClass エンティティのクラス
   * @param entityPropertyClass プロパティのクラス
   * @param basicClass 値のクラス
   * @param wrapperSupplier ラッパーのサプライヤ
   * @param parentEntityPropertyType 親のエンティティのプロパティ型、親のエンティティを持たない場合 {@code null}
   * @param domainType ドメインのメタタイプ、ドメインでない場合 {@code null}
   * @param name プロパティの名前
   * @param columnName カラム名
   * @param namingType ネーミング規約
   * @param quoteRequired カラム名に引用符が必要とされるかどうか
   */
  public AssignedIdPropertyType(
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
  public boolean isId() {
    return true;
  }
}
