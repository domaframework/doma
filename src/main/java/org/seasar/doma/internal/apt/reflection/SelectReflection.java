package org.seasar.doma.internal.apt.reflection;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNullValue;

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

public class SelectReflection extends AbstractReflection {

  public static final String SQL_LOG = "sqlLog";

  public static final String MAP_KEY_NAMING = "mapKeyNaming";

  public static final String MAX_ROWS = "maxRows";

  public static final String FETCH_SIZE = "fetchSize";

  public static final String QUERY_TIMEOUT = "queryTimeout";

  public static final String ENSURE_RESULT_MAPPING = "ensureResultMapping";

  public static final String ENSURE_RESULT = "ensureResult";

  public static final String FETCH = "fetch";

  public static final String STRATEGY = "strategy";

  private final AnnotationValue strategy;

  private final AnnotationValue fetch;

  private final AnnotationValue ensureResult;

  private final AnnotationValue ensureResultMapping;

  private final AnnotationValue queryTimeout;

  private final AnnotationValue fetchSize;

  private final AnnotationValue maxRows;

  private final AnnotationValue mapKeyNaming;

  private final AnnotationValue sqlLog;

  SelectReflection(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    assertNotNull(values);
    this.strategy = assertNotNullValue(values, STRATEGY);
    this.fetch = assertNotNullValue(values, FETCH);
    this.ensureResult = assertNotNullValue(values, ENSURE_RESULT);
    this.ensureResultMapping = assertNotNullValue(values, ENSURE_RESULT_MAPPING);
    this.queryTimeout = assertNotNullValue(values, QUERY_TIMEOUT);
    this.fetchSize = assertNotNullValue(values, FETCH_SIZE);
    this.maxRows = assertNotNullValue(values, MAX_ROWS);
    this.mapKeyNaming = assertNotNullValue(values, MAP_KEY_NAMING);
    this.sqlLog = assertNotNullValue(values, SQL_LOG);
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
    return value;
  }

  public int getFetchSizeValue() {
    Integer value = AnnotationValueUtil.toInteger(fetchSize);
    if (value == null) {
      throw new AptIllegalStateException(FETCH_SIZE);
    }
    return value;
  }

  public int getMaxRowsValue() {
    Integer value = AnnotationValueUtil.toInteger(maxRows);
    if (value == null) {
      throw new AptIllegalStateException(MAX_ROWS);
    }
    return value;
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
    return value;
  }

  public boolean getEnsureResultMappingValue() {
    Boolean value = AnnotationValueUtil.toBoolean(ensureResultMapping);
    if (value == null) {
      throw new AptIllegalStateException(ENSURE_RESULT_MAPPING);
    }
    return value;
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
