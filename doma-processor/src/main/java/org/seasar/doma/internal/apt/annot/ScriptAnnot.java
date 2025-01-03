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
import org.seasar.doma.jdbc.SqlLogType;

public class ScriptAnnot extends AbstractAnnot {

  private static final String HALT_ON_ERROR = "haltOnError";

  private static final String BLOCK_DELIMITER = "blockDelimiter";

  private static final String SQL_LOG = "sqlLog";

  private final AnnotationValue haltOnError;

  private final AnnotationValue blockDelimiter;

  private final AnnotationValue sqlLog;

  ScriptAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    this.haltOnError = assertNonNullValue(values, HALT_ON_ERROR);
    this.blockDelimiter = assertNonNullValue(values, BLOCK_DELIMITER);
    this.sqlLog = assertNonNullValue(values, SQL_LOG);
  }

  public AnnotationValue getHaltOnError() {
    return haltOnError;
  }

  public AnnotationValue getBlockDelimiter() {
    return blockDelimiter;
  }

  public AnnotationValue getSqlLog() {
    return sqlLog;
  }

  public boolean getHaltOnErrorValue() {
    Boolean value = AnnotationValueUtil.toBoolean(haltOnError);
    if (value == null) {
      throw new AptIllegalStateException("haltOnError");
    }
    return value;
  }

  public String getBlockDelimiterValue() {
    String value = AnnotationValueUtil.toString(blockDelimiter);
    if (value == null) {
      throw new AptIllegalStateException(BLOCK_DELIMITER);
    }
    return value;
  }

  public SqlLogType getSqlLogValue() {
    VariableElement enumConstant = AnnotationValueUtil.toEnumConstant(sqlLog);
    if (enumConstant == null) {
      throw new AptIllegalStateException(SQL_LOG);
    }
    return SqlLogType.valueOf(enumConstant.getSimpleName().toString());
  }
}
