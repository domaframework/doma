package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNullValue;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.jdbc.SqlLogType;

public class ScriptAnnot extends AbstractAnnot {

  public static final String SQL_LOG = "sqlLog";

  public static final String BLOCK_DELIMITER = "blockDelimiter";

  public static final String HALT_ON_ERROR = "haltOnError";

  private final AnnotationValue haltOnError;

  private final AnnotationValue blockDelimiter;

  private final AnnotationValue sqlLog;

  ScriptAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    assertNotNull(values);
    this.haltOnError = assertNotNullValue(values, HALT_ON_ERROR);
    this.blockDelimiter = assertNotNullValue(values, BLOCK_DELIMITER);
    this.sqlLog = assertNotNullValue(values, SQL_LOG);
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
    var value = AnnotationValueUtil.toBoolean(haltOnError);
    if (value == null) {
      throw new AptIllegalStateException(HALT_ON_ERROR);
    }
    return value;
  }

  public String getBlockDelimiterValue() {
    var value = AnnotationValueUtil.toString(blockDelimiter);
    if (value == null) {
      throw new AptIllegalStateException(BLOCK_DELIMITER);
    }
    return value;
  }

  public SqlLogType getSqlLogValue() {
    var enumConstant = AnnotationValueUtil.toEnumConstant(sqlLog);
    if (enumConstant == null) {
      throw new AptIllegalStateException(SQL_LOG);
    }
    return SqlLogType.valueOf(enumConstant.getSimpleName().toString());
  }
}
