package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNullValue;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class TableAnnot extends AbstractAnnot {

  public static final String QUOTE = "quote";

  public static final String NAME = "name";

  public static final String SCHEMA = "schema";

  public static final String CATALOG = "catalog";

  private final AnnotationValue catalog;

  private final AnnotationValue schema;

  private final AnnotationValue name;

  private final AnnotationValue quote;

  TableAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    assertNotNull(values);
    this.catalog = assertNotNullValue(values, CATALOG);
    this.schema = assertNotNullValue(values, SCHEMA);
    this.name = assertNotNullValue(values, NAME);
    this.quote = assertNotNullValue(values, QUOTE);
  }

  public String getCatalogValue() {
    var value = AnnotationValueUtil.toString(catalog);
    if (value == null) {
      throw new AptIllegalStateException(CATALOG);
    }
    return value;
  }

  public String getSchemaValue() {
    var value = AnnotationValueUtil.toString(schema);
    if (value == null) {
      throw new AptIllegalStateException(CATALOG);
    }
    return value;
  }

  public String getNameValue() {
    var value = AnnotationValueUtil.toString(name);
    if (value == null) {
      throw new AptIllegalStateException(NAME);
    }
    return value;
  }

  public boolean getQuoteValue() {
    var value = AnnotationValueUtil.toBoolean(quote);
    if (value == null) {
      throw new AptIllegalStateException(QUOTE);
    }
    return value;
  }
}
