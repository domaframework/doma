package org.seasar.doma.internal.apt.cttype;

import static java.util.stream.Collectors.toList;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.apt.Context;

public class DomainCtType extends AbstractCtType {

  private final BasicCtType basicCtType;

  private final List<CtType> typeArgCtTypes;

  private final ClassName domainDescClassName;

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
    String binaryName = ctx.getElements().getBinaryNameAsString(typeElement);
    if (external) {
      this.domainDescClassName = ClassNames.newExternalDomainDescClassName(binaryName);
    } else {
      this.domainDescClassName = ClassNames.newDomainDescClassName(binaryName);
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

  public String domainDescSingletonCode() {
    if (typeArgCtTypes.isEmpty()) {
      return String.format("%1$s.getSingletonInternal()", domainDescClassName);
    }
    List<TypeMirror> typeMirrors = typeArgCtTypes.stream().map(CtType::getType).collect(toList());
    String typeArgs = String.join(", ", ctx.getTypes().getTypeParameterNames(typeMirrors));
    return String.format("%1$s.<%2$s>getSingletonInternal()", domainDescClassName, typeArgs);
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitDomainCtType(this, p);
  }
}
