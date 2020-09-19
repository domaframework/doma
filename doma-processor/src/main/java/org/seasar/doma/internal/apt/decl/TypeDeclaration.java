package org.seasar.doma.internal.apt.decl;

import static java.util.stream.Collectors.toList;
import static javax.lang.model.util.ElementFilter.*;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.MoreTypes;
import org.seasar.doma.internal.apt.cttype.ArrayCtType;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.DomainCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.util.Pair;
import org.seasar.doma.internal.util.Zip;

public class TypeDeclaration {

  private static final Map<String, Integer> NUMBER_PRIORITY_MAP = new HashMap<>();

  static {
    NUMBER_PRIORITY_MAP.put(BigDecimal.class.getName(), 80);
    NUMBER_PRIORITY_MAP.put(BigInteger.class.getName(), 70);
    NUMBER_PRIORITY_MAP.put(double.class.getName(), 60);
    NUMBER_PRIORITY_MAP.put(Double.class.getName(), 60);
    NUMBER_PRIORITY_MAP.put(float.class.getName(), 50);
    NUMBER_PRIORITY_MAP.put(Float.class.getName(), 50);
    NUMBER_PRIORITY_MAP.put(long.class.getName(), 40);
    NUMBER_PRIORITY_MAP.put(Long.class.getName(), 40);
    NUMBER_PRIORITY_MAP.put(int.class.getName(), 30);
    NUMBER_PRIORITY_MAP.put(Integer.class.getName(), 30);
    NUMBER_PRIORITY_MAP.put(short.class.getName(), 20);
    NUMBER_PRIORITY_MAP.put(Short.class.getName(), 20);
    NUMBER_PRIORITY_MAP.put(byte.class.getName(), 10);
    NUMBER_PRIORITY_MAP.put(Byte.class.getName(), 10);
  }

  private final Context ctx;

  private final TypeMirror type;

  private final CtType ctType;

  private final TypeElement typeElement;

  private final Map<Name, List<TypeParameterDeclaration>> typeParameterDeclarationsMap;

  private final int numberPriority;

  protected TypeDeclaration(
      Context ctx,
      TypeMirror type,
      CtType ctType,
      TypeElement typeElement,
      Map<Name, List<TypeParameterDeclaration>> typeParameterDeclarationsMap) {
    assertNotNull(ctx, type, ctType, typeParameterDeclarationsMap);
    this.ctx = ctx;
    this.type = type;
    this.ctType = ctType;
    this.typeElement = typeElement; // nullable
    this.typeParameterDeclarationsMap = typeParameterDeclarationsMap;
    this.numberPriority = determineNumberPriority(typeElement, type);
  }

  public TypeMirror getType() {
    return type;
  }

  public String getBinaryName() {
    if (typeElement == null) {
      return type.toString();
    }
    return ctx.getMoreElements().getBinaryName(typeElement).toString();
  }

  public boolean isUnknownType() {
    return type.getKind() == TypeKind.NONE;
  }

  public boolean isNullType() {
    return type.getKind() == TypeKind.NULL;
  }

  public boolean isBooleanType() {
    return type.getKind() == TypeKind.BOOLEAN
        || ctx.getMoreTypes().isSameTypeWithErasure(type, Boolean.class);
  }

  public boolean isTextType() {
    return type.getKind() == TypeKind.CHAR
        || ctx.getMoreTypes().isSameTypeWithErasure(type, String.class)
        || ctx.getMoreTypes().isSameTypeWithErasure(type, Character.class);
  }

  public boolean isNumberType() {
    switch (type.getKind()) {
      case BYTE:
      case SHORT:
      case INT:
      case LONG:
      case FLOAT:
      case DOUBLE:
        return true;
      default:
        if (typeElement == null) {
          return false;
        }
        String canonicalName = typeElement.getQualifiedName().toString();
        return NUMBER_PRIORITY_MAP.containsKey(canonicalName);
    }
  }

  public boolean is(Class<?> clazz) {
    return ctx.getMoreTypes().isSameTypeWithErasure(type, clazz);
  }

  public boolean isScalarType() {
    return ctType.accept(new ScalarDetector(), null);
  }

  public boolean isScalarIterableType() {
    return ctType.accept(
        new SimpleCtTypeVisitor<Boolean, Void, RuntimeException>() {

          @Override
          protected Boolean defaultAction(CtType ctType, Void aVoid) {
            return false;
          }

          @Override
          public Boolean visitIterableCtType(IterableCtType ctType, Void aVoid) {
            return ctType.getElementCtType().accept(new ScalarDetector(), aVoid);
          }
        },
        null);
  }

