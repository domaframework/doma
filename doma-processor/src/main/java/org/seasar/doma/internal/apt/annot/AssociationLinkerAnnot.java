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

public class AssociationLinkerAnnot extends AbstractAnnot {

  private static final String PROPERTY_PATH = "propertyPath";
  private static final String COLUMN_PREFIX = "columnPrefix";

  private final AnnotationValue propertyPath;
  private final AnnotationValue columnPrefix;

  AssociationLinkerAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    this.propertyPath = assertNonNullValue(values, PROPERTY_PATH);
    this.columnPrefix = assertNonNullValue(values, COLUMN_PREFIX);
  }

  public AnnotationValue getPropertyPath() {
    return propertyPath;
  }

  public AnnotationValue getColumnPrefix() {
    return columnPrefix;
  }

  public String getPropertyPathValue() {
    String result = AnnotationValueUtil.toString(propertyPath);
    if (result == null) {
      throw new AptIllegalStateException(PROPERTY_PATH);
    }
    return result;
  }

  public String getColumnPrefixValue() {
    String result = AnnotationValueUtil.toString(columnPrefix);
    if (result == null) {
      throw new AptIllegalStateException(COLUMN_PREFIX);
    }
    return result;
  }
}
