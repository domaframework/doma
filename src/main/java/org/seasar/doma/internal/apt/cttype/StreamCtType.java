package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;

public class StreamCtType extends AbstractCtType {

  protected TypeMirror elementTypeMirror;

  protected CtType elementCtType;

  public StreamCtType(TypeMirror type, ProcessingEnvironment env) {
    super(type, env);
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

  public static StreamCtType newInstance(TypeMirror type, ProcessingEnvironment env) {
    assertNotNull(type, env);
    if (!TypeMirrorUtil.isSameType(type, Stream.class, env)) {
      return null;
    }
    StreamCtType streamCtType = new StreamCtType(type, env);
    DeclaredType declaredType = TypeMirrorUtil.toDeclaredType(type, env);
    if (declaredType == null) {
      return null;
    }
    List<? extends TypeMirror> typeArgs = declaredType.getTypeArguments();
    if (typeArgs.size() > 0) {
      streamCtType.elementTypeMirror = typeArgs.get(0);
      streamCtType.elementCtType =
          buildCtTypeSuppliers(streamCtType.elementTypeMirror, env)
              .stream()
              .map(Supplier::get)
              .filter(Objects::nonNull)
              .findFirst()
              .get();
    }
    return streamCtType;
  }

  protected static List<Supplier<CtType>> buildCtTypeSuppliers(
      TypeMirror typeMirror, ProcessingEnvironment env) {
    return Arrays.<Supplier<CtType>>asList(
        () -> EntityCtType.newInstance(typeMirror, env),
        () -> OptionalCtType.newInstance(typeMirror, env),
        () -> OptionalIntCtType.newInstance(typeMirror, env),
        () -> OptionalLongCtType.newInstance(typeMirror, env),
        () -> OptionalDoubleCtType.newInstance(typeMirror, env),
        () -> DomainCtType.newInstance(typeMirror, env),
        () -> BasicCtType.newInstance(typeMirror, env),
        () -> MapCtType.newInstance(typeMirror, env),
        () -> AnyCtType.newInstance(typeMirror, env));
  }

  @Override
  public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
    return visitor.visitStreamCtType(this, p);
  }
}
