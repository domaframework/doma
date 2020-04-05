package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.generator.Code;

public class BasicCtType extends AbstractCtType {

  private final TypeMirror boxedType;

  private final TypeMirror wrapperType;

  BasicCtType(Context ctx, TypeMirror type, TypeMirror wrapperType) {
    super(ctx, type);
    assertNotNull(wrapperType);
    this.boxedType = ctx.getMoreTypes().boxIfPrimitive(type);
    this.wrapperType = wrapperType;
  }

  public TypeMirror getBoxedType() {
    return boxedType;
  }

  public Code getWrapperSupplierCode() {
    return new Code(
        p -> {
          if (isEnum()) {
            p.print("() -> new %1$s(%2$s.class)", wrapperType, getQualifiedName());
          } else {
            p.print("%1$s::new", wrapperType);
          }
        });
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitBasicCtType(this, p);
  }
}
