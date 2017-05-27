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

import java.util.Optional;

/**
 * マッピングに関するヒント情報です。
 * <p>
 * {@link JdbcMappingVisitor} の実装クラスにおいて、次のマッピングのカスタマイズに利用できます。
 * 
 * <ul>
 * <li>JavaのクラスからSQLの型へのマッピング（JavaからSQLのバインド変数へのマッピング）</li>
 * <li>SQLの型からJavaのクラスへのマッピング（SQLの結果セットからJavaのマッピング）</li>
 * </ul>
 * 
 * @author nakamura-to
 * 
 */
public interface JdbcMappingHint {

    /**
     * ドメインクラスにマッピングされている場合、そのドメインクラスを返します。
     * 
     * @return ドメインクラス
     */
    Optional<Class<?>> getHolderClass();
}
