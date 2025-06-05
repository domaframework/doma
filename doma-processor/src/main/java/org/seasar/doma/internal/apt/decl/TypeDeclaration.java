/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.apt.decl;

import static java.util.stream.Collectors.toList;
import static javax.lang.model.util.ElementFilter.constructorsIn;
import static javax.lang.model.util.ElementFilter.fieldsIn;
import static javax.lang.model.util.ElementFilter.methodsIn;
import static org.seasar.doma.internal.util.AssertionUtil.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.cttype.ArrayCtType;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.DomainCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.util.Pair;
import org.seasar.doma.internal.util.Zip;

public class TypeDeclaration {

  private enum NumberTypePriority {
    BIG_DECIMAL(80),
    BIG_INTEGER(70),
    DOUBLE(60),
    FLOAT(50),
    LONG(40),
    INT(30),
    SHORT(20),
    BYTE(10),
    DEFAULT(0);

    private final int value;

    NumberTypePriority(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }

  private static final Map<String, NumberTypePriority> NUMBER_PRIORITY_MAP =
      createNumberPriorityMap();

  private static Map<String, NumberTypePriority> createNumberPriorityMap() {
    var map = new HashMap<String, NumberTypePriority>();
    map.put(BigDecimal.class.getName(), NumberTypePriority.BIG_DECIMAL);
    map.put(BigInteger.class.getName(), NumberTypePriority.BIG_INTEGER);
    map.put(double.class.getName(), NumberTypePriority.DOUBLE);
    map.put(Double.class.getName(), NumberTypePriority.DOUBLE);
    map.put(float.class.getName(), NumberTypePriority.FLOAT);
    map.put(Float.class.getName(), NumberTypePriority.FLOAT);
    map.put(long.class.getName(), NumberTypePriority.LONG);
    map.put(Long.class.getName(), NumberTypePriority.LONG);
    map.put(int.class.getName(), NumberTypePriority.INT);
    map.put(Integer.class.getName(), NumberTypePriority.INT);
    map.put(short.class.getName(), NumberTypePriority.SHORT);
    map.put(Short.class.getName(), NumberTypePriority.SHORT);
    map.put(byte.class.getName(), NumberTypePriority.BYTE);
    map.put(Byte.class.getName(), NumberTypePriority.BYTE);
    return map;
  }

  private final RoundContext ctx;

  private final TypeMirror type;

  private final CtType ctType;

  private final TypeElement typeElement;

  private final Map<Name, List<TypeParameterDeclaration>> typeParameterDeclarationsMap;

  private final NumberTypePriority numberPriority;

  protected TypeDeclaration(
      RoundContext ctx,
      TypeMirror type,
      CtType ctType,
      TypeElement typeElement,
      Map<Name, List<TypeParameterDeclaration>> typeParameterDeclarationsMap) {
    this.ctx = Objects.requireNonNull(ctx);
    this.type = Objects.requireNonNull(type);
    this.ctType = Objects.requireNonNull(ctType);
    this.typeElement = typeElement; // nullable
    this.typeParameterDeclarationsMap = Objects.requireNonNull(typeParameterDeclarationsMap);
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
    return isPrimitiveTypeKind(TypeKind.BOOLEAN) || isBoxedType(Boolean.class);
  }

  public boolean isTextType() {
    return isPrimitiveTypeKind(TypeKind.CHAR)
        || isBoxedType(String.class)
        || isBoxedType(Character.class);
  }

  public boolean isNumberType() {
    return isPrimitiveNumberType() || isBoxedNumberType();
  }

  private boolean isPrimitiveTypeKind(TypeKind kind) {
    return type.getKind() == kind;
  }

  private boolean isBoxedType(Class<?> clazz) {
    return ctx.getMoreTypes().isSameTypeWithErasure(type, clazz);
  }

  private boolean isPrimitiveNumberType() {
    return switch (type.getKind()) {
      case BYTE, SHORT, INT, LONG, FLOAT, DOUBLE -> true;
      default -> false;
    };
  }

