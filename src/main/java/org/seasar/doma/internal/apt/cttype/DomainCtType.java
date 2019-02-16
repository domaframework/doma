package org.seasar.doma.internal.apt.cttype;

import static java.util.stream.Collectors.toList;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.generator.Code;

public class DomainCtType extends AbstractCtType {

  private final BasicCtType basicCtType;

  private final List<CtType> typeArgCtTypes;

  private final ClassName descClassName;

  DomainCtType(
      Context ctx,
      TypeMirror type,
      BasicCtType basicCtType,
      List<CtType> typeArgCtTypes,
      ClassName descClassName) {
    super(ctx, type);
    assertNotNull(basicCtType, typeArgCtTypes, descClassName);
    this.basicCtType = basicCtType;
    this.typeArgCtTypes = typeArgCtTypes;
    this.descClassName = descClassName;
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

  public Code getDescCode() {
    return new Code(
        p -> {
          if (typeArgCtTypes.isEmpty()) {
            p.print("%1$s.getSingletonInternal()", descClassName);
          } else {
            List<TypeMirror> typeArgs =
                typeArgCtTypes.stream().map(CtType::getType).collect(toList());
            p.print("%1$s.<%2$s>getSingletonInternal()", descClassName, typeArgs);
          }
        });
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitDomainCtType(this, p);
  }
}
