package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNullValue;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class ColumnAnnot extends AbstractAnnot {

  public static final String QUOTE = "quote";

  public static final String UPDATABLE = "updatable";

  public static final String INSERTABLE = "insertable";

  public static final String NAME = "name";

  private final AnnotationValue name;

  private final AnnotationValue insertable;

  private final AnnotationValue updatable;

  private final AnnotationValue quote;

  ColumnAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    assertNotNull(values);
    this.name = assertNotNullValue(values, NAME);
    this.insertable = assertNotNullValue(values, INSERTABLE);
    this.updatable = assertNotNullValue(values, UPDATABLE);
    this.quote = assertNotNullValue(values, QUOTE);
  }

  public AnnotationValue getName() {
    return name;
  }

  public AnnotationValue getInsertable() {
    return insertable;
  }

  public AnnotationValue getUpdatable() {
    return updatable;
  }

  public AnnotationValue getQuote() {
    return quote;
  }

  public String getNameValue() {
    var value = AnnotationValueUtil.toString(name);
    if (value == null) {
      throw new AptIllegalStateException(NAME);
    }
    return value;
  }

  public boolean getInsertableValue() {
    var value = AnnotationValueUtil.toBoolean(insertable);
    if (value == null) {
      throw new AptIllegalStateException(INSERTABLE);
    }
    return value;
  }

  public boolean getUpdatableValue() {
    var value = AnnotationValueUtil.toBoolean(updatable);
    if (value == null) {
      throw new AptIllegalStateException(UPDATABLE);
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
