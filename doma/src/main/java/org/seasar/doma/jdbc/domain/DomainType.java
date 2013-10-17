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
package org.seasar.doma.jdbc.domain;

import java.util.Optional;

import org.seasar.doma.internal.wrapper.Scalar;

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
 * @param <DOMAIN>
 *            ドメイン型
 */
public interface DomainType<BASIC, DOMAIN> {

    /**
     * 基本型のクラスを返します。
     * 
     * @return 基本型のクラス
     */
    Class<BASIC> getBasicClass();

    /**
     * ドメイン型のクラスを返します。
     * 
     * @return ドメイン型のクラス
     */
    Class<DOMAIN> getDomainClass();

    /**
     * ドメインのホルダーを返します。
     * 
     * @return 値のホルダー
     * @since 2.0.0
     */
    Scalar<BASIC, DOMAIN> createScalar();

    /**
     * {@link Optional} なドメインのホルダーを返します。
     * 
     * @return 値のホルダー
     * @since 2.0.0
     */
    Scalar<BASIC, Optional<DOMAIN>> createOptionalScalar();

}
