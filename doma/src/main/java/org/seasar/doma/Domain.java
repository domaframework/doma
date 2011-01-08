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

/**
 * ドメインクラスを示します。ドメインクラスとは、カラムに対応付け可能な値クラスです。
 * <p>
 * 注釈されたクラスは、 次の制約を満たす必要があります。
 * <ul>
 * <li>トップレベルのクラスまたは列挙型である。
 * <li>{@code valueType} 要素に指定した型を引数とする非 {@code private} なコンストラクタを持ち、
 * {@code factoryMethod} 要素が {@code "new"} である。もしくは、{@code factoryMethod}
 * 要素に指定した名前の{@code static} で非 {@code private} なメソッドを持ち、戻り値は注釈された型であり、 パラメータは
 * {@code valueType} 要素に指定した型である。
 * <li>{@code accessorMethod} 要素に指定した名前の非 {@code private} なメソッドを持つ。このメソッドは、
 * {@code valueType} 要素に指定した型を戻り値とし、パラメータは受け取らない。
 * </ul>
 * <p>
 * 
 * <h5>例1:コンストラクタで生成するケース</h5>
 * 
 * <pre>
 * &#064;Domain(valueType = String.class)
 * public class PhoneNumber {
 * 
 *     private final String value;
 * 
 *     public PhoneNumber(String value) {
 *         this.value = value;
 *     }
 * 
 *     public String getValue() {
 *         return value;
 *     }
 * }
 * </pre>
 * 
 * <h5>例2:ファクトリメソッドで生成するケース</h5>
 * 
 * <pre>
 * &#064;Domain(valueType = String.class, factoryMethod = &quot;of&quot;)
 * public class PhoneNumber {
 * 
 *     private final String value;
 * 
 *     private PhoneNumber(String value) {
 *         this.value = value;
 *     }
 * 
 *     public String getValue() {
 *         return value;
 *     }
 * 
 *     public static PhoneNumber of(String value) {
 *         return new PhoneNumber(value);
 *     }
 * }
 * </pre>
 * 
 * @author taedium
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Domain {

    /**
     * ドメインクラスが扱う値型(基本型)。
     */
    Class<?> valueType();

    /**
     * ドメインクラスのファクトリメソッドの名前。
     * <p>
     * デフォルトの値である {@code "new"} はコンストラクタで生成することを意味します。
     * 
     * @since 1.12.0
     */
    String factoryMethod() default "new";

    /**
     * ドメインクラスが扱う値に対するアクセッサーメソッドの名前。
     */
    String accessorMethod() default "getValue";

}
