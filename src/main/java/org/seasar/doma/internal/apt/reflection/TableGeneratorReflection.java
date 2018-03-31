package org.seasar.doma.internal.apt.reflection;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNullValue;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

/** @author taedium */
public class TableGeneratorReflection extends AbstractReflection {

  public static final String IMPLEMENTER = "implementer";

  public static final String ALLOCATION_SIZE = "allocationSize";

  public static final String INITIAL_VALUE = "initialValue";

  public static final String PK_COLUMN_VALUE = "pkColumnValue";

  public static final String VALUE_COLUMN_NAME = "valueColumnName";

  public static final String PK_COLUMN_NAME = "pkColumnName";

  public static final String TABLE = "table";

  public static final String SCHEMA = "schema";

  public static final String CATALOG = "catalog";

  private final AnnotationValue catalog;

  private final AnnotationValue schema;

  private final AnnotationValue table;

  private final AnnotationValue pkColumnName;

  private final AnnotationValue valueColumnName;

  private final AnnotationValue pkColumnValue;

  private final AnnotationValue initialValue;

  private final AnnotationValue allocationSize;

  private final AnnotationValue implementer;

  TableGeneratorReflection(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    assertNotNull(values);
    this.catalog = assertNotNullValue(values, CATALOG);
    this.schema = assertNotNullValue(values, SCHEMA);
    this.table = assertNotNullValue(values, TABLE);
    this.pkColumnName = assertNotNullValue(values, PK_COLUMN_NAME);
    this.valueColumnName = assertNotNullValue(values, VALUE_COLUMN_NAME);
    this.pkColumnValue = assertNotNullValue(values, PK_COLUMN_VALUE);
    this.initialValue = assertNotNullValue(values, INITIAL_VALUE);
    this.allocationSize = assertNotNullValue(values, ALLOCATION_SIZE);
    this.implementer = assertNotNullValue(values, IMPLEMENTER);
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
