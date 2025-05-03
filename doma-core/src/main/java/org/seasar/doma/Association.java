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
 * Indicates an association relationship between entities.
 *
 * <pre>
 * &#064;Entity
 * class Department {
 *     &#064;Association
 *     List&lt;Employee&gt; employeeList;
 * }
 *
 * &#064;Entity
 * class Employee {
 *     &#064;Association
 *     Department department;
 * }
 * </pre>
 *
 * <p>This annotation is applied to fields that represent a relationship between entities, enabling
 * the framework to automatically handle entity relationships during database operations.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@EntityField
public @interface Association {}
