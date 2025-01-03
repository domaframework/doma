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
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class SequenceGeneratorAnnot extends AbstractAnnot {

  private static final String CATALOG = "catalog";

  private static final String SCHEMA = "schema";

  private static final String SEQUENCE = "sequence";

  private static final String INITIAL_VALUE = "initialValue";

  private static final String ALLOCATION_SIZE = "allocationSize";

  private static final String IMPLEMENTER = "implementer";

  private final AnnotationValue catalog;

  private final AnnotationValue schema;

  private final AnnotationValue sequence;

  private final AnnotationValue initialValue;

  private final AnnotationValue allocationSize;

  private final AnnotationValue implementer;

  SequenceGeneratorAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    this.catalog = assertNonNullValue(values, CATALOG);
    this.schema = assertNonNullValue(values, SCHEMA);
    this.sequence = assertNonNullValue(values, SEQUENCE);
    this.initialValue = assertNonNullValue(values, INITIAL_VALUE);
    this.allocationSize = assertNonNullValue(values, ALLOCATION_SIZE);
    this.implementer = assertNonNullValue(values, IMPLEMENTER);
  }

  public AnnotationValue getCatalog() {
    return catalog;
  }

  public AnnotationValue getSchema() {
    return schema;
  }

  public AnnotationValue getSequence() {
    return sequence;
  }

  public AnnotationValue getInitialValue() {
    return initialValue;
  }

  public AnnotationValue getAllocationSize() {
    return allocationSize;
  }

  public AnnotationValue getImplementer() {
    return implementer;
  }

  public String getCatalogValue() {
    String value = AnnotationValueUtil.toString(catalog);
    if (value == null) {
      throw new AptIllegalStateException(CATALOG);
    }
    return value;
  }

  public String getSchemaValue() {
    String value = AnnotationValueUtil.toString(schema);
    if (value == null) {
      throw new AptIllegalStateException(SCHEMA);
    }
    return value;
  }

  public String getSequenceValue() {
    String value = AnnotationValueUtil.toString(sequence);
    if (value == null) {
      throw new AptIllegalStateException(SEQUENCE);
    }
    return value;
  }

  public Long getInitialValueValue() {
    Long value = AnnotationValueUtil.toLong(initialValue);
    if (value == null) {
      throw new AptIllegalStateException(INITIAL_VALUE);
    }
    return value;
  }

  public Long getAllocationSizeValue() {
    Long value = AnnotationValueUtil.toLong(allocationSize);
    if (value == null) {
      throw new AptIllegalStateException(ALLOCATION_SIZE);
    }
    return value;
  }

  public TypeMirror getImplementerValue() {
    TypeMirror value = AnnotationValueUtil.toType(implementer);
    if (value == null) {
      throw new AptIllegalStateException(IMPLEMENTER);
    }
    return value;
  }
}
