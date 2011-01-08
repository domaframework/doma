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
 * 列挙型用のドメインクラスを示します。
 * <p>
 * 注釈された列挙型は、 次の制約を満たす必要があります。
 * <ul>
 * <li>トップレベルのクラスである。
 * <li>{@code factoryMethod} 要素に指定した名前の非 {@code private} で {@code static}
 * なメソッドを持つ。このメソッドは、注釈された列挙型と同じ型を戻り値とし、{@code valueType}
 * 要素に指定した型と同じ型をパラメータとして受け取る。
 * <li>{@code accessorMethod} 要素に指定した名前の非 {@code private} なメソッドを持つ。このメソッドは、
 * {@code valueType} 要素に指定した型を戻り値とし、パラメータは受け取らない。
 * </ul>
 * <p>
 * 
 * <h5>例:</h5>
 * 
 * <pre>
 * &#064;EnumDomain(valueType = String.class)
 * public enum JobType {
 * 
 *     SALESMAN(&quot;S&quot;), MANAGER(&quot;M&quot;), ANALYST(&quot;A&quot;), PRESIDENT(&quot;P&quot;), CLERK(&quot;C&quot;);
 * 
 *     private final String value;
 * 
 *     private JobType(String value) {
 *         this.value = value;
 *     }
 * 
 *     static JobType of(String value) {
 *         for (JobType jobType : JobType.values()) {
 *             if (jobType.value.equals(value)) {
 *                 return jobType;
 *             }
 *         }
 *         throw new IllegalArgumentException(value);
 *     }
 * 
 *     String getValue() {
 *         return value;
 *     }
 * }
 * </pre>
 * 
 * @author taedium
 * @since 1.7.0
 * @deprecated 代わりに {@link Domain} を使用してください。1.12.0から {@code Domain}
 *             は列挙型に指定可能になっています。 {@link Domain#factoryMethod()}
 *             には明示的にファクトリメソッドの名前を指定してください。
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface EnumDomain {

    /**
     * ドメインクラスが扱う値型(基本型)。
     */
    Class<?> valueType();

    /**
     * ドメインクラスのファクトリメソッドの名前。
     */
    String factoryMethod() default "of";

    /**
     * ドメインクラスが扱う値に対するアクセッサーメソッドの名前。
     */
    String accessorMethod() default "getValue";

}
