package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.function.Function;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.codespec.CodeSpec;

public class HolderCtType extends ScalarCtType {

  private final BasicCtType basicCtType;

  private final String descClassName;

  HolderCtType(
      Context ctx,
      TypeMirror type,
      BasicCtType basicCtType,
      Function<TypeElement, CodeSpec> codeSpecFactory) {
    super(ctx, type);
    assertNotNull(basicCtType, codeSpecFactory, typeElement);
    this.basicCtType = basicCtType;
    var codeSpec = codeSpecFactory.apply(typeElement);
    this.descClassName = codeSpec.getQualifiedName();
  }

  public BasicCtType getBasicCtType() {
    return basicCtType;
  }

  public String getDescClassName() {
    return descClassName;
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitHolderCtType(this, p);
  }
}
