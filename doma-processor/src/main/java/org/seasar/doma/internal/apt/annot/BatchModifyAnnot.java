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

public abstract class BatchModifyAnnot extends AbstractAnnot {

  public static final String SQL_FILE = "sqlFile";
  public static final String QUERY_TIMEOUT = "queryTimeout";
  public static final String BATCH_SIZE = "batchSize";
  public static final String IGNORE_VERSION = "ignoreVersion";
  public static final String SUPPRESS_OPTIMISTIC_LOCK_EXCEPTION = "suppressOptimisticLockException";
  public static final String SQL_LOG = "sqlLog";
  public static final String INCLUDE = "include";
  public static final String EXCLUDE = "exclude";

  public static final String IGNORE_GENERATED_KEYS = "ignoreGeneratedKeys";

  private static final String DUPLICATE_KEY_TYPE = "duplicateKeyType";

  private final AnnotationValue sqlFile;

  private final AnnotationValue queryTimeout;

  private final AnnotationValue batchSize;

  private final AnnotationValue ignoreVersion;

  private final AnnotationValue suppressOptimisticLockException;

  private final AnnotationValue include;

  private final AnnotationValue exclude;

  private final AnnotationValue sqlLog;

  private final AnnotationValue ignoreGeneratedKeys;

  private final AnnotationValue duplicateKeyType;

  BatchModifyAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);

    // non null values
    this.sqlFile = assertNonNullValue(values, SQL_FILE);
    this.queryTimeout = assertNonNullValue(values, QUERY_TIMEOUT);
    this.batchSize = assertNonNullValue(values, BATCH_SIZE);
    this.sqlLog = assertNonNullValue(values, SQL_LOG);

    // nullable values
    this.ignoreVersion = values.get(IGNORE_VERSION);
    this.suppressOptimisticLockException = values.get(SUPPRESS_OPTIMISTIC_LOCK_EXCEPTION);
    this.include = values.get(INCLUDE);
    this.exclude = values.get(EXCLUDE);
    this.ignoreGeneratedKeys = values.get(IGNORE_GENERATED_KEYS);
    this.duplicateKeyType = values.get(DUPLICATE_KEY_TYPE);
  }

  public AnnotationValue getSqlFile() {
    return sqlFile;
  }

  public AnnotationValue getQueryTimeout() {
    return queryTimeout;
  }

  public AnnotationValue getBatchSize() {
    return batchSize;
  }

  public AnnotationValue getIgnoreVersion() {
    return ignoreVersion;
  }

  public AnnotationValue getSuppressOptimisticLockException() {
    return suppressOptimisticLockException;
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

  public AnnotationValue getIgnoreGeneratedKeys() {
    return ignoreGeneratedKeys;
  }

  public DuplicateKeyType getDuplicateKeyType() {
    VariableElement enumConstant = AnnotationValueUtil.toEnumConstant(duplicateKeyType);
    if (enumConstant == null) {
      throw new AptIllegalStateException(DUPLICATE_KEY_TYPE);
    }
    return DuplicateKeyType.valueOf(enumConstant.getSimpleName().toString());
  }

  public int getQueryTimeoutValue() {
    Integer value = AnnotationValueUtil.toInteger(queryTimeout);
    if (value == null) {
      throw new AptIllegalStateException(QUERY_TIMEOUT);
    }
    return value;
  }

  public int getBatchSizeValue() {
    Integer value = AnnotationValueUtil.toInteger(batchSize);
    if (value == null) {
      throw new AptIllegalStateException(BATCH_SIZE);
    }
    return value;
  }

  public Boolean getIgnoreVersionValue() {
    return AnnotationValueUtil.toBoolean(ignoreVersion);
  }

  public Boolean getSuppressOptimisticLockExceptionValue() {
    return AnnotationValueUtil.toBoolean(suppressOptimisticLockException);
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

  public Boolean getIgnoreGeneratedKeysValues() {
    return AnnotationValueUtil.toBoolean(ignoreGeneratedKeys);
  }
}
