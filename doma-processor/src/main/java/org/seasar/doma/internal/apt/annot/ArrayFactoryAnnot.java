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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class ArrayFactoryAnnot extends AbstractAnnot {

  private static final String TYPE_NAME = "typeName";

  private final AnnotationValue typeName;

  ArrayFactoryAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    this.typeName = assertNonNullValue(values, TYPE_NAME);
  }

  public String getTypeNameValue() {
    String result = AnnotationValueUtil.toString(typeName);
    if (result == null) {
      throw new AptIllegalStateException(TYPE_NAME);
    }
    return result;
  }
}
