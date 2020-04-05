package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNonNullValue;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.FetchType;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.SelectType;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.jdbc.SqlLogType;

public class SelectAnnot extends AbstractAnnot {

  private static final String STRATEGY = "strategy";

  private static final String FETCH = "fetch";

  private static final String ENSURE_RESULT = "ensureResult";

  private static final String ENSURE_RESULT_MAPPING = "ensureResultMapping";

  private static final String QUERY_TIMEOUT = "queryTimeout";

  private static final String FETCH_SIZE = "fetchSize";

  private static final String MAX_ROWS = "maxRows";

  private static final String MAP_KEY_NAMING = "mapKeyNaming";

  private static final String SQL_LOG = "sqlLog";

  private final AnnotationValue strategy;

  private final AnnotationValue fetch;

  private final AnnotationValue ensureResult;

  private final AnnotationValue ensureResultMapping;

  private final AnnotationValue queryTimeout;

  private final AnnotationValue fetchSize;

  private final AnnotationValue maxRows;

  private final AnnotationValue mapKeyNaming;

  private final AnnotationValue sqlLog;

  SelectAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    this.strategy = assertNonNullValue(values, STRATEGY);
    this.fetch = assertNonNullValue(values, FETCH);
    this.ensureResult = assertNonNullValue(values, ENSURE_RESULT);
    this.ensureResultMapping = assertNonNullValue(values, ENSURE_RESULT_MAPPING);
    this.queryTimeout = assertNonNullValue(values, QUERY_TIMEOUT);
    this.fetchSize = assertNonNullValue(values, FETCH_SIZE);
    this.maxRows = assertNonNullValue(values, MAX_ROWS);
    this.mapKeyNaming = assertNonNullValue(values, MAP_KEY_NAMING);
    this.sqlLog = assertNonNullValue(values, SQL_LOG);
  }

  public AnnotationValue getStrategy() {
    return strategy;
  }

  public AnnotationValue getFetch() {
    return fetch;
  }

  public AnnotationValue getEnsureResult() {
    return ensureResult;
  }

  public AnnotationValue getEnsureResultMapping() {
    return ensureResultMapping;
  }

  public AnnotationValue getQueryTimeout() {
    return queryTimeout;
  }

  public AnnotationValue getFetchSize() {
    return fetchSize;
  }

  public AnnotationValue getMaxRows() {
    return maxRows;
  }

  public AnnotationValue getMapKeyNaming() {
    return mapKeyNaming;
  }

  public AnnotationValue getSqlLog() {
    return sqlLog;
  }

  public int getQueryTimeoutValue() {
    Integer value = AnnotationValueUtil.toInteger(queryTimeout);
    if (value == null) {
      throw new AptIllegalStateException(QUERY_TIMEOUT);
    }
    return value.intValue();
  }

  public int getFetchSizeValue() {
    Integer value = AnnotationValueUtil.toInteger(fetchSize);
    if (value == null) {
      throw new AptIllegalStateException(FETCH_SIZE);
    }
    return value.intValue();
  }

  public int getMaxRowsValue() {
    Integer value = AnnotationValueUtil.toInteger(maxRows);
    if (value == null) {
      throw new AptIllegalStateException(MAX_ROWS);
    }
    return value.intValue();
  }

  public SelectType getStrategyValue() {
    VariableElement enumConstant = AnnotationValueUtil.toEnumConstant(strategy);
    if (enumConstant == null) {
      throw new AptIllegalStateException(STRATEGY);
    }
    return SelectType.valueOf(enumConstant.getSimpleName().toString());
  }

  public FetchType getFetchValue() {
    VariableElement enumConstant = AnnotationValueUtil.toEnumConstant(fetch);
    if (enumConstant == null) {
      throw new AptIllegalStateException(FETCH);
    }
    return FetchType.valueOf(enumConstant.getSimpleName().toString());
  }

  public boolean getEnsureResultValue() {
    Boolean value = AnnotationValueUtil.toBoolean(ensureResult);
    if (value == null) {
      throw new AptIllegalStateException(ENSURE_RESULT);
    }
    return value.booleanValue();
  }

  public boolean getEnsureResultMappingValue() {
    Boolean value = AnnotationValueUtil.toBoolean(ensureResultMapping);
    if (value == null) {
      throw new AptIllegalStateException(ENSURE_RESULT_MAPPING);
    }
    return value.booleanValue();
  }

  public MapKeyNamingType getMapKeyNamingValue() {
    VariableElement enumConstant = AnnotationValueUtil.toEnumConstant(mapKeyNaming);
    if (enumConstant == null) {
      throw new AptIllegalStateException(MAP_KEY_NAMING);
    }
    return MapKeyNamingType.valueOf(enumConstant.getSimpleName().toString());
  }

  public SqlLogType getSqlLogValue() {
    VariableElement enumConstant = AnnotationValueUtil.toEnumConstant(sqlLog);
    if (enumConstant == null) {
      throw new AptIllegalStateException(SQL_LOG);
    }
    return SqlLogType.valueOf(enumConstant.getSimpleName().toString());
  }
}
