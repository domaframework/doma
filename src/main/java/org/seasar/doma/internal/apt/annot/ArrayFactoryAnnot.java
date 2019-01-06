package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class ArrayFactoryAnnot extends AbstractAnnot {

  private static final String TYPE_NAME = "typeName";

  private final AnnotationValue typeName;

  ArrayFactoryAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    this.typeName = assertNonNullValue(values, TYPE_NAME);
  }

  public String getTypeNameValue() {
    String result = AnnotationValueUtil.toString(typeName);
    if (result == null) {
      throw new AptIllegalStateException(TYPE_NAME);
    }
    return result;
  }
}
