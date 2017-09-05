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
 * Indicates an entity identifier that is mapped to a primary key of a database
 * table.
 * <p>
 * The annotated field must be a member of an {@link Entity} annotated class.
 * <p>
 * 
 * <pre>
 * &#064;Entity
 * public class Employee {
 * 
 *     &#064;Id
 *     &#064;Column(name = &quot;ID&quot;)
 *     String id;
 *     ...
 * }
 * </pre>
 * 
 * @see GeneratedValue
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@EntityField
public @interface Id {
}
