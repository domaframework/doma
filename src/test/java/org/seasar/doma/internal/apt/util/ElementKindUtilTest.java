package org.seasar.doma.internal.apt.util;

import static org.junit.jupiter.api.Assertions.*;

import javax.lang.model.element.ElementKind;
import org.junit.jupiter.api.Test;

class ElementKindUtilTest {

  @Test
  void isRecord() {
    assertFalse(ElementKindUtil.isRecord(ElementKind.CLASS));
  }
}