  private boolean isBoxedNumberType() {
    if (typeElement == null) {
      return false;
    }
    var canonicalName = typeElement.getQualifiedName().toString();
    return NUMBER_PRIORITY_MAP.containsKey(canonicalName);
  }

  public boolean is(Class<?> clazz) {
    return ctx.getMoreTypes().isSameTypeWithErasure(type, clazz);
  }

  public boolean isScalarType() {
    return isScalarCtType(ctType);
  }

  public boolean isScalarIterableType() {
    if (ctType instanceof IterableCtType iterableCtType) {
      return isScalarCtType(iterableCtType.getElementCtType());
    }
    return false;
  }

  public boolean isScalarArrayType() {
    if (ctType instanceof ArrayCtType arrayCtType) {
      return isScalarCtType(arrayCtType.getElementCtType());
    }
    return false;
  }

  private boolean isScalarCtType(CtType ctType) {
    return ctType instanceof BasicCtType || ctType instanceof DomainCtType;
  }

  public List<TypeParameterDeclaration> getTypeParameterDeclarations() {
    var typeParameterDeclarations = typeParameterDeclarationsMap.values().stream().findFirst();
    return typeParameterDeclarations.orElse(Collections.emptyList());
  }

  public List<TypeParameterDeclaration> getAllTypeParameterDeclarations() {
    return typeParameterDeclarationsMap.values().stream()
        .flatMap(Collection::stream)
        .collect(toList());
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
    var candidates = getCandidateFieldDeclarations(name, statik);
    removeHiddenFieldDeclarations(candidates);
    return candidates.stream().findFirst();
  }

  private List<FieldDeclaration> getCandidateFieldDeclarations(String name, boolean statik) {
    return processTypeParameterDeclarations(
        (typeElement, typeParams) ->
            fieldsIn(typeElement.getEnclosedElements()).stream()
                .filter(f -> matchesFieldCriteria(f, name, statik))
                .map(f -> ctx.getDeclarations().newFieldDeclaration(f, typeParams)));
  }

  private boolean matchesFieldCriteria(VariableElement field, String name, boolean statik) {
    return (!statik || field.getModifiers().contains(Modifier.STATIC))
        && field.getSimpleName().contentEquals(name);
  }

  private <T> List<T> processTypeParameterDeclarations(TypeElementProcessor<T> processor) {
    return typeParameterDeclarationsMap.entrySet().stream()
        .map(e -> new Pair<>(e.getKey(), e.getValue()))
        .map(p -> new Pair<>(ctx.getMoreElements().getTypeElement(p.fst), p.snd))
        .filter(p -> Objects.nonNull(p.fst))
        .flatMap(p -> processor.process(p.fst, p.snd))
        .collect(toList());
  }

  @FunctionalInterface
  private interface TypeElementProcessor<T> {
    java.util.stream.Stream<T> process(
        TypeElement typeElement, List<TypeParameterDeclaration> typeParams);
  }

  private void removeHiddenFieldDeclarations(List<FieldDeclaration> candidates) {
    removeHiddenDeclarations(
        candidates,
        (hider, hidden) -> ctx.getMoreElements().hides(hider.element(), hidden.element()));
  }

  private <T> void removeHiddenDeclarations(List<T> candidates, HidingPredicate<T> hidesPredicate) {
    var hiders = new LinkedList<>(candidates);
    for (var it = candidates.iterator(); it.hasNext(); ) {
      var hidden = it.next();
      for (var hider : hiders) {
        if (hidesPredicate.hides(hider, hidden)) {
          it.remove();
          break;
        }
      }
    }
  }

  @FunctionalInterface
  private interface HidingPredicate<T> {
    boolean hides(T hider, T hidden);
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
    var candidates = getCandidateMethodDeclarations(name, parameterTypeDeclarations, statik);
    removeOverriddenMethodDeclarations(candidates);
    removeHiddenMethodDeclarations(candidates);
    return candidates.stream().findFirst();
  }

