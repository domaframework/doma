package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class IterableCtType extends AbstractCtType {

  protected TypeMirror elementTypeMirror;

  protected CtType elementCtType;

  public IterableCtType(TypeMirror type, Context ctx) {
    super(type, ctx);
  }

  public CtType getElementCtType() {
    return elementCtType;
  }

  public boolean isRawType() {
    return elementTypeMirror == null;
  }

  public boolean isWildcardType() {
    return elementTypeMirror != null && elementTypeMirror.getKind() == TypeKind.WILDCARD;
  }

  public boolean isList() {
    return ctx.getTypes().isSameType(typeMirror, List.class);
  }

  public static IterableCtType newInstance(TypeMirror type, Context ctx) {
    assertNotNull(type, ctx);
    TypeMirror supertype = ctx.getTypes().getSupertypeMirror(type, Iterable.class);
    if (supertype == null) {
      return null;
    }
    IterableCtType iterableCtType = new IterableCtType(type, ctx);
    DeclaredType declaredType = ctx.getTypes().toDeclaredType(supertype);
    if (declaredType == null) {
      return null;
    }
    List<? extends TypeMirror> typeArgs = declaredType.getTypeArguments();
    if (typeArgs.size() > 0) {
      iterableCtType.elementTypeMirror = typeArgs.get(0);
      iterableCtType.elementCtType =
          buildCtTypeSuppliers(iterableCtType.elementTypeMirror, ctx)
              .stream()
              .map(Supplier::get)
              .filter(Objects::nonNull)
              .findFirst()
              .get();
    }
    return iterableCtType;
  }

  protected static List<Supplier<CtType>> buildCtTypeSuppliers(TypeMirror typeMirror, Context ctx) {
    return Arrays.<Supplier<CtType>>asList(
        () -> EntityCtType.newInstance(typeMirror, ctx),
        () -> OptionalCtType.newInstance(typeMirror, ctx),
        () -> OptionalIntCtType.newInstance(typeMirror, ctx),
        () -> OptionalLongCtType.newInstance(typeMirror, ctx),
        () -> OptionalDoubleCtType.newInstance(typeMirror, ctx),
        () -> DomainCtType.newInstance(typeMirror, ctx),
        () -> BasicCtType.newInstance(typeMirror, ctx),
        () -> MapCtType.newInstance(typeMirror, ctx),
        () -> AnyCtType.newInstance(typeMirror, ctx));
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitIterableCtType(this, p);
  }
}
