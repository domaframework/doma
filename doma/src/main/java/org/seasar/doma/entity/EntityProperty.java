/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.doma.entity;

import org.seasar.doma.domain.Domain;
import org.seasar.doma.jdbc.Config;

/**
 * エンティティのプロパティを表します。
 * 
 * <p>
 * このインタフェースの実装はスレッドセーフであることは要求されません。
 * </p>
 * 
 * @author taedium
 * 
 * @param <D>
 *            ドメインの型
 */
public interface EntityProperty<D extends Domain<?, ?>> {

    /**
     * ドメインを返します。
     * 
     * @return ドメイン
     */
    D getDomain();

    /**
     * 名前を返します。
     * 
     * @return 名前
     */
    String getName();

    /**
     * カラム名を返します。
     * 
     * @param config
     *            JDBCの設定
     * @return カラム名
     */
    String getColumnName(Config config);

    /**
     * 識別子かどうかを返します。
     * 
     * @return 識別子の場合 {@code true}
     */
    boolean isId();

    /**
     * バージョンかどうかを返します。
     * 
     * @return バージョンの場合 {@code true}
     */
    boolean isVersion();

    /**
     * INSERT文に含める対象かどうかを返します。
     * 
     * @return INSERT文に含める対象の場合 {@code true}
     */
    boolean isInsertable();

    /**
     * UPDATE文のSET句に含める対象かどうかを返します。
     * 
     * @return UPDATE文のSET句に含める対象の場合 {@code true}
     */
    boolean isUpdatable();

    /**
     * 非永続性かどうかを返します。
     * 
     * @return 非永続性の場合 {@code true}
     */
    boolean isTransient();

}
