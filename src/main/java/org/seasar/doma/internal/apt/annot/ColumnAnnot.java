package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNonNullValue;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class ColumnAnnot extends AbstractAnnot {

  public static final String INSERTABLE = "insertable";

  public static final String UPDATABLE = "updatable";

  public static final String QUOTE = "quote";

  public static final String NAME = "name";

  private final AnnotationValue name;

  private final AnnotationValue insertable;

  private final AnnotationValue updatable;

  private final AnnotationValue quote;

  ColumnAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    this.name = assertNonNullValue(values, NAME);
    this.insertable = assertNonNullValue(values, INSERTABLE);
    this.updatable = assertNonNullValue(values, UPDATABLE);
    this.quote = assertNonNullValue(values, QUOTE);
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
    String value = AnnotationValueUtil.toString(name);
    if (value == null) {
      throw new AptIllegalStateException(NAME);
    }
    return value;
  }

  public boolean getInsertableValue() {
    Boolean value = AnnotationValueUtil.toBoolean(insertable);
    if (value == null) {
      throw new AptIllegalStateException(INSERTABLE);
    }
    return value.booleanValue();
  }

  public boolean getUpdatableValue() {
    Boolean value = AnnotationValueUtil.toBoolean(updatable);
    if (value == null) {
      throw new AptIllegalStateException(UPDATABLE);
    }
    return value.booleanValue();
  }

  public boolean getQuoteValue() {
    Boolean value = AnnotationValueUtil.toBoolean(quote);
    if (value == null) {
      throw new AptIllegalStateException(QUOTE);
    }
    return value.booleanValue();
  }
}
