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

import java.util.List;
import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class ReturningAnnot extends AbstractAnnot {

  private static final String INCLUDE = "include";

  private static final String EXCLUDE = "exclude";

  private final AnnotationValue include;

  private final AnnotationValue exclude;

  ReturningAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    this.include = assertNonNullValue(values, INCLUDE);
    this.exclude = assertNonNullValue(values, EXCLUDE);
  }

  public AnnotationValue getInclude() {
    return include;
  }

  public AnnotationValue getExclude() {
    return exclude;
  }

  public List<String> getIncludeValue() {
    return AnnotationValueUtil.toStringList(include);
  }

  public List<String> getExcludeValue() {
    return AnnotationValueUtil.toStringList(exclude);
  }
}
