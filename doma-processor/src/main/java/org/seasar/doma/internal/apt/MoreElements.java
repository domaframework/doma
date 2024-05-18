package org.seasar.doma.internal.apt;

import static java.util.stream.Collectors.toList;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.Parameterizable;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleElementVisitor8;
import org.seasar.doma.ParameterName;
import org.seasar.doma.internal.apt.def.TypeParametersDef;
import org.seasar.doma.internal.util.Pair;
import org.seasar.doma.internal.util.Zip;

public class MoreElements implements Elements {

  private final Context ctx;

  private final Elements elementUtils;

  private final Map<String, TypeElement> typeElementCache = new HashMap<>(64);

  public MoreElements(Context ctx, ProcessingEnvironment env) {
    assertNotNull(ctx, env);
    this.ctx = ctx;
    this.elementUtils = env.getElementUtils();
  }

  // delegate to elementUtils
  @Override
  public PackageElement getPackageElement(CharSequence name) {
    return elementUtils.getPackageElement(name);
  }

  // delegate to elementUtils
  @Override
  public TypeElement getTypeElement(CharSequence canonicalName) {
    assertNotNull(canonicalName);
    return getTypeElementInternal(canonicalName.toString());
  }

  public TypeElement getTypeElement(Class<?> clazz) {
    assertNotNull(clazz);
    return getTypeElementInternal(clazz.getCanonicalName());
  }

  private TypeElement getTypeElementInternal(String canonicalName) {
    return typeElementCache.computeIfAbsent(canonicalName, elementUtils::getTypeElement);
  }

  // delegate to elementUtils
  @Override
  public Map<? extends ExecutableElement, ? extends AnnotationValue> getElementValuesWithDefaults(
      AnnotationMirror a) {
    return elementUtils.getElementValuesWithDefaults(a);
  }

  // delegate to elementUtils
  @Override
  public String getDocComment(Element e) {
    return elementUtils.getDocComment(e);
  }

  // delegate to elementUtils
  @Override
  public boolean isDeprecated(Element e) {
    return elementUtils.isDeprecated(e);
  }

  // delegate to elementUtils
  @Override
  public Name getBinaryName(TypeElement type) {
    return elementUtils.getBinaryName(type);
  }

  // delegate to elementUtils
  @Override
  public PackageElement getPackageOf(Element type) {
    return elementUtils.getPackageOf(type);
  }

  // delegate to elementUtils
  @Override
  public List<? extends Element> getAllMembers(TypeElement type) {
    return elementUtils.getAllMembers(type);
  }

  // delegate to elementUtils
  @Override
  public List<? extends AnnotationMirror> getAllAnnotationMirrors(Element e) {
    return elementUtils.getAllAnnotationMirrors(e);
  }

  // delegate to elementUtils
  @Override
  public boolean hides(Element hider, Element hidden) {
    return elementUtils.hides(hider, hidden);
  }

  // delegate to elementUtils
  @Override
  public boolean overrides(
      ExecutableElement overrider, ExecutableElement overridden, TypeElement type) {
    return elementUtils.overrides(overrider, overridden, type);
  }

  // delegate to elementUtils
  @Override
  public String getConstantExpression(Object value) {
    return elementUtils.getConstantExpression(value);
  }

  // delegate to elementUtils
  @Override
  public void printElements(Writer w, Element... elements) {
    elementUtils.printElements(w, elements);
  }

  // delegate to elementUtils
  @Override
  public Name getName(CharSequence cs) {
    return elementUtils.getName(cs);
  }

  // delegate to elementUtils
  @Override
  public boolean isFunctionalInterface(TypeElement type) {
    return elementUtils.isFunctionalInterface(type);
  }

  public String getParameterName(VariableElement variableElement) {
    assertNotNull(variableElement);
    ParameterName parameterName = variableElement.getAnnotation(ParameterName.class);
    if (parameterName != null && !parameterName.value().isEmpty()) {
      return parameterName.value();
    }
    return variableElement.getSimpleName().toString();
  }

  public TypeElement toTypeElement(Element element) {
    assertNotNull(element);
    return element.accept(
        new SimpleElementVisitor8<TypeElement, Void>() {

          // delegate to elementUtils
          public TypeElement visitType(TypeElement e, Void p) {
            return e;
          }
        },
        null);
  }

  public TypeParameterElement toTypeParameterElement(Element element) {
    assertNotNull(element);
    return element.accept(
        new SimpleElementVisitor8<TypeParameterElement, Void>() {

          @Override
          public TypeParameterElement visitTypeParameter(TypeParameterElement e, Void aVoid) {
            return e;
          }
        },
        null);
  }

  public ExecutableType toExecutableType(Element element) {
    assertNotNull(element);
    return element.accept(
        new SimpleElementVisitor8<ExecutableType, Void>() {

          public ExecutableType visitExecutableType(ExecutableType e, Void p) {
            return e;
          }
        },
        null);
  }

  public TypeElement getTypeElementFromBinaryName(String binaryName) {
    assertNotNull(binaryName);
    String[] parts = binaryName.split("\\$");
    if (parts.length > 1) {
      TypeElement topElement = getTypeElementFromBinaryName(parts[0]);
      if (topElement == null) {
        return null;
      }
      return getEnclosedTypeElement(topElement, Arrays.asList(parts).subList(1, parts.length));
    }
    try {
      return getTypeElement(binaryName);
    } catch (NullPointerException ignored) {
      return null;
    }
  }

