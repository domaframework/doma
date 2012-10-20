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

import org.seasar.doma.DomainConverters;
import org.seasar.doma.ExternalDomain;

/**
 * 任意の型の値を <a href="http://doma.seasar.org/reference/basic.html">基本型</a>
 * の値と相互に変換します。つまり 、任意の型をドメインクラスとして扱うことを可能にします。
 * <p>
 * 通常、このインタフェースの実装クラスには {@link ExternalDomain} を注釈します。また、 実装クラスは
 * {@link DomainConverters} に登録して使用します。
 * <p>
 * 1番目の型パラメータは、 次の制約を満たす必要があります。
 * <ul>
 * <li>トップレベルのクラスである。
 * <li>パッケージに属する。
 * </ul>
 * 
 * <h5>例:</h5>
 * 
 * <pre>
 * &#064;ExtenalDomain
 * public class SalaryConverter implements DomainConverter&lt;Salary, BigDecimal&gt; {
 * 
 *     public BigDecimal fromDomainToValue(Salary domain) {
 *         return domain.getValue();
 *     }
 * 
 *     public Salary fromValueToDomain(BigDecimal value) {
 *         return new Salary(value);
 *     }
 * }
 * </pre>
 * 
 * @author taedium
 * @since 1.25.0
 * @see ExternalDomain
 * @see DomainConverters
 * 
 * @param <D>
 *            ドメインの型（任意の型）
 * @param <V>
 *            ドメインが扱う値の型（基本型）
 */
public interface DomainConverter<D, V> {

    /**
     * ドメインから値へ変換します。
     * 
     * @param domain
     *            ドメイン
     * @return 値
     */
    V fromDomainToValue(D domain);

    /**
     * 値からドメインへ変換します。
     * 
     * @param value
     *            値
     * @return ドメイン
     */
    D fromValueToDomain(V value);
}
