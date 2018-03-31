package org.seasar.doma.internal.apt.reflection;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNullValue;

import java.util.List;
import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.message.Message;

public class SuppressReflection extends AbstractReflection {

  public static final String MESSAGES = "messages";

  private final AnnotationValue messages;

  SuppressReflection(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
    super(annotationMirror);
    assertNotNull(values);
    this.messages = assertNotNullValue(values, MESSAGES);
  }

  public boolean isSuppressed(Message message) {
    List<VariableElement> enumConstants = AnnotationValueUtil.toEnumConstantList(messages);
    if (enumConstants == null) {
      throw new AptIllegalStateException(MESSAGES);
    }
    for (VariableElement enumConstant : enumConstants) {
      Message m = Message.valueOf(enumConstant.getSimpleName().toString());
      if (m == message) {
        return true;
      }
    }
    return false;
  }
}
