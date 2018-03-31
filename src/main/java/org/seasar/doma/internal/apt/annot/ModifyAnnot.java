package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNullValue;

import java.util.List;
import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.jdbc.SqlLogType;

public abstract class ModifyAnnot extends AbstractAnnot {

  /** */
  public static final String EXCLUDE = "exclude";

  /** */
  public static final String INCLUDE = "include";

  /** */
  public static final String INCLUDE_UNCHANGED = "includeUnchanged";

  /** */
  public static final String SUPPRESS_OPTIMISTIC_LOCK_EXCEPTION = "suppressOptimisticLockException";

  /** */
  public static final String EXCLUDE_NULL = "excludeNull";

  /** */
  public static final String IGNORE_VERSION = "ignoreVersion";

  /** */
  public static final String SQL_LOG = "sqlLog";

  /** */
  public static final String QUERY_TIMEOUT = "queryTimeout";

  /** */
  public static final String SQL_FILE = "sqlFile";

  private final AnnotationValue sqlFile;

  private final AnnotationValue queryTimeout;

  private final AnnotationValue sqlLog;

  private final AnnotationValue ignoreVersion;

  private final AnnotationValue excludeNull;

  private final AnnotationValue suppressOptimisticLockException;

  private final AnnotationValue includeUnchanged;

  private final AnnotationValue include;

  private final AnnotationValue exclude;

  ModifyAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    assertNotNull(values);

    // non null values
    this.sqlFile = assertNotNullValue(values, SQL_FILE);
    this.queryTimeout = assertNotNullValue(values, QUERY_TIMEOUT);
    this.sqlLog = assertNotNullValue(values, SQL_LOG);

    // nullable values
    this.ignoreVersion = values.get(IGNORE_VERSION);
    this.excludeNull = values.get(EXCLUDE_NULL);
    this.suppressOptimisticLockException = values.get(SUPPRESS_OPTIMISTIC_LOCK_EXCEPTION);
    this.includeUnchanged = values.get(INCLUDE_UNCHANGED);
    this.include = values.get(INCLUDE);
    this.exclude = values.get(EXCLUDE);
  }

  public AnnotationValue getSqlFile() {
    return sqlFile;
  }

  public AnnotationValue getQueryTimeout() {
    return queryTimeout;
  }

  public AnnotationValue getIgnoreVersion() {
    return ignoreVersion;
  }

  public AnnotationValue getExcludeNull() {
    return excludeNull;
  }

  public AnnotationValue getSuppressOptimisticLockException() {
    return suppressOptimisticLockException;
  }

  public AnnotationValue getIncludeUnchanged() {
    return includeUnchanged;
  }

  public AnnotationValue getInclude() {
    return include;
  }

  public AnnotationValue getExclude() {
    return exclude;
  }

  public AnnotationValue getSqlLog() {
    return sqlLog;
  }

  public int getQueryTimeoutValue() {
    var value = AnnotationValueUtil.toInteger(queryTimeout);
    if (value == null) {
      throw new AptIllegalStateException(QUERY_TIMEOUT);
    }
    return value;
  }

  public Boolean getIgnoreVersionValue() {
    return AnnotationValueUtil.toBoolean(ignoreVersion);
  }

  public Boolean getExcludeNullValue() {
    return AnnotationValueUtil.toBoolean(excludeNull);
  }

  public Boolean getSuppressOptimisticLockExceptionValue() {
    return AnnotationValueUtil.toBoolean(suppressOptimisticLockException);
  }

  public Boolean getIncludeUnchangedValue() {
    return AnnotationValueUtil.toBoolean(includeUnchanged);
  }

  public List<String> getIncludeValue() {
    return AnnotationValueUtil.toStringList(include);
  }

  public List<String> getExcludeValue() {
    return AnnotationValueUtil.toStringList(exclude);
  }

  public SqlLogType getSqlLogValue() {
    var enumConstant = AnnotationValueUtil.toEnumConstant(sqlLog);
    if (enumConstant == null) {
      throw new AptIllegalStateException(SQL_LOG);
    }
    return SqlLogType.valueOf(enumConstant.getSimpleName().toString());
  }

  public boolean getSqlFileValue() {
    var value = AnnotationValueUtil.toBoolean(sqlFile);
    if (value == null) {
      throw new AptIllegalStateException(SQL_FILE);
    }
    return value;
  }
}
