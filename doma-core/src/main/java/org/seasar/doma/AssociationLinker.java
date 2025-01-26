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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.BiFunction;

/**
 * Associates a field in an entity class with properties in a related object for the purpose of
 * creating object relationships when mapping between database tables and entities.
 *
 * <p>This class can only be annotated on {@code public static final} fields of type {@link
 * BiFunction}.
 */
@Target(java.lang.annotation.ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AssociationLinker {
  /**
   * Specifies the path to the property in the related object.
   *
   * @return the property path as a string
   */
  String propertyPath();

  /**
   * Defines the prefix for the column in the database table that is linked to the field.
   *
   * @return the column prefix as a string
   */
  String columnPrefix();
}
