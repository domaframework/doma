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
import java.util.Objects;
import java.util.function.Function;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class EmbeddedAnnot extends AbstractAnnot {

  public static final String PREFIX = "prefix";

  public static final String COLUMN_OVERRIDES = "columnOverrides";

  private final AnnotationValue prefix;

  private final AnnotationValue columnOverrides;

  private final Function<AnnotationMirror, ColumnOverrideAnnot> columnOverrideAnnotFactory;

  EmbeddedAnnot(
      AnnotationMirror annotationMirror,
      Map<String, AnnotationValue> values,
      Function<AnnotationMirror, ColumnOverrideAnnot> columnOverrideAnnotFactory) {
    super(annotationMirror);
    this.prefix = assertNonNullValue(values, PREFIX);
    this.columnOverrides = assertNonNullValue(values, COLUMN_OVERRIDES);
    this.columnOverrideAnnotFactory = Objects.requireNonNull(columnOverrideAnnotFactory);
  }

  public AnnotationValue getPrefix() {
    return prefix;
  }

  public String getPrefixValue() {
    String value = AnnotationValueUtil.toString(prefix);
    if (value == null) {
      throw new AptIllegalStateException(PREFIX);
    }
    return value;
  }

  public AnnotationValue getColumnOverrides() {
    return columnOverrides;
  }

  public List<ColumnOverrideAnnot> getColumnOverridesValue() {
    List<AnnotationMirror> value = AnnotationValueUtil.toAnnotationList(columnOverrides);
    if (value == null) {
      throw new AptIllegalStateException(COLUMN_OVERRIDES);
    }
    return value.stream().map(columnOverrideAnnotFactory).toList();
  }
}
