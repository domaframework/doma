/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates an entity identifier that is mapped to a primary key of a database table.
 *
 * <p>The annotated field must be a member of an {@link Entity} annotated class.
 *
 * <p>This annotation marks a field as representing a primary key column in the database table.
 * It can be used in conjunction with {@link GeneratedValue} to specify how the identifier
 * should be generated.
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
 * @see SequenceGenerator
 * @see TableGenerator
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@EntityField
public @interface Id {}
