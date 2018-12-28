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
 * {@link DomainConverter} を複数登録します。
 *
 * <p>このアノテーションの{@code value} 要素に指定される {@code DomainConverter} のクラスには {@link ExternalDomain}
 * が注釈されていないければいけません。
 *
 * <p>このアノテーションが注釈されたクラスの完全修飾名は、注釈処理のオプションに登録する必要があります。オプションのキーは {@code doma.domain.converters} です。
 *
 * <h3>例:</h3>
 *
 * <pre>
 * &#064;DomainConverters({ SalaryConverter.class, DayConverter.class,
 *         LocationConverter.class })
 * public class DomainConvertersProvider {
 * }
 * </pre>
 *
 * @author taedium
 * @since 1.25.0
 * @see DomainConverter
 * @see ExternalDomain
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainConverters {

  /**
   * {@code DomainConverter} のクラスの配列を返します。
   *
   * @return {@code DomainConverter} のクラスの配列
   */
  Class<? extends DomainConverter<?, ?>>[] value() default {};
}
