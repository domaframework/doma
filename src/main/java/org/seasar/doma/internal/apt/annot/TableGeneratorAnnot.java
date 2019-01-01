package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class TableGeneratorAnnot extends AbstractAnnot {

  private static final String CATALOG = "catalog";

  private static final String SCHEMA = "schema";

  private static final String TABLE = "table";

  private static final String PK_COLUMN_NAME = "pkColumnName";

  private static final String VALUE_COLUMN_NAME = "valueColumnName";

  private static final String PK_COLUMN_VALUE = "pkColumnValue";

  private static final String INITIAL_VALUE = "initialValue";

  private static final String ALLOCATION_SIZE = "allocationSize";

  private static final String IMPLEMENTER = "implementer";

  private final AnnotationValue catalog;

  private final AnnotationValue schema;

  private final AnnotationValue table;

  private final AnnotationValue pkColumnName;

  private final AnnotationValue valueColumnName;

  private final AnnotationValue pkColumnValue;

  private final AnnotationValue initialValue;

  private final AnnotationValue allocationSize;

  private final AnnotationValue implementer;

  TableGeneratorAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    this.catalog = assertNonNullValue(values, CATALOG);
    this.schema = assertNonNullValue(values, SCHEMA);
    this.table = assertNonNullValue(values, TABLE);
    this.pkColumnName = assertNonNullValue(values, PK_COLUMN_NAME);
    this.valueColumnName = assertNonNullValue(values, VALUE_COLUMN_NAME);
    this.pkColumnValue = assertNonNullValue(values, PK_COLUMN_VALUE);
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

  public AnnotationValue getTable() {
    return table;
  }

  public AnnotationValue getPkColumnName() {
    return pkColumnName;
  }

  public AnnotationValue getValueColumnName() {
    return valueColumnName;
  }

  public AnnotationValue getPkColumnValue() {
    return pkColumnValue;
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

  public String getTableValue() {
    String value = AnnotationValueUtil.toString(table);
    if (value == null) {
      throw new AptIllegalStateException(TABLE);
    }
    return value;
  }

  public String getPkColumnNameValue() {
    String value = AnnotationValueUtil.toString(pkColumnName);
    if (value == null) {
      throw new AptIllegalStateException(PK_COLUMN_NAME);
    }
    return value;
  }

  public String getValueColumnNameValue() {
    String value = AnnotationValueUtil.toString(valueColumnName);
    if (value == null) {
      throw new AptIllegalStateException(VALUE_COLUMN_NAME);
    }
    return value;
  }

  public String getPkColumnValueValue() {
    String value = AnnotationValueUtil.toString(pkColumnValue);
    if (value == null) {
      throw new AptIllegalStateException(PK_COLUMN_VALUE);
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
