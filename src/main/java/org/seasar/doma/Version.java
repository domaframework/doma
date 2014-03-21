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
 * 
 * <h3>例:</h3>
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
