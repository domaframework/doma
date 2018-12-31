package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;

public class StreamCtType extends AbstractCtType {

  protected TypeMirror elementTypeMirror;

  protected CtType elementCtType;

  public StreamCtType(TypeMirror type, Context ctx) {
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

  public static StreamCtType newInstance(TypeMirror type, Context ctx) {
    assertNotNull(type, ctx);
    if (!ctx.getTypes().isSameType(type, Stream.class)) {
      return null;
    }
    StreamCtType streamCtType = new StreamCtType(type, ctx);
    DeclaredType declaredType = ctx.getTypes().toDeclaredType(type);
    if (declaredType == null) {
      return null;
    }
    List<? extends TypeMirror> typeArgs = declaredType.getTypeArguments();
    if (typeArgs.size() > 0) {
      streamCtType.elementTypeMirror = typeArgs.get(0);
      streamCtType.elementCtType =
          buildCtTypeSuppliers(streamCtType.elementTypeMirror, ctx)
              .stream()
              .map(Supplier::get)
              .filter(Objects::nonNull)
              .findFirst()
              .get();
    }
    return streamCtType;
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
    return visitor.visitStreamCtType(this, p);
  }
}
