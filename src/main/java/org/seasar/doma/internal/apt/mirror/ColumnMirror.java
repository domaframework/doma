package org.seasar.doma.internal.apt.mirror;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Map;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.Column;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.internal.apt.util.ElementUtil;

/** @author taedium */
public class ColumnMirror {

  protected final AnnotationMirror annotationMirror;

  protected AnnotationValue name;

  protected AnnotationValue insertable;

  protected AnnotationValue updatable;

  protected AnnotationValue quote;

  protected ColumnMirror(AnnotationMirror annotationMirror) {
    assertNotNull(annotationMirror);
    this.annotationMirror = annotationMirror;
  }

  public static ColumnMirror newInstance(VariableElement field, ProcessingEnvironment env) {
    assertNotNull(env);
    AnnotationMirror annotationMirror = ElementUtil.getAnnotationMirror(field, Column.class, env);
    if (annotationMirror == null) {
      return null;
    }
    ColumnMirror result = new ColumnMirror(annotationMirror);
    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
        env.getElementUtils().getElementValuesWithDefaults(annotationMirror).entrySet()) {
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
