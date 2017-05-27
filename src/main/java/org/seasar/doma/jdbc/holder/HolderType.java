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
package org.seasar.doma.jdbc.holder;

import java.util.Optional;

import org.seasar.doma.internal.jdbc.scalar.Scalar;

/**
 * ドメイン型のメタタイプです。
 * 
 * <p>
 * このインタフェースの実装はスレッドセーフであることは要求されません。
 * </p>
 * 
 * @author taedium
 * @since 1.8.0
 * @param <BASIC>
 *            ドメイン型が扱う基本型
 * @param <HOLDER>
 *            ドメイン型
 */
public interface HolderType<BASIC, HOLDER> {

    /**
     * 基本型のクラスを返します。
     * <p>
     * プリミティブ型を返す場合があるため、戻り値の型は {@code Class<BASIC>} ではなく {@code Class<?>}
     * となっています。
     * 
     * @return 基本型のクラス
     */
    Class<?> getBasicClass();

    /**
     * ドメイン型のクラスを返します。
     * 
     * @return ドメイン型のクラス
     */
    Class<HOLDER> getHolderClass();

    /**
     * スカラーを作成します。
     * 
     * @return スカラー
     * @since 2.0.0
     */
    Scalar<BASIC, HOLDER> createScalar();

    /**
     * 初期値を持ったスカラーを作成します。
     * 
     * @param value
     *            初期値
     * @return スカラー
     * @since 2.0.0
     */
    Scalar<BASIC, HOLDER> createScalar(HOLDER value);

    /**
     * {@link Optional} なスカラーを作成します。
     * 
     * @return スカラー
     * @since 2.0.0
     */
    Scalar<BASIC, Optional<HOLDER>> createOptionalScalar();

    /**
     * 初期値を持った {@link Optional} なスカラーを作成します。
     * 
     * @param value
     *            初期値
     * @return スカラー
     * @since 2.0.0
     */
    Scalar<BASIC, Optional<HOLDER>> createOptionalScalar(HOLDER value);
}
