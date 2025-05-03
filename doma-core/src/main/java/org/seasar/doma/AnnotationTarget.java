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

/**
 * Defines the locations where annotations may appear in Java code.
 *
 * <p>This enum is used to specify the valid target locations for annotations within the Doma
 * framework's annotation processing system.
 *
 * @see Annotation
 */
public enum AnnotationTarget {

  /** Class */
  CLASS,

  /** Constructor */
  CONSTRUCTOR,

  /** Constructor's parameter */
  CONSTRUCTOR_PARAMETER,
}
