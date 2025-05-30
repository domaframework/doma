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
package org.seasar.doma.internal.apt.processor.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.seasar.doma.AnnotateWith;
import org.seasar.doma.Annotation;
import org.seasar.doma.AnnotationTarget;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@AnnotateWith(
    annotations = {
      @Annotation(
          target = AnnotationTarget.CONSTRUCTOR,
          type = ConstructorAnnotation.class,
          elements = "aaa = 1, bbb = true"),
      @Annotation(
          target = AnnotationTarget.CONSTRUCTOR,
          type = ConstructorAnnotation2.class,
          elements = "aaa = 1, bbb = true"),
    })
public @interface MultipleAnnotationConfig2 {}
