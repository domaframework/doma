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
import java.util.Objects;
import java.util.function.Function;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class ColumnOverrideAnnot extends AbstractAnnot {

  public static final String NAME = "name";

  public static final String COLUMN = "column";

  private final AnnotationValue name;

  private final AnnotationValue column;

  private final Function<AnnotationMirror, Map<String, AnnotationValue>> getValuesWithoutDefaults;

  ColumnOverrideAnnot(
      AnnotationMirror annotationMirror,
      Map<String, AnnotationValue> values,
      Function<AnnotationMirror, Map<String, AnnotationValue>> getValuesWithoutDefaults) {
    super(annotationMirror);
    this.name = assertNonNullValue(values, NAME);
    this.column = assertNonNullValue(values, COLUMN);
    this.getValuesWithoutDefaults = Objects.requireNonNull(getValuesWithoutDefaults);
  }

  public AnnotationValue getName() {
    return name;
  }

  public AnnotationValue getColumn() {
    return column;
  }

  public String getNameValue() {
    String value = AnnotationValueUtil.toString(name);
    if (value == null) {
      throw new AptIllegalStateException(NAME);
    }
    return value;
  }

  public ColumnAnnot getColumnValue() {
    AnnotationMirror value = AnnotationValueUtil.toAnnotation(column);
    if (value == null) {
      throw new AptIllegalStateException(COLUMN);
    }
    Map<String, AnnotationValue> valuesWithoutDefaults = getValuesWithoutDefaults.apply(value);
    return new ColumnAnnot(value, valuesWithoutDefaults);
  }

  public static class ColumnAnnot extends AbstractAnnot {

    public static final String INSERTABLE = "insertable";

    public static final String UPDATABLE = "updatable";

    public static final String QUOTE = "quote";

    public static final String NAME = "name";

    private final AnnotationValue name;

    private final AnnotationValue insertable;

    private final AnnotationValue updatable;

    private final AnnotationValue quote;

    ColumnAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
      super(annotationMirror);
      this.name = values.get(NAME);
      this.insertable = values.get(INSERTABLE);
      this.updatable = values.get(UPDATABLE);
      this.quote = values.get(QUOTE);
    }

    public AnnotationValue getName() {
      return name;
    }

    public AnnotationValue getInsertable() {
      return insertable;
    }

    public AnnotationValue getUpdatable() {
      return updatable;
    }

    public AnnotationValue getQuote() {
      return quote;
    }

    /**
     * @return nullable
     */
    public String getNameValue() {
      return AnnotationValueUtil.toString(name);
    }

    /**
     * @return nullable
     */
    public Boolean getInsertableValue() {
      return AnnotationValueUtil.toBoolean(insertable);
    }

    /**
     * @return nullable
     */
    public Boolean getUpdatableValue() {
      return AnnotationValueUtil.toBoolean(updatable);
    }

    /**
     * @return nullable
     */
    public Boolean getQuoteValue() {
      return AnnotationValueUtil.toBoolean(quote);
    }
  }
}
