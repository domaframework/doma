package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.Constants.EXTERNAL_DOMAIN_DESC_ARRAY_SUFFIX;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeMirror;

public class Names {

  private final Context ctx;

  Names(Context ctx) {
    assertNotNull(ctx);
    this.ctx = ctx;
  }

  public Name createExternalDomainName(TypeMirror externalDomainType) {
    assertNotNull(externalDomainType);
    ArrayType arrayType = ctx.getMoreTypes().toArrayType(externalDomainType);
    if (arrayType != null) {
      TypeMirror componentType = arrayType.getComponentType();
      TypeElement componentElement = ctx.getMoreTypes().toTypeElement(componentType);
      if (componentElement == null) {
        throw new AptIllegalStateException(componentType.toString());
      }
      Name binaryName = ctx.getMoreElements().getBinaryName(componentElement);
      return ctx.getMoreElements().getName(binaryName + EXTERNAL_DOMAIN_DESC_ARRAY_SUFFIX);
    }
    TypeElement domainElement = ctx.getMoreTypes().toTypeElement(externalDomainType);
    if (domainElement == null) {
      throw new AptIllegalStateException(externalDomainType.toString());
    }
    return ctx.getMoreElements().getBinaryName(domainElement);
  }
}
