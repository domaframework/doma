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
import org.seasar.doma.jdbc.Reference;

public class ReferenceCtType extends AbstractCtType {

  protected TypeMirror referentTypeMirror;

  protected CtType referentType;

  public ReferenceCtType(TypeMirror type, Context ctx) {
    super(type, ctx);
  }

  public CtType getReferentCtType() {
    return referentType;
  }

  public TypeMirror getReferentTypeMirror() {
    return referentTypeMirror;
  }

  public boolean isRaw() {
    return referentTypeMirror == null;
  }

  public boolean isWildcardType() {
    return referentTypeMirror != null && referentTypeMirror.getKind() == TypeKind.WILDCARD;
  }

  public static ReferenceCtType newInstance(TypeMirror type, Context ctx) {
    assertNotNull(type, ctx);
    DeclaredType referenceDeclaredType = getReferenceDeclaredType(type, ctx);
    if (referenceDeclaredType == null) {
      return null;
    }
    ReferenceCtType referenceCtType = new ReferenceCtType(type, ctx);
    List<? extends TypeMirror> typeArguments = referenceDeclaredType.getTypeArguments();
    if (typeArguments.size() == 1) {
      referenceCtType.referentTypeMirror = typeArguments.get(0);
      referenceCtType.referentType =
          buildCtTypeSuppliers(referenceCtType.referentTypeMirror, ctx)
              .stream()
              .map(Supplier::get)
              .filter(Objects::nonNull)
              .findFirst()
              .get();
    }
    return referenceCtType;
  }

  protected static List<Supplier<CtType>> buildCtTypeSuppliers(TypeMirror typeMirror, Context ctx) {
    return Arrays.<Supplier<CtType>>asList(
        () -> OptionalCtType.newInstance(typeMirror, ctx),
        () -> OptionalIntCtType.newInstance(typeMirror, ctx),
        () -> OptionalLongCtType.newInstance(typeMirror, ctx),
        () -> OptionalDoubleCtType.newInstance(typeMirror, ctx),
        () -> DomainCtType.newInstance(typeMirror, ctx),
        () -> BasicCtType.newInstance(typeMirror, ctx),
        () -> AnyCtType.newInstance(typeMirror, ctx));
  }

  protected static DeclaredType getReferenceDeclaredType(TypeMirror type, Context ctx) {
    if (ctx.getTypes().isSameType(type, Reference.class)) {
      return ctx.getTypes().toDeclaredType(type);
    }
    for (TypeMirror supertype : ctx.getTypes().directSupertypes(type)) {
      if (ctx.getTypes().isSameType(supertype, Reference.class)) {
        return ctx.getTypes().toDeclaredType(supertype);
      }
      getReferenceDeclaredType(supertype, ctx);
    }
    return null;
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitReferenceCtType(this, p);
  }
}
