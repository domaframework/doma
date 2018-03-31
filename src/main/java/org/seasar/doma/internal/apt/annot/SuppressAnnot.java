package org.seasar.doma.internal.apt.annot;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNullValue;

import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.message.Message;

public class SuppressAnnot extends AbstractAnnot {

  public static final String MESSAGES = "messages";

  private final AnnotationValue messages;

  SuppressAnnot(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    assertNotNull(values);
    this.messages = assertNotNullValue(values, MESSAGES);
  }

  public boolean isSuppressed(Message message) {
    var enumConstants = AnnotationValueUtil.toEnumConstantList(messages);
    if (enumConstants == null) {
      throw new AptIllegalStateException(MESSAGES);
    }
    for (var enumConstant : enumConstants) {
      var m = Message.valueOf(enumConstant.getSimpleName().toString());
      if (m == message) {
        return true;
      }
    }
    return false;
  }
}
