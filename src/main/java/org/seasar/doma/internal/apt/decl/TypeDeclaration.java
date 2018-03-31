package org.seasar.doma.internal.apt.decl;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
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

  private final List<TypeParameterDeclaration> typeParameterDeclarations;

  private final List<TypeDeclaration> supertypeDeclarations;

  private final TypeElement typeElement;

  private final int numberPriority;

  TypeDeclaration(
      Context ctx,
      TypeMirror type,
      List<TypeParameterDeclaration> typeParameterDeclarations,
      List<TypeDeclaration> supertypeDeclarations) {
    this.ctx = ctx;
    this.type = type;
    this.typeElement = ctx.getTypes().toTypeElement(type);
    this.typeParameterDeclarations = typeParameterDeclarations;
    this.supertypeDeclarations = supertypeDeclarations;
    this.numberPriority = determineNumberPriority(ctx, type, typeElement);
  }

  private static int determineNumberPriority(
      Context ctx, TypeMirror type, TypeElement typeElement) {
    if (typeElement != null) {
      Integer result =
          NUMBER_PRIORITY_MAP.get(ctx.getElements().getBinaryName(typeElement).toString());
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

  public TypeMirror getType() {
    return type;
  }

  public String getBinaryName() {
    if (typeElement == null) {
      return type.toString();
    }
    return ctx.getElements().getBinaryName(typeElement).toString();
  }

  public boolean isUnknownType() {
    return type.getKind() == TypeKind.NONE;
  }

  public boolean isNullType() {
    return type.getKind() == TypeKind.NULL;
  }

  public boolean isBooleanType() {
    return type.getKind() == TypeKind.BOOLEAN || ctx.getTypes().isSameType(type, Boolean.class);
  }

  public boolean isTextType() {
    return type.getKind() == TypeKind.CHAR
        || ctx.getTypes().isSameType(type, String.class)
        || ctx.getTypes().isSameType(type, Character.class);
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
        return ctx.getTypes().isAssignable(type, Number.class);
    }
  }

  public boolean is(Class<?> clazz) {
    return ctx.getTypes().isSameType(type, clazz);
  }

  public List<TypeParameterDeclaration> getTypeParameterDeclarations() {
    return this.typeParameterDeclarations;
  }

  public List<ConstructorDeclaration> getConstructorDeclarations(
      List<TypeDeclaration> parameterTypeDeclarations) {
    List<ConstructorDeclaration> candidates =
        getCandidateConstructorDeclarations(parameterTypeDeclarations);
    if (candidates.size() == 1) {
      return candidates;
    }
    ConstructorDeclaration constructorDeclaration =
        findSuitableConstructorDeclaration(parameterTypeDeclarations, candidates);
    if (constructorDeclaration != null) {
      return Collections.singletonList(constructorDeclaration);
    }
    return candidates;
  }

  private List<ConstructorDeclaration> getCandidateConstructorDeclarations(
      List<TypeDeclaration> parameterTypeDeclarations) {
    return Optional.of(typeElement)
        .stream()
        .flatMap(t -> ElementFilter.constructorsIn(t.getEnclosedElements()).stream())
        .filter(e -> e.getModifiers().contains(Modifier.PUBLIC))
        .filter(e -> e.getParameters().size() == parameterTypeDeclarations.size())
        .filter(e -> isAssignable(parameterTypeDeclarations, e.getParameters()))
        .map(ctx.getDeclarations()::newConstructorDeclaration)
        .collect(Collectors.toList());
  }

  private ConstructorDeclaration findSuitableConstructorDeclaration(
      List<TypeDeclaration> parameterTypeDeclarations, List<ConstructorDeclaration> candidates) {
    return candidates
        .stream()
        .filter(c -> isSameType(parameterTypeDeclarations, c.getElement().getParameters()))
        .findFirst()
        .orElse(null);
  }

  public FieldDeclaration getFieldDeclaration(String name) {
    return getFieldDeclarationInternal(name, false);
  }

  public FieldDeclaration getStaticFieldDeclaration(String name) {
    return getFieldDeclarationInternal(name, true);
  }

  public FieldDeclaration getFieldDeclarationInternal(String name, boolean isStatic) {
    List<FieldDeclaration> candidates = getCandidateFieldDeclaration(name, isStatic);
    removeHiddenFieldDeclarations(candidates);

    if (candidates.size() == 0) {
      return null;
    }
    if (candidates.size() == 1) {
      return candidates.get(0);
    }
    throw new AptIllegalStateException(name);
  }

  public List<FieldDeclaration> getCandidateFieldDeclaration(String name, boolean isStatic) {
    return Stream.concat(Stream.of(this), supertypeDeclarations.stream())
        .map(t -> t.typeElement)
        .filter(Objects::nonNull)
        .flatMap(t -> ElementFilter.fieldsIn(t.getEnclosedElements()).stream())
        .filter(v -> !isStatic || v.getModifiers().contains(Modifier.STATIC))
        .filter(v -> v.getSimpleName().contentEquals(name))
        .map(v -> ctx.getDeclarations().newFieldDeclaration(v, typeParameterDeclarations))
        .collect(Collectors.toList());
  }

  private void removeHiddenFieldDeclarations(List<FieldDeclaration> candidates) {
    List<FieldDeclaration> hiders = new LinkedList<>(candidates);
    for (Iterator<FieldDeclaration> it = candidates.iterator(); it.hasNext(); ) {
      FieldDeclaration hidden = it.next();
      for (FieldDeclaration hider : hiders) {
        if (ctx.getElements().hides(hider.getElement(), hidden.getElement())) {
          it.remove();
          break;
        }
      }
    }
  }

  public List<MethodDeclaration> getMethodDeclarations(
      String name, List<TypeDeclaration> parameterTypeDeclarations) {
    return getMethodDeclarationsInternal(name, parameterTypeDeclarations, false);
  }

  public List<MethodDeclaration> getStaticMethodDeclarations(
      String name, List<TypeDeclaration> parameterTypeDeclarations) {
    return getMethodDeclarationsInternal(name, parameterTypeDeclarations, true);
  }

  private List<MethodDeclaration> getMethodDeclarationsInternal(
      String name, List<TypeDeclaration> parameterTypeDeclarations, boolean isStatic) {
    List<MethodDeclaration> candidates =
        getCandidateMethodDeclarations(name, parameterTypeDeclarations, isStatic);
    removeOverriddenMethodDeclarations(candidates);
    removeHiddenMethodDeclarations(candidates);
    if (candidates.size() == 1) {
      return candidates;
    }
    MethodDeclaration suitableMethodDeclaration =
        findSuitableMethodDeclaration(parameterTypeDeclarations, candidates);
    if (suitableMethodDeclaration != null) {
      return Collections.singletonList(suitableMethodDeclaration);
    }
    return candidates;
  }

  private List<MethodDeclaration> getCandidateMethodDeclarations(
      String name, List<TypeDeclaration> parameterTypeDeclarations, boolean isStatic) {
    return Stream.concat(Stream.of(this), supertypeDeclarations.stream())
        .map(t -> t.typeElement)
        .filter(Objects::nonNull)
        .flatMap(t -> ElementFilter.methodsIn(t.getEnclosedElements()).stream())
        .filter(e -> !isStatic || e.getModifiers().contains(Modifier.STATIC))
        .filter(e -> e.getModifiers().contains(Modifier.PUBLIC))
        .filter(e -> e.getSimpleName().contentEquals(name))
        .filter(e -> e.getReturnType().getKind() != TypeKind.VOID)
        .filter(e -> e.getParameters().size() == parameterTypeDeclarations.size())
        .filter(e -> isAssignable(parameterTypeDeclarations, e.getParameters()))
        .map(e -> ctx.getDeclarations().newMethodDeclaration(e, typeParameterDeclarations))
        .collect(Collectors.toList());
  }

  private void removeOverriddenMethodDeclarations(List<MethodDeclaration> candidates) {
    List<MethodDeclaration> overriders = new LinkedList<>(candidates);
    for (Iterator<MethodDeclaration> it = candidates.iterator(); it.hasNext(); ) {
      MethodDeclaration overridden = it.next();
      for (MethodDeclaration overrider : overriders) {
        TypeElement overriderTypeElement =
            ctx.getElements().toTypeElement(overrider.getElement().getEnclosingElement());
        if (overriderTypeElement == null) {
          continue;
        }
        if (ctx.getElements()
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
        if (ctx.getTypes().isAssignable(subtype, supertype)) {
          if (ctx.getElements().hides(hider.getElement(), hidden.getElement())) {
            it.remove();
            break;
          }
        }
      }
    }
  }

  private MethodDeclaration findSuitableMethodDeclaration(
      List<TypeDeclaration> parameterTypeDeclarations, List<MethodDeclaration> candidates) {
    return candidates
        .stream()
        .filter(e -> isAssignable(parameterTypeDeclarations, e.getElement().getParameters()))
        .findFirst()
        .orElse(null);
  }

  public TypeDeclaration emulateConcatOperation(TypeDeclaration other) {
    assertNotNull(other);
    assertTrue(isTextType());
    assertTrue(other.isTextType());
    TypeMirror type = ctx.getTypes().getType(String.class);
    return ctx.getDeclarations().newTypeDeclaration(type);
  }

  public TypeDeclaration emulateArithmeticOperation(TypeDeclaration other) {
    assertNotNull(other);
    assertTrue(isNumberType());
    assertTrue(other.isNumberType());
    TypeMirror type = this.numberPriority >= other.numberPriority ? this.type : other.type;
    return ctx.getDeclarations().newTypeDeclaration(type);
  }

  public boolean isComparable(TypeDeclaration other) {
    if (ctx.getTypes().isSameType(this.type, other.type)) {
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

  private boolean isSameType(
      List<TypeDeclaration> typeDeclarations, List<? extends VariableElement> parameters) {
    return allMatch(typeDeclarations, parameters, ctx.getTypes()::isSameType);
  }

  private boolean isAssignable(
      List<TypeDeclaration> typeDeclarations, List<? extends VariableElement> parameters) {
    return allMatch(typeDeclarations, parameters, ctx.getTypes()::isAssignable);
  }

  private boolean allMatch(
      List<TypeDeclaration> typeDeclarations,
      List<? extends VariableElement> parameters,
      Predicate<Pair<TypeMirror, TypeMirror>> predicate) {
    Stream<TypeMirror> fst = streamTypeDeclarations(typeDeclarations);
    Stream<TypeMirror> snd = streamVariableElements(parameters);
    return Zip.stream(fst, snd).allMatch(predicate);
  }

  private Stream<TypeMirror> streamTypeDeclarations(List<TypeDeclaration> typeDeclarations) {
    assertNotNull(typeDeclarations);
    return typeDeclarations
        .stream()
        .map(TypeDeclaration::getType)
        .map(ctx.getTypes()::boxIfPrimitive);
  }

  private Stream<TypeMirror> streamVariableElements(List<? extends VariableElement> parameters) {
    assertNotNull(parameters);
    return parameters.stream().map(Element::asType).map(ctx.getTypes()::boxIfPrimitive);
  }
}
