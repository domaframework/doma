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

import java.util.List;
import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class DomainConvertersAnnot extends AbstractAnnot {

  private static final String VALUE = "value";

  private final AnnotationValue value;

  DomainConvertersAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    this.value = assertNonNullValue(values, VALUE);
  }

  public AnnotationValue getValue() {
    return value;
  }

  public List<TypeMirror> getValueValue() {
    List<TypeMirror> typeList = AnnotationValueUtil.toTypeList(value);
    if (typeList == null) {
      throw new AptIllegalStateException(VALUE);
    }
    return typeList;
  }
}
