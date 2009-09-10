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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

/**
 * 永続化非対象を示します。
 * <p>
 * 注釈されたメソッドは、テーブルのカラムに対応付けされません。一時的な値や、SQLのIN句のパラメータにバインドするための {@link List}
 * を保持するのに適しています。
 * <p>
 * このアノテーションが注釈されるメソッドは、{@link Entity}もしくは {@link MappedSuperclass}
 * が注釈されたインタフェースのメンバでなければいけません。
 * 
 * <h5>例:</h5>
 * 
 * <pre>
 * &#064;Entity
 * public interface Employee {
 *     ...
 *     &#064;Transient
 *     BuiltinIntegerDomain tempNumber();
 *     
 *     &#064;Transient
 *     List&lt;BuiltinStringDomain&gt; names();
 * }
 * </pre>
 * 
 * @author taedium
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EntityMethod
public @interface Transient {
}
