package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNullValue;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

public class SqlAnnot extends AbstractAnnot {
  public static final String VALUE = "value";

  public static final String USE_FILE = "useFile";

  private final AnnotationValue value;

  private final AnnotationValue useFile;

  SqlAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    assertNotNull(values);
    this.value = assertNotNullValue(values, VALUE);
    this.useFile = assertNotNullValue(values, USE_FILE);
  }

  public AnnotationValue getValue() {
    return value;
  }

  public AnnotationValue getUseFile() {
    return useFile;
  }

  public String getValueValue() {
    var v = AnnotationValueUtil.toString(value);
    if (v == null) {
      throw new AptIllegalStateException(VALUE);
    }
    return v;
  }

  public boolean getUseFileValue() {
    var value = AnnotationValueUtil.toBoolean(useFile);
    if (value == null) {
      throw new AptIllegalStateException(USE_FILE);
    }
    return value;
  }
}
