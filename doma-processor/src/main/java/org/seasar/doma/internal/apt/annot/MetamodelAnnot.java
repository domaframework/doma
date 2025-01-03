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
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class MetamodelAnnot extends AbstractAnnot {

  private static final String PREFIX = "prefix";

  private static final String SUFFIX = "suffix";

  private static final String SCOPES = "scopes";

  private final AnnotationValue prefix;

  private final AnnotationValue suffix;

  private final AnnotationValue scopes;

  MetamodelAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    this.prefix = assertNonNullValue(values, PREFIX);
    this.suffix = assertNonNullValue(values, SUFFIX);
    this.scopes = assertNonNullValue(values, SCOPES);
  }

  public AnnotationValue getPrefix() {
    return prefix;
  }

  public AnnotationValue getSuffix() {
    return suffix;
  }

  public AnnotationValue getScopes() {
    return scopes;
  }

  public String getPrefixValue() {
    String value = AnnotationValueUtil.toString(prefix);
    if (value == null) {
      throw new AptIllegalStateException(PREFIX);
    }
    return value;
  }

  public String getSuffixValue() {
    String value = AnnotationValueUtil.toString(suffix);
    if (value == null) {
      throw new AptIllegalStateException(SUFFIX);
    }
    return value;
  }

  public List<TypeMirror> getScopesValue() {
    List<TypeMirror> value = AnnotationValueUtil.toTypeList(scopes);
    if (value == null) {
      throw new AptIllegalStateException(SCOPES);
    }
    return value;
  }
}
