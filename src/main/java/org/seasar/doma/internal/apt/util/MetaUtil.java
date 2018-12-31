package org.seasar.doma.internal.apt.util;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.Conventions;
import org.seasar.doma.internal.apt.Context;

public class MetaUtil {

  public static String toFullMetaName(TypeElement originalTypeElement, Context ctx) {
    assertNotNull(originalTypeElement, ctx);
    String binaryName = ctx.getElements().getBinaryNameAsString(originalTypeElement);
    return Conventions.toFullMetaName(binaryName);
  }

  public static String toFullMetaName(String originalBinaryName) {
    assertNotNull(originalBinaryName);
    return Conventions.toFullMetaName(originalBinaryName);
  }

  public static String toSimpleMetaName(TypeElement originalTypeElement, Context ctx) {
    assertNotNull(originalTypeElement, ctx);
    String simpleMetaName = ctx.getElements().getBinaryNameAsString(originalTypeElement);
    return Conventions.toSimpleMetaName(simpleMetaName);
  }

  public static String toSimpleMetaName(String originalBinaryName) {
    assertNotNull(originalBinaryName);
    return Conventions.toSimpleMetaName(originalBinaryName);
  }
}
