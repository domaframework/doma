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
package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNonNullValue;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class ValueAnnot extends AbstractAnnot {

  private static final String STATIC_CONSTRUCTOR = "staticConstructor";

  private final AnnotationValue staticConstructor;

  ValueAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    this.staticConstructor = assertNonNullValue(values, STATIC_CONSTRUCTOR);
  }

  public AnnotationValue getStaticConstructor() {
    return staticConstructor;
  }

  public String getStaticConstructorValue() {
    String value = AnnotationValueUtil.toString(staticConstructor);
    if (value == null) {
      throw new AptIllegalStateException(STATIC_CONSTRUCTOR);
    }
    return value;
  }
}
