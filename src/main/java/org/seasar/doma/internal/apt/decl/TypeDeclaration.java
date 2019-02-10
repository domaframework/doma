package org.seasar.doma.internal.apt.decl;

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
import java.util.Optional;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;

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

  private final TypeElement typeElement;

  private final Map<String, List<TypeParameterDeclaration>> typeParameterDeclarationsMap;

  private final int numberPriority;

  protected TypeDeclaration(
      Context ctx,
      TypeMirror type,
      TypeElement typeElement,
      Map<String, List<TypeParameterDeclaration>> typeParameterDeclarationsMap) {
    assertNotNull(ctx, type, typeParameterDeclarationsMap);
    this.ctx = ctx;
    this.type = type;
    this.typeElement = typeElement; // nullable
    this.typeParameterDeclarationsMap = typeParameterDeclarationsMap;
    this.numberPriority = determineNumberPriority(typeElement, type, ctx);
  }

  public TypeMirror getType() {
    return type;
  }

  public String getBinaryName() {
    if (typeElement == null) {
      return type.toString();
    }
    return ctx.getElements().getBinaryNameAsString(typeElement);
  }

  public boolean isUnknownType() {
    return type.getKind() == TypeKind.NONE;
  }

  public boolean isNullType() {
    return type.getKind() == TypeKind.NULL;
  }

  public boolean isBooleanType() {
    return type.getKind() == TypeKind.BOOLEAN
        || ctx.getTypes().isSameTypeWithErasure(type, Boolean.class);
  }

  public boolean isTextType() {
    return type.getKind() == TypeKind.CHAR
        || ctx.getTypes().isSameTypeWithErasure(type, String.class)
        || ctx.getTypes().isSameTypeWithErasure(type, Character.class);
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
        TypeElement typeElement = ctx.getTypes().toTypeElement(type);
        if (typeElement == null) {
          return false;
        }
        String binaryName = ctx.getElements().getBinaryNameAsString(typeElement);
        return NUMBER_PRIORITY_MAP.containsKey(binaryName);
    }
  }

  public boolean is(Class<?> clazz) {
    return ctx.getTypes().isSameTypeWithErasure(type, clazz);
  }

  public List<TypeParameterDeclaration> getTypeParameterDeclarations() {
    Optional<List<TypeParameterDeclaration>> typeParameterDeclarations =
        typeParameterDeclarationsMap.values().stream().findFirst();
    return typeParameterDeclarations.orElse(Collections.emptyList());
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
    List<ConstructorDeclaration> results = new LinkedList<>();
    for (Map.Entry<String, List<TypeParameterDeclaration>> e :
        typeParameterDeclarationsMap.entrySet()) {
      String typeQualifiedName = e.getKey();
      List<TypeParameterDeclaration> typeParameterDeclarations = e.getValue();
      TypeElement typeElement = ctx.getElements().getTypeElement(typeQualifiedName);

      outer:
      for (ExecutableElement constructor :
          ElementFilter.constructorsIn(typeElement.getEnclosedElements())) {
        if (!constructor.getModifiers().contains(Modifier.PUBLIC)) {
          continue;
        }
        List<? extends VariableElement> parameters = constructor.getParameters();
        if (parameters.size() != parameterTypeDeclarations.size()) {
          continue;
        }
        Iterator<TypeDeclaration> typeDeclIterator = parameterTypeDeclarations.iterator();
        Iterator<? extends VariableElement> valueElementIterator = parameters.iterator();
        while (typeDeclIterator.hasNext() && valueElementIterator.hasNext()) {
          TypeMirror t1 = ctx.getTypes().boxIfPrimitive(typeDeclIterator.next().getType());
          TypeMirror t2 = ctx.getTypes().boxIfPrimitive(valueElementIterator.next().asType());
          if (!ctx.getTypes().isAssignableWithErasure(t1, t2)) {
            continue outer;
          }
        }
        ConstructorDeclaration constructorDeclaration =
            ctx.getDeclarations().newConstructorDeclaration(constructor, typeParameterDeclarations);
        results.add(constructorDeclaration);
      }
    }
    return results;
  }

  private ConstructorDeclaration findSuitableConstructorDeclaration(
      List<TypeDeclaration> parameterTypeDeclarations, List<ConstructorDeclaration> candidates) {
    outer:
    for (ConstructorDeclaration constructorDeclaration : candidates) {
      Iterator<TypeDeclaration> typeDeclIterator = parameterTypeDeclarations.iterator();
      Iterator<? extends VariableElement> valueElementIterator =
          constructorDeclaration.getElement().getParameters().iterator();
      while (typeDeclIterator.hasNext() && valueElementIterator.hasNext()) {
        TypeMirror t1 = ctx.getTypes().boxIfPrimitive(typeDeclIterator.next().getType());
        TypeMirror t2 = ctx.getTypes().boxIfPrimitive(valueElementIterator.next().asType());
        if (!ctx.getTypes().isSameTypeWithErasure(t1, t2)) {
          continue outer;
        }
      }
      return constructorDeclaration;
    }
    return null;
  }

  public FieldDeclaration getFieldDeclaration(String name) {
    return getFieldDeclarationInternal(name, false);
  }

  public FieldDeclaration getStaticFieldDeclaration(String name) {
    return getFieldDeclarationInternal(name, true);
  }

  private FieldDeclaration getFieldDeclarationInternal(String name, boolean statik) {
    List<FieldDeclaration> candidates = getCandidateFieldDeclaration(name, statik);
    removeHiddenFieldDeclarations(candidates);

    if (candidates.size() == 0) {
      return null;
    }
    if (candidates.size() == 1) {
      return candidates.get(0);
    }
    throw new AptIllegalStateException(name);
  }

  private List<FieldDeclaration> getCandidateFieldDeclaration(String name, boolean statik) {
    List<FieldDeclaration> results = new LinkedList<FieldDeclaration>();
    for (Map.Entry<String, List<TypeParameterDeclaration>> e :
        typeParameterDeclarationsMap.entrySet()) {
      String typeQualifiedName = e.getKey();
      List<TypeParameterDeclaration> typeParameterDeclarations = e.getValue();
      TypeElement typeElement = ctx.getElements().getTypeElement(typeQualifiedName);
      for (VariableElement field : ElementFilter.fieldsIn(typeElement.getEnclosedElements())) {
        if (statik && !field.getModifiers().contains(Modifier.STATIC)) {
          continue;
        }
        if (!field.getSimpleName().contentEquals(name)) {
          continue;
        }
        FieldDeclaration fieldDeclaration =
            ctx.getDeclarations().newFieldDeclaration(field, typeParameterDeclarations);
        results.add(fieldDeclaration);
      }
    }
    return results;
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
      String name, List<TypeDeclaration> parameterTypeDeclarations, boolean statik) {
    List<MethodDeclaration> candidates =
        getCandidateMethodDeclarations(name, parameterTypeDeclarations, statik);
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
      String name, List<TypeDeclaration> parameterTypeDeclarations, boolean statik) {
    List<MethodDeclaration> results = new LinkedList<>();
    for (Map.Entry<String, List<TypeParameterDeclaration>> e :
        typeParameterDeclarationsMap.entrySet()) {
      String binaryName = e.getKey();
      List<TypeParameterDeclaration> typeParameterDeclarations = e.getValue();
      TypeElement typeElement = ctx.getElements().getTypeElement(binaryName);
      if (typeElement == null) {
        continue;
      }

      outer:
      for (ExecutableElement method : ElementFilter.methodsIn(typeElement.getEnclosedElements())) {
        if (statik && !method.getModifiers().contains(Modifier.STATIC)) {
          continue;
        }
        if (!method.getModifiers().contains(Modifier.PUBLIC)) {
          continue;
        }
        if (!method.getSimpleName().contentEquals(name)) {
          continue;
        }
        if (method.getReturnType().getKind() == TypeKind.VOID) {
          continue;
        }
        List<? extends VariableElement> parameters = method.getParameters();
        if (method.getParameters().size() != parameterTypeDeclarations.size()) {
          continue;
        }
        Iterator<TypeDeclaration> typeDeclIterator = parameterTypeDeclarations.iterator();
        Iterator<? extends VariableElement> valueElementIterator = parameters.iterator();
        while (typeDeclIterator.hasNext() && valueElementIterator.hasNext()) {
          TypeMirror t1 = ctx.getTypes().boxIfPrimitive(typeDeclIterator.next().getType());
          TypeMirror t2 = ctx.getTypes().boxIfPrimitive(valueElementIterator.next().asType());
          if (!ctx.getTypes().isAssignableWithErasure(t1, t2)) {
            continue outer;
          }
        }
        MethodDeclaration methodDeclaration =
            ctx.getDeclarations().newMethodDeclaration(method, typeParameterDeclarations);
        results.add(methodDeclaration);
      }
    }
    return results;
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
        if (ctx.getTypes().isAssignableWithErasure(subtype, supertype)) {
          if (ctx.getElements().hides(hider.getElement(), hidden.getElement())) {
            it.remove();
            break;
          }
        }
      }
    }
  }

  protected MethodDeclaration findSuitableMethodDeclaration(
      List<TypeDeclaration> parameterTypeDeclarations, List<MethodDeclaration> candidates) {
    outer:
    for (MethodDeclaration methodDeclaration : candidates) {
      Iterator<TypeDeclaration> typeDeclIterator = parameterTypeDeclarations.iterator();
      Iterator<? extends VariableElement> valueElementIterator =
          methodDeclaration.getElement().getParameters().iterator();
      while (typeDeclIterator.hasNext() && valueElementIterator.hasNext()) {
        TypeMirror t1 = ctx.getTypes().boxIfPrimitive(typeDeclIterator.next().getType());
        TypeMirror t2 = ctx.getTypes().boxIfPrimitive(valueElementIterator.next().asType());
        if (!ctx.getTypes().isAssignableWithErasure(t1, t2)) {
          continue outer;
        }
      }
      return methodDeclaration;
    }
    return null;
  }

  public TypeDeclaration emulateConcatOperation(TypeDeclaration other) {
    assertNotNull(other);
    assertTrue(isTextType());
    assertTrue(other.isTextType());
    TypeMirror type = ctx.getTypes().getTypeMirror(String.class);
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
    if (ctx.getTypes().isSameTypeWithErasure(this.type, other.type)) {
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

  private static int determineNumberPriority(
      TypeElement typeElement, TypeMirror type, Context ctx) {
    if (typeElement != null) {
      String binaryName = ctx.getElements().getBinaryNameAsString(typeElement);
      Integer result = NUMBER_PRIORITY_MAP.get(binaryName);
      if (result != null) {
        return result.intValue();
      }
    }
    Integer result = NUMBER_PRIORITY_MAP.get(type.getKind().name().toLowerCase());
    if (result != null) {
      return result.intValue();
    }
    return 0;
  }
}
