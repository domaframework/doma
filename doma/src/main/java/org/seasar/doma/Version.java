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

import org.seasar.doma.domain.NumberDomain;

/**
 * バージョンを示します。
 * <p>
 * このアノテーションが注釈されるメソッドは、{@link Entity} もしくは {@link MappedSuperclass}
 * が注釈されたインタフェースのメンバでなければいけません。
 * <p>
 * 注釈されるメソッドの戻り値の型は、{@link NumberDomain} のサブタイプでなければいけません。
 * 
 * <h5>例:</h5>
 * 
 * <pre>
 * &#064;Entity
 * public interface Employee {
 *     ...
 * 
 *     &#064;Version
 *     &#064;Column(name = &quot;VERSION_NO&quot;)
 *     IntegerDomain versionNo();
 * }
 * </pre>
 * 
 * @author taedium
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Version {
}
