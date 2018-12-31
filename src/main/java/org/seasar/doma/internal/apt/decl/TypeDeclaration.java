package org.seasar.doma.internal.apt.decl;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;

public class TypeDeclaration {

  protected static final Map<String, Integer> NUMBER_PRIORITY_MAP = new HashMap<String, Integer>();

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

  protected TypeElement typeElement;

  protected TypeMirror type;

  protected Map<String, List<TypeParameterDeclaration>> typeParameterDeclarationsMap =
      new HashMap<String, List<TypeParameterDeclaration>>();

  protected Context ctx;

  protected int numberPriority;

  protected TypeDeclaration() {}

  public TypeMirror getType() {
    return type;
  }

  public Context getContext() {
    return ctx;
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
        TypeElement typeElement = ctx.getTypes().toTypeElement(type);
        if (typeElement == null) {
          return false;
        }
        String binaryName = ctx.getElements().getBinaryNameAsString(typeElement);
        return NUMBER_PRIORITY_MAP.containsKey(binaryName);
    }
  }

  public boolean is(Class<?> clazz) {
    return ctx.getTypes().isSameType(type, clazz);
  }

  public int getNumberPriority() {
    return numberPriority;
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

  protected List<ConstructorDeclaration> getCandidateConstructorDeclarations(
      List<TypeDeclaration> parameterTypeDeclarations) {
    List<ConstructorDeclaration> results = new LinkedList<ConstructorDeclaration>();
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
          if (!ctx.getTypes().isAssignable(t1, t2)) {
            continue outer;
          }
        }
        ConstructorDeclaration constructorDeclaration =
            ConstructorDeclaration.newInstance(constructor, typeParameterDeclarations, ctx);
        results.add(constructorDeclaration);
      }
    }
    return results;
  }

  protected ConstructorDeclaration findSuitableConstructorDeclaration(
      List<TypeDeclaration> parameterTypeDeclarations, List<ConstructorDeclaration> candidates) {
    outer:
    for (ConstructorDeclaration constructorDeclaration : candidates) {
      Iterator<TypeDeclaration> typeDeclIterator = parameterTypeDeclarations.iterator();
      Iterator<? extends VariableElement> valueElementIterator =
          constructorDeclaration.getElement().getParameters().iterator();
      while (typeDeclIterator.hasNext() && valueElementIterator.hasNext()) {
        TypeMirror t1 = ctx.getTypes().boxIfPrimitive(typeDeclIterator.next().getType());
        TypeMirror t2 = ctx.getTypes().boxIfPrimitive(valueElementIterator.next().asType());
        if (!ctx.getTypes().isSameType(t1, t2)) {
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

  public FieldDeclaration getFieldDeclarationInternal(String name, boolean statik) {
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

  public List<FieldDeclaration> getCandidateFieldDeclaration(String name, boolean statik) {
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
            FieldDeclaration.newInstance(field, typeParameterDeclarations, ctx);
        results.add(fieldDeclaration);
      }
    }
    return results;
  }

  protected void removeHiddenFieldDeclarations(List<FieldDeclaration> candidates) {
    List<FieldDeclaration> hiders = new LinkedList<FieldDeclaration>(candidates);
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

  protected List<MethodDeclaration> getMethodDeclarationsInternal(
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

  protected List<MethodDeclaration> getCandidateMethodDeclarations(
      String name, List<TypeDeclaration> parameterTypeDeclarations, boolean statik) {
    List<MethodDeclaration> results = new LinkedList<MethodDeclaration>();
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
          if (!ctx.getTypes().isAssignable(t1, t2)) {
            continue outer;
          }
        }
        MethodDeclaration methodDeclaration =
            MethodDeclaration.newInstance(method, typeParameterDeclarations, ctx);
        results.add(methodDeclaration);
      }
    }
    return results;
  }

  protected void removeOverriddenMethodDeclarations(List<MethodDeclaration> candidates) {
    List<MethodDeclaration> overriders = new LinkedList<MethodDeclaration>(candidates);
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

  protected void removeHiddenMethodDeclarations(List<MethodDeclaration> candidates) {
    List<MethodDeclaration> hiders = new LinkedList<MethodDeclaration>(candidates);
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
        if (!ctx.getTypes().isAssignable(t1, t2)) {
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
    return newTypeDeclaration(type, ctx);
  }

  public TypeDeclaration emulateArithmeticOperation(TypeDeclaration other) {
    assertNotNull(other);
    assertTrue(isNumberType());
    assertTrue(other.isNumberType());
    TypeMirror type = this.numberPriority >= other.numberPriority ? this.type : other.type;
    return newTypeDeclaration(type, ctx);
  }

  public boolean isSameType(TypeDeclaration other) {
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

  public static TypeDeclaration newTypeDeclaration(Class<?> clazz, Context ctx) {
    assertNotNull(clazz);
    return newTypeDeclaration(ctx.getTypes().getTypeMirror(clazz), ctx);
  }

  public static TypeDeclaration newTypeDeclaration(TypeMirror type, Context ctx) {
    assertNotNull(type, ctx);
    TypeElement typeElement = ctx.getTypes().toTypeElement(type);
    Map<String, List<TypeParameterDeclaration>> map =
        new LinkedHashMap<String, List<TypeParameterDeclaration>>();
    gatherTypeParameterDeclarations(type, map, ctx);
    TypeDeclaration typeDeclaration = new TypeDeclaration();
    typeDeclaration.type = type;
    typeDeclaration.typeElement = typeElement;
    typeDeclaration.typeParameterDeclarationsMap = map;
    typeDeclaration.ctx = ctx;
    typeDeclaration.numberPriority = determineNumberPriority(typeElement, type, ctx);
    return typeDeclaration;
  }

  protected static int determineNumberPriority(
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

  public static TypeDeclaration newUnknownTypeDeclaration(Context ctx) {
    TypeDeclaration typeDeclaration = new TypeDeclaration();
    typeDeclaration.type = ctx.getTypes().getNoType(TypeKind.NONE);
    typeDeclaration.typeParameterDeclarationsMap = Collections.emptyMap();
    typeDeclaration.ctx = ctx;
    return typeDeclaration;
  }

  public static TypeDeclaration newBooleanTypeDeclaration(Context ctx) {
    assertNotNull(ctx);
    TypeMirror type = ctx.getTypes().getTypeMirror(boolean.class);
    return newTypeDeclaration(type, ctx);
  }

  protected static void gatherTypeParameterDeclarations(
      TypeMirror type,
      Map<String, List<TypeParameterDeclaration>> typeParameterDeclarationsMap,
      Context ctx) {
    TypeElement typeElement = ctx.getTypes().toTypeElement(type);
    if (typeElement == null) {
      return;
    }
    String binaryName = ctx.getElements().getBinaryNameAsString(typeElement);
    typeParameterDeclarationsMap.put(
        binaryName, createTypeParameterDeclarations(typeElement, type, ctx));
    for (TypeMirror superType : ctx.getTypes().directSupertypes(type)) {
      TypeElement superElement = ctx.getTypes().toTypeElement(superType);
      if (superElement == null) {
        continue;
      }
      String superBinaryName = ctx.getElements().getBinaryNameAsString(superElement);
      if (typeParameterDeclarationsMap.containsKey(superBinaryName)) {
        continue;
      }
      typeParameterDeclarationsMap.put(
          superBinaryName, createTypeParameterDeclarations(superElement, superType, ctx));
      gatherTypeParameterDeclarations(superType, typeParameterDeclarationsMap, ctx);
    }
  }

  public static List<TypeParameterDeclaration> createTypeParameterDeclarations(
      TypeElement typeElement, TypeMirror type, Context ctx) {
    assertNotNull(typeElement, type, ctx);
    List<TypeParameterDeclaration> list = new ArrayList<TypeParameterDeclaration>();
    Iterator<? extends TypeParameterElement> formalParams =
        typeElement.getTypeParameters().iterator();
    DeclaredType declaredType = ctx.getTypes().toDeclaredType(type);
    Iterator<? extends TypeMirror> actualParams = declaredType.getTypeArguments().iterator();
    for (; formalParams.hasNext() && actualParams.hasNext(); ) {
      TypeMirror formalType = formalParams.next().asType();
      TypeMirror actualType = actualParams.next();
      TypeParameterDeclaration typeParameterDeclaration =
          TypeParameterDeclaration.newInstance(formalType, actualType, ctx);
      list.add(typeParameterDeclaration);
    }
    return Collections.unmodifiableList(list);
  }
}