  public boolean isScalarArrayType() {
    return ctType.accept(
        new SimpleCtTypeVisitor<Boolean, Void, RuntimeException>() {
          @Override
          protected Boolean defaultAction(CtType ctType, Void aVoid) {
            return false;
          }

          @Override
          public Boolean visitArrayCtType(ArrayCtType ctType, Void aVoid) {
            return ctType.getElementCtType().accept(new ScalarDetector(), aVoid);
          }
        },
        null);
  }

  public List<TypeParameterDeclaration> getTypeParameterDeclarations() {
    Optional<List<TypeParameterDeclaration>> typeParameterDeclarations =
        typeParameterDeclarationsMap.values().stream().findFirst();
    return typeParameterDeclarations.orElse(Collections.emptyList());
  }

  public Optional<ConstructorDeclaration> getConstructorDeclaration(
      List<TypeDeclaration> parameterTypeDeclarations) {
    if (typeElement == null) {
      return Optional.empty();
    }
    return constructorsIn(typeElement.getEnclosedElements()).stream()
        .filter(c -> c.getModifiers().contains(Modifier.PUBLIC))
        .filter(c -> c.getParameters().size() == parameterTypeDeclarations.size())
        .filter(c -> isAssignable(parameterTypeDeclarations, c.getParameters()))
        .map(c -> ctx.getDeclarations().newConstructorDeclaration(c))
        .findAny();
  }

  public Optional<FieldDeclaration> getFieldDeclaration(String name) {
    return getFieldDeclarationInternal(name, false);
  }

  public Optional<FieldDeclaration> getStaticFieldDeclaration(String name) {
    return getFieldDeclarationInternal(name, true);
  }

  private Optional<FieldDeclaration> getFieldDeclarationInternal(String name, boolean statik) {
    List<FieldDeclaration> candidates = getCandidateFieldDeclarations(name, statik);
    removeHiddenFieldDeclarations(candidates);
    return candidates.stream().findFirst();
  }

  private List<FieldDeclaration> getCandidateFieldDeclarations(String name, boolean statik) {
    return typeParameterDeclarationsMap.entrySet().stream()
        .map(e -> new Pair<>(e.getKey(), e.getValue()))
        .map(p -> new Pair<>(ctx.getMoreElements().getTypeElement(p.fst), p.snd))
        .filter(p -> Objects.nonNull(p.fst))
        .flatMap(
            p ->
                fieldsIn(p.fst.getEnclosedElements()).stream()
                    .filter(f -> !statik || f.getModifiers().contains(Modifier.STATIC))
                    .filter(f -> f.getSimpleName().contentEquals(name))
                    .map(f -> ctx.getDeclarations().newFieldDeclaration(f, p.snd)))
        .collect(toList());
  }

  private void removeHiddenFieldDeclarations(List<FieldDeclaration> candidates) {
    List<FieldDeclaration> hiders = new LinkedList<>(candidates);
    for (Iterator<FieldDeclaration> it = candidates.iterator(); it.hasNext(); ) {
      FieldDeclaration hidden = it.next();
      for (FieldDeclaration hider : hiders) {
        if (ctx.getMoreElements().hides(hider.getElement(), hidden.getElement())) {
          it.remove();
          break;
        }
      }
    }
  }

  public Optional<MethodDeclaration> getMethodDeclaration(
      String name, List<TypeDeclaration> parameterTypeDeclarations) {
    return getMethodDeclarationInternal(name, parameterTypeDeclarations, false);
  }

  public Optional<MethodDeclaration> getStaticMethodDeclaration(
      String name, List<TypeDeclaration> parameterTypeDeclarations) {
    return getMethodDeclarationInternal(name, parameterTypeDeclarations, true);
  }

  private Optional<MethodDeclaration> getMethodDeclarationInternal(
      String name, List<TypeDeclaration> parameterTypeDeclarations, boolean statik) {
    List<MethodDeclaration> candidates =
        getCandidateMethodDeclarations(name, parameterTypeDeclarations, statik);
    removeOverriddenMethodDeclarations(candidates);
    removeHiddenMethodDeclarations(candidates);
    return candidates.stream().findFirst();
  }

