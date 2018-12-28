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
package org.seasar.doma.internal.apt.mirror;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import org.seasar.doma.SqlProcessor;
import org.seasar.doma.internal.apt.util.ElementUtil;

/** @author nakamura */
public class SqlProcessorMirror {

  protected final AnnotationMirror annotationMirror;

  protected SqlProcessorMirror(AnnotationMirror annotationMirror) {
    this.annotationMirror = annotationMirror;
  }

  public static SqlProcessorMirror newInstance(
      ExecutableElement method, ProcessingEnvironment env) {
    assertNotNull(env);
    AnnotationMirror annotationMirror =
        ElementUtil.getAnnotationMirror(method, SqlProcessor.class, env);
    if (annotationMirror == null) {
      return null;
    }
    return new SqlProcessorMirror(annotationMirror);
  }

  public AnnotationMirror getAnnotationMirror() {
    return annotationMirror;
  }
}
