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
package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.doma.jdbc.domain.DomainConverter;

/**
 * 任意のクラスをドメインクラスとして扱うことを示します。
 * <p>
 * 注釈されたクラスは次の制約を満たす必要があります。
 * <ul>
 * <li>{@link DomainConverter} のサブタイプである。
 * <li>トップレベルのクラスである。
 * <li>引数なしの {@code public} なコンストラクタを持つ。
 * <li>具象クラスである。
 * <li>スレッドセーフである。
 * </ul>
 * 
 * <h5>例:</h5>
 * 
 * <pre>
 * &#064;ExternalDomain
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
 * @see DomainConverter
 * @see DomainConverters
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExternalDomain {
}
