package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.generator.Code;
import org.seasar.doma.internal.jdbc.scalar.BasicScalarSuppliers;
import org.seasar.doma.internal.jdbc.scalar.OptionalBasicScalarSuppliers;
import org.seasar.doma.internal.util.Pair;
import org.seasar.doma.internal.wrapper.WrapperSuppliers;

public class BasicCtType extends AbstractCtType {

  private final TypeMirror boxedType;

  private final TypeElement wrapperTypeElement;

  private final TypeMirror wrapperType;

  BasicCtType(Context ctx, TypeMirror type, Pair<TypeElement, TypeMirror> wrapperElementAndType) {
    super(ctx, type);
    assertNotNull(wrapperElementAndType);
    this.boxedType = ctx.getMoreTypes().boxIfPrimitive(type);
    this.wrapperTypeElement = wrapperElementAndType.fst;
    this.wrapperType = wrapperElementAndType.snd;
  }

  public TypeMirror getBoxedType() {
    return boxedType;
  }

  public Code getWrapperSupplierCode() {
    Class<?> clazz = WrapperSuppliers.class;
    String methodName = "of" + wrapperTypeElement.getSimpleName().toString().replace("Wrapper", "");
    return new Code(
        p -> {
          if (isEnum()) {
            p.print("%1$s.%2$s(%3$s.class)", clazz, methodName, getQualifiedName());
          } else {
            p.print("%1$s.%2$s()", clazz, methodName);
          }
        });
  }

  public Code getScalarSupplierCode(boolean optional) {
    Class<?> clazz = optional ? OptionalBasicScalarSuppliers.class : BasicScalarSuppliers.class;
    String methodName = "of" + wrapperTypeElement.getSimpleName().toString().replace("Wrapper", "");
    return new Code(
        p -> {
          if (isEnum()) {
            p.print("%1$s.%2$s(%3$s.class)", clazz, methodName, getQualifiedName());
          } else {
            p.print("%1$s.%2$s()", clazz, methodName);
          }
        });
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitBasicCtType(this, p);
  }
}
