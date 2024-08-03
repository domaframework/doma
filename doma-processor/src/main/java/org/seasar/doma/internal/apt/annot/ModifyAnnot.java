package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNonNullValue;

import java.util.List;
import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

public abstract class ModifyAnnot extends AbstractAnnot {

  private static final String SQL_FILE = "sqlFile";

  private static final String QUERY_TIMEOUT = "queryTimeout";

  private static final String IGNORE_VERSION = "ignoreVersion";

  private static final String EXCLUDE_NULL = "excludeNull";

  private static final String SUPPRESS_OPTIMISTIC_LOCK_EXCEPTION =
      "suppressOptimisticLockException";

  private static final String INCLUDE_UNCHANGED = "includeUnchanged";

  private static final String INCLUDE = "include";

  private static final String EXCLUDE = "exclude";

  private static final String SQL_LOG = "sqlLog";

  private static final String DUPLICATE_KEY_TYPE = "duplicateKeyType";

  private static final String DUPLICATE_KEYS = "duplicateKeys";

  private final AnnotationValue sqlFile;

  private final AnnotationValue queryTimeout;

  private final AnnotationValue ignoreVersion;

  private final AnnotationValue excludeNull;

  private final AnnotationValue suppressOptimisticLockException;

  private final AnnotationValue includeUnchanged;

  private final AnnotationValue include;

  private final AnnotationValue exclude;

  private final AnnotationValue sqlLog;

  private final AnnotationValue duplicateKeyType;

  private final AnnotationValue duplicateKeys;

  ModifyAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);

    // non null values
    this.sqlFile = assertNonNullValue(values, SQL_FILE);
    this.queryTimeout = assertNonNullValue(values, QUERY_TIMEOUT);
    this.sqlLog = assertNonNullValue(values, SQL_LOG);

    // nullable values
    this.ignoreVersion = values.get(IGNORE_VERSION);
    this.excludeNull = values.get(EXCLUDE_NULL);
    this.suppressOptimisticLockException = values.get(SUPPRESS_OPTIMISTIC_LOCK_EXCEPTION);
    this.includeUnchanged = values.get(INCLUDE_UNCHANGED);
    this.include = values.get(INCLUDE);
    this.exclude = values.get(EXCLUDE);
    this.duplicateKeyType = values.get(DUPLICATE_KEY_TYPE);
    this.duplicateKeys = values.get(DUPLICATE_KEYS);
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

  public AnnotationValue getDuplicateKeys() {
    return duplicateKeys;
  }

  public AnnotationValue getSqlLog() {
    return sqlLog;
  }

  public DuplicateKeyType getDuplicateKeyValue() {
    VariableElement enumConstant = AnnotationValueUtil.toEnumConstant(duplicateKeyType);
    if (enumConstant == null) {
      throw new AptIllegalStateException(DUPLICATE_KEY_TYPE);
    }
    return DuplicateKeyType.valueOf(enumConstant.getSimpleName().toString());
  }

  public List<String> getDuplicateKeysValue() {
    return AnnotationValueUtil.toStringList(duplicateKeys);
  }

  public int getQueryTimeoutValue() {
    Integer value = AnnotationValueUtil.toInteger(queryTimeout);
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
    VariableElement enumConstant = AnnotationValueUtil.toEnumConstant(sqlLog);
    if (enumConstant == null) {
      throw new AptIllegalStateException(SQL_LOG);
    }
    return SqlLogType.valueOf(enumConstant.getSimpleName().toString());
  }

  public boolean getSqlFileValue() {
    Boolean value = AnnotationValueUtil.toBoolean(sqlFile);
    if (value == null) {
      throw new AptIllegalStateException(SQL_FILE);
    }
    return value;
  }
}