  private List<MethodDeclaration> getCandidateMethodDeclarations(
      String name, List<TypeDeclaration> parameterTypeDeclarations, boolean statik) {
    return typeParameterDeclarationsMap.entrySet().stream()
        .map(e -> new Pair<>(e.getKey(), e.getValue()))
        .map(p -> new Pair<>(ctx.getMoreElements().getTypeElement(p.fst), p.snd))
        .filter(p -> Objects.nonNull(p.fst))
        .flatMap(
            p ->
                methodsIn(p.fst.getEnclosedElements()).stream()
                    .filter(m -> !statik || m.getModifiers().contains(Modifier.STATIC))
                    .filter(m -> m.getModifiers().contains(Modifier.PUBLIC))
                    .filter(m -> m.getSimpleName().contentEquals(name))
                    .filter(m -> m.getReturnType().getKind() != TypeKind.VOID)
                    .filter(m -> m.getParameters().size() == parameterTypeDeclarations.size())
                    .filter(m -> isAssignable(parameterTypeDeclarations, m.getParameters()))
                    .map(m -> ctx.getDeclarations().newMethodDeclaration(m, p.snd)))
        .collect(toList());
  }

  private void removeOverriddenMethodDeclarations(List<MethodDeclaration> candidates) {
    List<MethodDeclaration> overriders = new LinkedList<>(candidates);
    for (Iterator<MethodDeclaration> it = candidates.iterator(); it.hasNext(); ) {
      MethodDeclaration overridden = it.next();
      for (MethodDeclaration overrider : overriders) {
        TypeElement overriderTypeElement =
            ctx.getMoreElements().toTypeElement(overrider.getElement().getEnclosingElement());
        if (overriderTypeElement == null) {
          continue;
        }
        if (ctx.getMoreElements()
            .overrides(overrider.getElement(), overridden.getElement(), overriderTypeElement)) {
          it.remove();
          break;
        }
      }
    }
  }

  private void removeHiddenMethodDeclarations(List<MethodDeclaration> candidates) {
    List<MethodDeclaration> hiders = new LinkedList<>(candidates);
    for (Iterator<MethodDeclaration> it = candidates.iterator(); it.hasNext(); ) {
      MethodDeclaration hidden = it.next();
      for (MethodDeclaration hider : hiders) {
        TypeMirror subtype = hider.getElement().getEnclosingElement().asType();
        TypeMirror supertype = hidden.getElement().getEnclosingElement().asType();
        if (ctx.getMoreTypes().isAssignableWithErasure(subtype, supertype)) {
          if (ctx.getMoreElements().hides(hider.getElement(), hidden.getElement())) {
            it.remove();
            break;
          }
        }
      }
    }
  }

  public TypeDeclaration emulateConcatOperation(TypeDeclaration other) {
    assertNotNull(other);
    assertTrue(isTextType());
    assertTrue(other.isTextType());
    TypeMirror type = ctx.getMoreTypes().getTypeMirror(String.class);
    return ctx.getDeclarations().newTypeDeclaration(type);
  }

  public TypeDeclaration emulateArithmeticOperation(TypeDeclaration other) {
    assertNotNull(other);
    assertTrue(isNumberType());
    assertTrue(other.isNumberType());
    TypeMirror type = this.numberPriority >= other.numberPriority ? this.type : other.type;
    return ctx.getDeclarations().newTypeDeclaration(type);
  }

  public boolean isSameType(TypeDeclaration other) {
    if (ctx.getMoreTypes().isSameTypeWithErasure(this.type, other.type)) {
      return true;
    }
    if (this.isNumberType()) {
      if (other.isNumberType()) {
        return this.numberPriority == other.numberPriority;
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return type.toString();
  }

  private static int determineNumberPriority(TypeElement typeElement, TypeMirror type) {
    if (typeElement != null) {
      String canonicalName = typeElement.getQualifiedName().toString();
      Integer result = NUMBER_PRIORITY_MAP.get(canonicalName);
      if (result != null) {
        return result;
      }
    }
    Integer result = NUMBER_PRIORITY_MAP.get(type.getKind().name().toLowerCase());
    if (result != null) {
      return result;
    }
    return 0;
  }

  private boolean isAssignable(
      List<TypeDeclaration> parameterTypeDeclarations, List<? extends VariableElement> parameters) {
    MoreTypes types = ctx.getMoreTypes();
    return Zip.stream(parameterTypeDeclarations, parameters)
        .map(p -> p.map(TypeDeclaration::getType, Element::asType))
        .map(p -> p.map(types::boxIfPrimitive, types::boxIfPrimitive))
        .allMatch(p -> types.isAssignableWithErasure(p.fst, p.snd));
  }

  private static class ScalarDetector extends SimpleCtTypeVisitor<Boolean, Void, RuntimeException> {

    private ScalarDetector() {
      super(false);
    }

    @Override
    public Boolean visitBasicCtType(BasicCtType ctType, Void p) {
      return true;
    }

    @Override
    public Boolean visitDomainCtType(DomainCtType ctType, Void p) {
      return true;
    }
  }
}
