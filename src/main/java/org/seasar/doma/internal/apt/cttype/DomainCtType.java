package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.Context;

public class DomainCtType extends AbstractCtType {

  private final BasicCtType basicCtType;

  private final List<CtType> typeArgCtTypes;

  private final boolean external;

  private final String metaClassName;

  private final String typeArgDecl;

  DomainCtType(
      Context ctx,
      TypeMirror domainType,
      BasicCtType basicCtType,
      List<CtType> typeArgCtTypes,
      boolean external) {
    super(ctx, domainType);
    assertNotNull(basicCtType, typeArgCtTypes);
    this.basicCtType = basicCtType;
    this.typeArgCtTypes = typeArgCtTypes;
    this.external = external;
    int pos = metaTypeName.indexOf('<');
    if (pos > -1) {
      this.metaClassName = metaTypeName.substring(0, pos);
      this.typeArgDecl = metaTypeName.substring(pos);
    } else {
      this.metaClassName = metaTypeName;
      this.typeArgDecl = "";
    }
  }

  public BasicCtType getBasicCtType() {
    return basicCtType;
  }

  public boolean isRaw() {
    return typeArgCtTypes.stream().anyMatch(CtType::isNone);
  }

  public boolean hasWildcard() {
    return typeArgCtTypes.stream().anyMatch(CtType::isWildcard);
  }

  public boolean hasTypevar() {
    return typeArgCtTypes.stream().anyMatch(CtType::isTypevar);
  }

  public String getInstantiationCommand() {
    return normalize(metaClassName) + "." + typeArgDecl + "getSingletonInternal()";
  }

  @Override
  public String getMetaTypeName() {
    return normalize(metaTypeName);
  }

  protected String normalize(String name) {
    if (external) {
      return Constants.EXTERNAL_DOMAIN_METATYPE_ROOT_PACKAGE + "." + name;
    }
    return name;
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitDomainCtType(this, p);
  }
}
