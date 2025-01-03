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

/**
 * Used in conjunction with the {@link AnnotateWith} annotation to indicate which kind of annotation
 * is specified for generated code.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Annotation {

  /**
   * @return the location where the annotation is specified.
   */
  AnnotationTarget target();

  /**
   * @return the annotation class that this annotation represents.
   */
  Class<? extends java.lang.annotation.Annotation> type();

  /**
   * The annotation elements as a set of key-value pair.
   *
   * <p>Represented in CSV format:
   *
   * <pre>
   * elementName1=value1, elementName2=value2
   * </pre>
   *
   * @return the annotation elements
   */
  String elements() default "";
}
