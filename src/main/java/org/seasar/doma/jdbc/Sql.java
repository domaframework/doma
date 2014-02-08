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
package org.seasar.doma.jdbc;

import java.util.List;

/**
 * SQLを表します。
 * <p>
 * SQLとSQL実行時のパラメータをカプセル化します。また、SQLのバインド変数をパラメータで置換した文字列やSQLファイルのパスを保持します。
 * 
 * <p>
 * このインタフェースの実装はスレッドセーフであることを要求されません。
 * 
 * @author taedium
 * @param <P>
 *            パラメータの種別を表す型
 */
public interface Sql<P extends SqlParameter> {

    /**
     * SQLの種別を返します。
     * 
     * @return SQLの種別
     */
    SqlKind getKind();

    /**
     * 未加工SQLを返します。
     * <p>
     * バインド変数は {@code ?} で表されます。
     * 
     * @return 未加工SQL
     */
    String getRawSql();

    /**
     * フォーマット済みSQLを返します。
     * <p>
     * バインド変数 {@code ?} が、 {@link SqlLogFormattingVisitor}
     * の実装によって適切な文字列に置換されたSQLです。
     * 
     * @return フォーマット済みSQL
     */
    String getFormattedSql();

    /**
     * 未加工SQLが記述されているSQLファイルのパスを返します。
     * 
     * @return SQLファイルのパス、SQLが自動生成された場合は {@code null}
     */
    String getSqlFilePath();

    /**
     * バインド変数へのパラメータのリストを返します。
     * 
     * @return バインド変数のパラメータのリスト
     */
    List<P> getParameters();

}
