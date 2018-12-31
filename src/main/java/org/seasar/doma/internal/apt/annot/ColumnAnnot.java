package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.Column;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class ColumnAnnot {

  protected final AnnotationMirror annotationMirror;

  protected AnnotationValue name;

  protected AnnotationValue insertable;

  protected AnnotationValue updatable;

  protected AnnotationValue quote;

  protected ColumnAnnot(AnnotationMirror annotationMirror) {
    assertNotNull(annotationMirror);
    this.annotationMirror = annotationMirror;
  }

  public static ColumnAnnot newInstance(VariableElement field, Context ctx) {
    assertNotNull(ctx);
    AnnotationMirror annotationMirror = ctx.getElements().getAnnotationMirror(field, Column.class);
    if (annotationMirror == null) {
      return null;
    }
    ColumnAnnot result = new ColumnAnnot(annotationMirror);
    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
        ctx.getElements().getElementValuesWithDefaults(annotationMirror).entrySet()) {
      String name = entry.getKey().getSimpleName().toString();
      AnnotationValue value = entry.getValue();
      if ("name".equals(name)) {
        result.name = value;
      } else if ("insertable".equals(name)) {
        result.insertable = value;
      } else if ("updatable".equals(name)) {
        result.updatable = value;
      } else if ("quote".equals(name)) {
        result.quote = value;
      }
    }
    return result;
  }

  public AnnotationMirror getAnnotationMirror() {
    return annotationMirror;
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
      throw new AptIllegalStateException("name");
    }
    return value;
  }

  public boolean getInsertableValue() {
    Boolean value = AnnotationValueUtil.toBoolean(insertable);
    if (value == null) {
      throw new AptIllegalStateException("insertable");
    }
    return value.booleanValue();
  }

  public boolean getUpdatableValue() {
    Boolean value = AnnotationValueUtil.toBoolean(updatable);
    if (value == null) {
      throw new AptIllegalStateException("updatable");
    }
    return value.booleanValue();
  }

  public boolean getQuoteValue() {
    Boolean value = AnnotationValueUtil.toBoolean(quote);
    if (value == null) {
      throw new AptIllegalStateException("quote");
    }
    return value.booleanValue();
  }
}
