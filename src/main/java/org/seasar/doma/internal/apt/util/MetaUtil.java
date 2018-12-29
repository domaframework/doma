package org.seasar.doma.internal.apt.util;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.Conventions;

public class MetaUtil {

  public static String toFullMetaName(TypeElement originalTypeElement, ProcessingEnvironment env) {
    assertNotNull(originalTypeElement, env);
    String binaryName = ElementUtil.getBinaryName(originalTypeElement, env);
    return Conventions.toFullMetaName(binaryName);
  }

  public static String toFullMetaName(String originalBinaryName) {
    assertNotNull(originalBinaryName);
    return Conventions.toFullMetaName(originalBinaryName);
  }

  public static String toSimpleMetaName(
      TypeElement originalTypeElement, ProcessingEnvironment env) {
    assertNotNull(originalTypeElement, env);
    return Conventions.toSimpleMetaName(ElementUtil.getBinaryName(originalTypeElement, env));
  }

  public static String toSimpleMetaName(String originalBinaryName) {
    assertNotNull(originalBinaryName);
    return Conventions.toSimpleMetaName(originalBinaryName);
  }
}