  private List<MethodDeclaration> getCandidateMethodDeclarations(
      String name, List<TypeDeclaration> parameterTypeDeclarations, boolean statik) {
    return processTypeParameterDeclarations(
        (typeElement, typeParams) ->
            methodsIn(typeElement.getEnclosedElements()).stream()
                .filter(m -> matchesMethodCriteria(m, name, parameterTypeDeclarations, statik))
                .map(m -> ctx.getDeclarations().newMethodDeclaration(m, typeParams)));
  }

  private boolean matchesMethodCriteria(
      ExecutableElement method,
      String name,
      List<TypeDeclaration> parameterTypeDeclarations,
      boolean statik) {
    return (!statik || method.getModifiers().contains(Modifier.STATIC))
        && method.getModifiers().contains(Modifier.PUBLIC)
        && method.getSimpleName().contentEquals(name)
        && method.getReturnType().getKind() != TypeKind.VOID
        && method.getParameters().size() == parameterTypeDeclarations.size()
        && isAssignable(parameterTypeDeclarations, method.getParameters());
  }

  private void removeOverriddenMethodDeclarations(List<MethodDeclaration> candidates) {
    removeHiddenDeclarations(candidates, this::methodOverrides);
  }

  private boolean methodOverrides(MethodDeclaration overrider, MethodDeclaration overridden) {
    var overriderTypeElement =
        ctx.getMoreElements().toTypeElement(overrider.element().getEnclosingElement());
    if (overriderTypeElement == null) {
      return false;
    }
    return ctx.getMoreElements()
        .overrides(overrider.element(), overridden.element(), overriderTypeElement);
  }

  private void removeHiddenMethodDeclarations(List<MethodDeclaration> candidates) {
    removeHiddenDeclarations(candidates, this::methodHides);
  }

  private boolean methodHides(MethodDeclaration hider, MethodDeclaration hidden) {
    var subtype = hider.element().getEnclosingElement().asType();
    var supertype = hidden.element().getEnclosingElement().asType();
    return ctx.getMoreTypes().isAssignableWithErasure(subtype, supertype)
        && ctx.getMoreElements().hides(hider.element(), hidden.element());
  }

  public TypeDeclaration emulateConcatOperation(TypeDeclaration other) {
    Objects.requireNonNull(other);
    assertTrue(isTextType());
    assertTrue(other.isTextType());
    var type = ctx.getMoreTypes().getTypeMirror(String.class);
    return ctx.getDeclarations().newTypeDeclaration(type);
  }

  public TypeDeclaration emulateArithmeticOperation(TypeDeclaration other) {
    Objects.requireNonNull(other);
    assertTrue(isNumberType());
    assertTrue(other.isNumberType());
    var type =
        this.numberPriority.getValue() >= other.numberPriority.getValue() ? this.type : other.type;
    return ctx.getDeclarations().newTypeDeclaration(type);
  }

  public boolean isSameType(TypeDeclaration other) {
    if (ctx.getMoreTypes().isSameTypeWithErasure(this.type, other.type)) {
      return true;
    }
    if (this.isNumberType()) {
      if (other.isNumberType()) {
        return this.numberPriority.getValue() == other.numberPriority.getValue();
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return type.toString();
  }

  private static NumberTypePriority determineNumberPriority(
      TypeElement typeElement, TypeMirror type) {
    return Optional.ofNullable(typeElement)
        .map(TypeElement::getQualifiedName)
        .map(Object::toString)
        .map(NUMBER_PRIORITY_MAP::get)
        .orElseGet(
            () ->
                NUMBER_PRIORITY_MAP.getOrDefault(
                    type.getKind().name().toLowerCase(), NumberTypePriority.DEFAULT));
  }

  private boolean isAssignable(
      List<TypeDeclaration> parameterTypeDeclarations, List<? extends VariableElement> parameters) {
    var types = ctx.getMoreTypes();
    return Zip.stream(parameterTypeDeclarations, parameters)
        .map(p -> p.map(TypeDeclaration::getType, Element::asType))
        .map(p -> p.map(types::boxIfPrimitive, types::boxIfPrimitive))
        .allMatch(p -> types.isAssignableWithErasure(p.fst, p.snd));
  }
}
