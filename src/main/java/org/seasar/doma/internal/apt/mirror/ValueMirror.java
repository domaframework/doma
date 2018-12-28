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

import java.util.Map;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.internal.apt.util.ElementUtil;

/** @author nakamura-to */
public class ValueMirror {

  protected final AnnotationMirror annotationMirror;

  protected AnnotationValue staticConstructor;

  protected ValueMirror(AnnotationMirror annotationMirror) {
    assertNotNull(annotationMirror);
    this.annotationMirror = annotationMirror;
  }

  public static ValueMirror newInstance(TypeElement typeElement, ProcessingEnvironment env) {
    assertNotNull(env);
    AnnotationMirror annotationMirror =
        ElementUtil.getAnnotationMirror(typeElement, Options.getLombokValue(env), env);
    if (annotationMirror == null) {
      return null;
    }
    ValueMirror result = new ValueMirror(annotationMirror);
    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
        env.getElementUtils().getElementValuesWithDefaults(annotationMirror).entrySet()) {
      String name = entry.getKey().getSimpleName().toString();
      AnnotationValue value = entry.getValue();
      if ("staticConstructor".equals(name)) {
        result.staticConstructor = value;
      }
    }
    return result;
  }

  public AnnotationMirror getAnnotationMirror() {
    return annotationMirror;
  }

  public AnnotationValue getStaticConstructor() {
    return staticConstructor;
  }

  public String getStaticConstructorValue() {
    String value = AnnotationValueUtil.toString(staticConstructor);
    if (value == null) {
      throw new AptIllegalStateException("staticConstructor");
    }
    return value;
  }
}
