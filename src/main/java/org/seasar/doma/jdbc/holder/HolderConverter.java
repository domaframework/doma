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

import org.seasar.doma.ExternalHolder;
import org.seasar.doma.HolderConverters;

/**
 * 任意の型の値を基本型の値と相互に変換します。つまり 、任意の型をドメインクラスとして扱うことを可能にします。
 * <p>
 * 通常、このインタフェースの実装クラスには {@link ExternalHolder} を注釈します。また、 実装クラスは
 * {@link HolderConverters} に登録して使用します。
 * <p>
 * 1番目の型パラメータは、 次の制約を満たす必要があります。
 * <ul>
 * <li>トップレベルのクラスである。
 * <li>パッケージに属する。
 * </ul>
 * 
 * <h3>例:</h3>
 * 
 * <pre>
 * &#064;ExtenalHolder
 * public class SalaryConverter implements HolderConverter&lt;Salary, BigDecimal&gt; {
 * 
 *     public BigDecimal fromHolderToValue(Salary holder) {
 *         return holder.getValue();
 *     }
 * 
 *     public Salary fromValueToHolder(BigDecimal value) {
 *         return new Salary(value);
 *     }
 * }
 * </pre>
 * 
 * @author taedium
 * @since 1.25.0
 * @see ExternalHolder
 * @see HolderConverters
 * 
 * @param <HOLDER>
 *            ドメイン型
 * @param <BASIC>
 *            基本型
 */
public interface HolderConverter<HOLDER, BASIC> {

    /**
     * ドメインから値へ変換します。
     * 
     * @param holder
     *            ドメイン
     * @return 値
     */
    BASIC fromHolderToValue(HOLDER holder);

    /**
     * 値からドメインへ変換します。
     * 
     * @param value
     *            値
     * @return ドメイン
     */
    HOLDER fromValueToHolder(BASIC value);
}