  private TypeElement getEnclosedTypeElement(TypeElement typeElement, List<String> enclosedNames) {
    TypeElement enclosing = typeElement;
    for (String enclosedName : enclosedNames) {
      for (TypeElement enclosed : ElementFilter.typesIn(enclosing.getEnclosedElements())) {
        if (enclosed.getSimpleName().contentEquals(enclosedName)) {
          enclosing = enclosed;
          break;
        }
      }
    }
    return typeElement != enclosing ? enclosing : null;
  }

  public AnnotationMirror getAnnotationMirror(
      Element element, Class<? extends Annotation> annotationClass) {
    assertNotNull(element, annotationClass);
    return getAnnotationMirrorInternal(
        element, type -> ctx.getMoreTypes().isSameTypeWithErasure(type, annotationClass));
  }

  public AnnotationMirror getAnnotationMirror(Element element, String annotationClassName) {
    assertNotNull(element, annotationClassName);
    return getAnnotationMirrorInternal(
        element,
        type -> {
          TypeElement typeElement = ctx.getMoreTypes().toTypeElement(type);
          if (typeElement == null) {
            return false;
          }
          return typeElement.getQualifiedName().contentEquals(annotationClassName);
        });
  }

  private AnnotationMirror getAnnotationMirrorInternal(
      Element element, Predicate<DeclaredType> predicate) {
    for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
      DeclaredType annotationType = annotationMirror.getAnnotationType();
      if (predicate.test(annotationType)) {
        return annotationMirror;
      }
    }
    return null;
  }

  public ExecutableElement getNoArgConstructor(TypeElement typeElement) {
    assertNotNull(typeElement);
    for (ExecutableElement constructor :
        ElementFilter.constructorsIn(typeElement.getEnclosedElements())) {
      if (constructor.getParameters().isEmpty()) {
        return constructor;
      }
    }
    return null;
  }

  public Map<String, AnnotationValue> getValuesWithoutDefaults(AnnotationMirror annotationMirror) {
    assertNotNull(annotationMirror);
    Map<String, AnnotationValue> map = new HashMap<>();
    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
        annotationMirror.getElementValues().entrySet()) {
      String key = entry.getKey().getSimpleName().toString();
      AnnotationValue value = entry.getValue();
      map.put(key, value);
    }
    return Collections.unmodifiableMap(map);
  }

  public Map<String, AnnotationValue> getValuesWithDefaults(AnnotationMirror annotationMirror) {
    assertNotNull(annotationMirror);
    Map<String, AnnotationValue> map = new HashMap<>();
    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
        getElementValuesWithDefaults(annotationMirror).entrySet()) {
      String key = entry.getKey().getSimpleName().toString();
      AnnotationValue value = entry.getValue();
      map.put(key, value);
    }
    return Collections.unmodifiableMap(map);
  }

  public TypeParametersDef getTypeParametersDef(Parameterizable element) {
    assertNotNull(element);

    List<? extends TypeParameterElement> typeParameters = element.getTypeParameters();
    List<String> typeParameterNames = getTypeParameterNames(typeParameters);
    Iterator<String> values = typeParameterNames.iterator();

    Iterator<? extends TypeParameterElement> keys = typeParameters.iterator();

    LinkedHashMap<TypeParameterElement, String> map = new LinkedHashMap<>();
    while (keys.hasNext() && values.hasNext()) {
      map.put(keys.next(), values.next());
    }
    return new TypeParametersDef(map);
  }

  List<String> getTypeParameterNames(List<? extends TypeParameterElement> typeParameterElements) {
    assertNotNull(typeParameterElements);
    List<TypeMirror> typeMirrors =
        typeParameterElements.stream().map(TypeParameterElement::asType).collect(toList());
    return ctx.getMoreTypes().getTypeParameterNames(typeMirrors);
  }

  public boolean isVirtualDefaultMethod(TypeElement typeElement, ExecutableElement methodElement) {
    return ElementFilter.typesIn(typeElement.getEnclosedElements()).stream()
        .filter(t -> t.getSimpleName().contentEquals("DefaultImpls"))
        .anyMatch(
            t ->
                ElementFilter.methodsIn(t.getEnclosedElements()).stream()
                    .filter(
                        m -> {
                          EnumSet<Modifier> set = EnumSet.of(Modifier.PUBLIC, Modifier.STATIC);
                          return m.getModifiers().containsAll(set);
                        })
                    .filter(
                        m -> {
                          Name name1 = m.getSimpleName();
                          Name name2 = methodElement.getSimpleName();
                          return name1.contentEquals(name2);
                        })
                    .filter(
                        m -> {
                          TypeMirror type1 = m.getReturnType();
                          TypeMirror type2 = methodElement.getReturnType();
                          return ctx.getMoreTypes().isSameType(type1, type2);
                        })
                    .filter(
                        m -> {
                          int size1 = m.getParameters().size();
                          int size2 = methodElement.getParameters().size() + 1;
                          return size1 == size2;
                        })
                    .filter(
                        m -> {
                          TypeMirror type1 = m.getParameters().iterator().next().asType();
                          TypeMirror type2 = typeElement.asType();
                          return ctx.getMoreTypes().isSameTypeWithErasure(type1, type2);
                        })
                    .anyMatch(
                        m -> {
                          Stream<? extends VariableElement> parameters1 =
                              m.getParameters().stream().skip(1);
                          Stream<? extends VariableElement> parameters2 =
                              methodElement.getParameters().stream();
                          return Zip.stream(parameters1, parameters2)
                              .map(pair -> new Pair<>(pair.fst.asType(), pair.snd.asType()))
                              .allMatch(p -> ctx.getMoreTypes().isSameType(p.fst, p.snd));
                        }));
  }
}
