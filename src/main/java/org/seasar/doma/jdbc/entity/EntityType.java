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

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.seasar.doma.Entity;

/**
 * エンティティのメタタイプです。
 *
 * <p>このインタフェースの実装はスレッドセーフでなければいけません。
 *
 * @author taedium
 * @param <ENTITY> エンティティの型
 */
public interface EntityType<ENTITY> {

  /**
   * エンティティがイミュータブルかどうかを返します。
   *
   * @return イミュータブルの場合 {@code true}
   * @since 1.34.0
   */
  boolean isImmutable();

  /**
   * エンティティの名前を返します。
   *
   * @return 名前
   */
  String getName();

  /**
   * カタログ名を返します。
   *
   * @return カタログ名
   */
  String getCatalogName();

  /**
   * スキーマ名を返します。
   *
   * @return スキーマ名
   */
  String getSchemaName();

  /**
   * テーブル名を返します。
   *
   * @return テーブル名
   */
  String getTableName();

  /**
   * テーブル名を返します。
   *
   * <p>ネーミング規約が適用されます。
   *
   * @param namingFunction ネーミング規約を適用する関数
   * @return テーブル名
   * @since 2.2.0
   */
  String getTableName(BiFunction<NamingType, String, String> namingFunction);

  /**
   * 完全修飾されたテーブル名を返します。
   *
   * @return 完全修飾されたテーブル名
   */
  String getQualifiedTableName();

  /**
   * 完全修飾されたテーブル名を返します。
   *
   * <p>カタログ、スキーマ、テーブル名は引用符で区切られます。
   *
   * @param quoteFunction 引用符を適用する関数
   * @return 完全修飾されたテーブル名
   */
  String getQualifiedTableName(Function<String, String> quoteFunction);

  /**
   * 完全修飾されたテーブル名を返します。
   *
   * <p>テーブル名には、ネーミング規約が適用されます。
   *
   * <p>カタログ、スキーマ、テーブル名は引用符で区切られます。
   *
   * @param namingFunction ネーミング規約を適用する関数
   * @param quoteFunction 引用符を適用する関数
   * @return 完全修飾されたテーブル名
   * @since 2.2.0
   */
  String getQualifiedTableName(
      BiFunction<NamingType, String, String> namingFunction,
      Function<String, String> quoteFunction);

  /**
   * カタログ、スキーマ、テーブル名において引用符が必要とされるかどうかを返します。
   *
   * @return 引用符が必要とされる場合 {@code true}
   */
  boolean isQuoteRequired();

  /**
   * ネーミング規約を返します。
   *
   * <p>{@link Entity#naming()} に指定された値を返します。指定されていない場合は {@literal null} を返します。
   *
   * @return ネーミング規約
   */
  NamingType getNamingType();

  /**
   * 自動生成される識別子のプロパティ型を返します。
   *
   * @return 自動生成される識別子のプロパティ型
   */
  GeneratedIdPropertyType<? super ENTITY, ENTITY, ?, ?> getGeneratedIdPropertyType();

  /**
   * バージョンのプロパティ型を返します。
   *
   * @return バージョンのプロパティ型
   */
  VersionPropertyType<? super ENTITY, ENTITY, ?, ?> getVersionPropertyType();

  TenantIdPropertyType<? super ENTITY, ENTITY, ?, ?> getTenantIdPropertyType();

  /**
   * 識別子のプロパティ型のリストを返します。
   *
   * @return 識別子のプロパティ型のリスト
   */
  List<EntityPropertyType<ENTITY, ?>> getIdPropertyTypes();

  /**
   * 名前を指定してプロパティ型を返します。
   *
   * @param __name プロパティ名
   * @return プロパティ名、存在しない場合 {@code null}
   */
  EntityPropertyType<ENTITY, ?> getEntityPropertyType(String __name);

  /**
   * プロパティ型のリストを返します。
   *
   * @return プロパティ型のリスト
   */
  List<EntityPropertyType<ENTITY, ?>> getEntityPropertyTypes();

  /**
   * 新しいエンティティをインスタンス化します。
   *
   * @param __args プロパティ
   * @return 新しいエンティティ
   * @since 1.34.0
   */
  ENTITY newEntity(Map<String, Property<ENTITY, ?>> __args);

  /**
   * エンティティのクラスを返します。
   *
   * @return エンティティのクラス
   */
  Class<ENTITY> getEntityClass();

  /**
   * 現在の状態を保存します。
   *
   * @param entity 現在の状態
   */
  void saveCurrentStates(ENTITY entity);

  /**
   * 元の状態を返します。
   *
   * @param entity 元の状態
   * @return 元の状態、存在しない場合 {@code null}
   */
  ENTITY getOriginalStates(ENTITY entity);

  /**
   * 挿入処理の前処理を行います。
   *
   * @param entity エンティティ
   * @param context コンテキスト
   */
  void preInsert(ENTITY entity, PreInsertContext<ENTITY> context);

  /**
   * 更新処理の前処理を行います。
   *
   * @param entity エンティティ
   * @param context コンテキスト
   */
  void preUpdate(ENTITY entity, PreUpdateContext<ENTITY> context);

  /**
   * 削除処理の前処理を行います。
   *
   * @param entity エンティティ
   * @param context コンテキスト
   */
  void preDelete(ENTITY entity, PreDeleteContext<ENTITY> context);

  /**
   * 挿入処理の後処理を行います。
   *
   * @param entity エンティティ
   * @param context コンテキスト
   */
  void postInsert(ENTITY entity, PostInsertContext<ENTITY> context);

  /**
   * 更新処理の後処理を行います。
   *
   * @param entity エンティティ
   * @param context コンテキスト
   */
  void postUpdate(ENTITY entity, PostUpdateContext<ENTITY> context);

  /**
   * 削除処理の後処理を行います。
   *
   * @param entity エンティティ
   * @param context コンテキスト
   */
  void postDelete(ENTITY entity, PostDeleteContext<ENTITY> context);
}
