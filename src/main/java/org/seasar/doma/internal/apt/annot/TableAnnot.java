package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNonNullValue;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class TableAnnot extends AbstractAnnot {

  private static final String CATALOG = "catalog";

  private static final String SCHEMA = "schema";

  private static final String NAME = "name";

  private static final String QUOTE = "quote";

  private final AnnotationValue catalog;

  private final AnnotationValue schema;

  private final AnnotationValue name;

  private final AnnotationValue quote;

  TableAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    this.catalog = assertNonNullValue(values, CATALOG);
    this.schema = assertNonNullValue(values, SCHEMA);
    this.name = assertNonNullValue(values, NAME);
    this.quote = assertNonNullValue(values, QUOTE);
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

  public String getNameValue() {
    String value = AnnotationValueUtil.toString(name);
    if (value == null) {
      throw new AptIllegalStateException(NAME);
    }
    return value;
  }

  public boolean getQuoteValue() {
    Boolean value = AnnotationValueUtil.toBoolean(quote);
    if (value == null) {
      throw new AptIllegalStateException(QUOTE);
    }
    return value.booleanValue();
  }
}
