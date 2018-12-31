package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collector;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class CollectorCtType extends AbstractCtType {

  protected CtType targetCtType;

  protected AnyCtType returnCtType;

  public CollectorCtType(TypeMirror type, Context ctx) {
    super(type, ctx);
  }

  public CtType getTargetCtType() {
    return targetCtType;
  }

  public AnyCtType getReturnCtType() {
    return returnCtType;
  }

  public boolean isRawType() {
    return returnCtType.getTypeMirror() == null || targetCtType.getTypeMirror() == null;
  }

  public boolean isWildcardType() {
    return returnCtType.getTypeMirror() != null
            && returnCtType.getTypeMirror().getKind() == TypeKind.WILDCARD
        || targetCtType.getTypeMirror() != null
            && targetCtType.getTypeMirror().getKind() == TypeKind.WILDCARD;
  }

  public static CollectorCtType newInstance(TypeMirror type, Context ctx) {
    assertNotNull(type, ctx);
    DeclaredType collectorDeclaredType = getCollectorDeclaredType(type, ctx);
    if (collectorDeclaredType == null) {
      return null;
    }

    CollectorCtType collectorCtType = new CollectorCtType(type, ctx);
    List<? extends TypeMirror> typeArguments = collectorDeclaredType.getTypeArguments();
    if (typeArguments.size() == 3) {
      TypeMirror targetTypeMirror = typeArguments.get(0);
      TypeMirror returnTypeMirror = typeArguments.get(2);
      collectorCtType.targetCtType =
          buildCtTypeSuppliers(targetTypeMirror, ctx)
              .stream()
              .map(Supplier::get)
              .filter(Objects::nonNull)
              .findFirst()
              .get();
      collectorCtType.returnCtType = AnyCtType.newInstance(returnTypeMirror, ctx);
    }
    return collectorCtType;
  }

  protected static DeclaredType getCollectorDeclaredType(TypeMirror type, Context ctx) {
    if (ctx.getTypes().isSameType(type, Collector.class)) {
      return ctx.getTypes().toDeclaredType(type);
    }
    for (TypeMirror supertype : ctx.getTypes().directSupertypes(type)) {
      if (ctx.getTypes().isSameType(supertype, Collector.class)) {
        return ctx.getTypes().toDeclaredType(supertype);
      }
      DeclaredType result = getCollectorDeclaredType(supertype, ctx);
      if (result != null) {
        return result;
      }
    }
    return null;
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
    return visitor.visitCollectorCtType(this, p);
  }
}
