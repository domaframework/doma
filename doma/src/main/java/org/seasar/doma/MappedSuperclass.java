/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.doma.domain.Domain;

/**
 * テーブル、結果セット、もしくはパラメータの集合の抽象を示します。
 * <p>
 * このアノテーションは、トップレベルのインタフェースに指定できます。注釈されたインタフェースは {@link MappedSuperclass}
 * が注釈されたインタフェースのみを拡張できます。
 * <p>
 * インタフェースのメンバメソッドは、 {@link Delegate} で注釈されていない限り、次の制約を満たす必要があります。
 * <ul>
 * <li>パラメータは受け取らない。
 * <li>戻り値の型は {@link Domain}の実装クラスである。
 * </ul>
 * 
 * <pre>
 * &#064;MappedSuperclass
 * public interface AbstractEmployee {
 * 
 *     &#064;Id
 *     &#064;Column(name = &quot;ID&quot;)
 *     BuiltinIntegerDomain id();
 * 
 *     &#064;Column(name = &quot;EMPLOYEE_NAME&quot;)
 *     BuiltinStringDomain employeeName();
 * 
 *     &#064;Version
 *     &#064;Column(name = &quot;VERSION&quot;)
 *     BuiltinIntegerDomain version();
 * }
 * </pre>
 * 
 * <p>
 * {@link Delegate} が注釈されていないメソッドの 戻り値の型がすべて {@link Serializable}
 * のサブタイプであれば、注釈されたインタフェースの実装は直列化可能です。
 * <p>
 * 注釈されたインタフェースの実装はスレッドセーフであることを要求されません。
 * <p>
 * 
 * @author taedium
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MappedSuperclass {
}
