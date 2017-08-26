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

import org.seasar.doma.jdbc.holder.HolderConverter;

/**
 * {@link HolderConverter} を複数登録します。
 * <p>
 * このアノテーションの{@code value} 要素に指定される {@code HolderConverter} のクラスには
 * {@link ExternalHolder} が注釈されていないければいけません。
 * 
 * このアノテーションが注釈されたクラスの完全修飾名は、注釈処理のオプションに登録する必要があります。オプションのキーは
 * {@code doma.holder.converters} です。
 * 
 * <h3>例:</h3>
 * 
 * <pre>
 * &#064;HolderConverters({ SalaryConverter.class, DayConverter.class, LocationConverter.class })
 * public class HolderConvertersProvider {
 * }
 * </pre>
 * 
 * @author taedium
 * @since 1.25.0
 * @see HolderConverter
 * @see ExternalHolder
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HolderConverters {

    /**
     * {@code HolderConverter} のクラスの配列を返します。
     * 
     * @return {@code HolderConverter} のクラスの配列
     */
    Class<? extends HolderConverter<?, ?>>[] value() default {};
}
