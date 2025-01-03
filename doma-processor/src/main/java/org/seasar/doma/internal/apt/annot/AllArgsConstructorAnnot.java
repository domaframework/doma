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
import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class AllArgsConstructorAnnot extends AbstractAnnot {

  private static final String STATIC_NAME = "staticName";

  private static final String ACCESS = "access";

  private final AnnotationValue staticName;

  private final AnnotationValue access;

  AllArgsConstructorAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    this.staticName = assertNonNullValue(values, STATIC_NAME);
    this.access = assertNonNullValue(values, ACCESS);
  }

  public AnnotationValue getStaticName() {
    return staticName;
  }

  public AnnotationValue getAccess() {
    return access;
  }

  public String getStaticNameValue() {
    String value = AnnotationValueUtil.toString(staticName);
    if (value == null) {
      throw new AptIllegalStateException(STATIC_NAME);
    }
    return value;
  }

  public boolean isAccessPrivate() {
    VariableElement enumConstant = AnnotationValueUtil.toEnumConstant(access);
    if (enumConstant == null) {
      throw new AptIllegalStateException(ACCESS);
    }
    return "PRIVATE".equals(enumConstant.getSimpleName().toString());
  }

  public boolean isAccessNone() {
    VariableElement enumConstant = AnnotationValueUtil.toEnumConstant(access);
    if (enumConstant == null) {
      throw new AptIllegalStateException(ACCESS);
    }
    return "NONE".equals(enumConstant.getSimpleName().toString());
  }
}
