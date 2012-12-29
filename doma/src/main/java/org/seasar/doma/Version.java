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
 * 楽観的排他制御で使用されるバージョンを示します。
 * <p>
 * このアノテーションが注釈されるフィールドは、エンティティインタフェースのメンバでなければいけません。
 * <p>
 * フィールドの型は、次のいずれかでなければいけません。
 * <ul>
 * <li>byte</li>
 * <li>short</li>
 * <li>int</li>
 * <li>long</li>
 * <li>double</li>
 * <li>float</li>
 * <li>java.lang.Byte</li>
 * <li>java.lang.Short</li>
 * <li>java.lang.Integer</li>
 * <li>java.lang.Long</li>
 * <li>java.lang.Double</li>
 * <li>java.lang.Float</li>
 * <li>java.math.BigInteger</li>
 * <li>java.math.BigDecimal</li>
 * <li>上に示した型にマッピングされたドメインクラス</li>
 * </ul>
 * 
 * 
 * <h5>例:</h5>
 * 
 * <pre>
 * &#064;Entity
 * public class Employee {
 *     ...
 * 
 *     &#064;Version
 *     &#064;Column(name = &quot;VERSION_NO&quot;)
 *     int versionNo;
 * }
 * </pre>
 * 
 * @author taedium
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@EntityField
public @interface Version {
}
