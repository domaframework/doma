package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.TypeName;

public class DomainCtType extends AbstractCtType {

  private final BasicCtType basicCtType;

  private final List<CtType> typeArgCtTypes;

  private final TypeName domainDescTypeName;

  DomainCtType(
      Context ctx,
      TypeMirror type,
      TypeElement typeElement,
      BasicCtType basicCtType,
      List<CtType> typeArgCtTypes,
      boolean external) {
    super(ctx, type);
    assertNotNull(basicCtType, typeArgCtTypes);
    this.basicCtType = basicCtType;
    this.typeArgCtTypes = typeArgCtTypes;
    if (external) {
      this.domainDescTypeName = ctx.getTypeNames().newExternalDomainDescTypeName(typeElement, type);
    } else {
      this.domainDescTypeName = ctx.getTypeNames().newDomainDescTypeName(typeElement, type);
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

  public String domainTypeSingletonCode() {
    ClassName className = domainDescTypeName.getClassName();
    String typeParametersDeclaration = domainDescTypeName.getTypeParametersDeclaration();
    return className + "." + typeParametersDeclaration + "getSingletonInternal()";
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitDomainCtType(this, p);
  }
}
