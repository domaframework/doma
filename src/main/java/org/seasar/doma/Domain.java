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
 * 
 * <h3>例1:コンストラクタで生成するケース</h3>
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
 * <h3>例2:ファクトリメソッドで生成するケース</h3>
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
     * ドメインクラスが扱う値型(基本型)を返します。
     * 
     * @return ドメインクラスが扱う値型(基本型)
     */
    Class<?> valueType();

    /**
     * ドメインクラスのファクトリメソッドの名前を返します。
     * <p>
     * デフォルトの値である {@code "new"} はコンストラクタで生成することを意味します。
     * 
     * @return ドメインクラスのファクトリメソッドの名前
     * @since 1.12.0
     */
    String factoryMethod() default "new";

    /**
     * ドメインクラスが扱う値に対するアクセッサーメソッドの名前を返します。
     * 
     * @return ドメインクラスが扱う値に対するアクセッサーメソッドの名前
     */
    String accessorMethod() default "getValue";

    /**
     * ファクトリメソッドで {@code null} を受け入れるかどうかを返します。
     * <p>
     * {@code null} をドメインクラスで扱いたい場合 {@code true} を設定します。
     * 
     * @return ファクトリメソッドで {@code null} を受け入れるかどうか
     * @since 2.0.0
     */
    boolean acceptNull() default false;

}
