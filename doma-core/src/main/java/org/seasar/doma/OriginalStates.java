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
 * Indicates that the annotated field holds original states that are fetched from database.
 *
 * <p>This annotation allows that only modified properties are reflected to SQL UPDATE statements.
 *
 * <p>The annotated field must be a member of an {@link Entity} annotated class. The field type must
 * be same as the {@link Entity} annotated class.
 *
 * <p>The field must not be modified by application code.
 *
 * <pre>
 * &#064;Entity
 * public class Employee {
 *
 *     String name;
 *
 *     &#064;OriginalStates
 *     Employee originalStates;
 *
 *     public String getName() {
 *         return name;
 *     }
 *
 *     public void setName(String name) {
 *         this.name = name;
 *     }
 *
 *     ...
 * }
 * </pre>
 *
 * @see Update
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@EntityField
public @interface OriginalStates {}
