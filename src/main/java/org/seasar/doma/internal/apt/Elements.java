package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Predicate;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.SimpleElementVisitor8;
import org.seasar.doma.ParameterName;

public class Elements implements javax.lang.model.util.Elements {

  private final Context ctx;

  private final javax.lang.model.util.Elements elementUtils;

  public Elements(Context ctx, ProcessingEnvironment env) {
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
  public TypeElement getTypeElement(CharSequence name) {
    return elementUtils.getTypeElement(name);
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

  public String getBinaryNameAsString(TypeElement type) {
    assertNotNull(type);
    Name binaryName = elementUtils.getBinaryName(type);
    return binaryName.toString();
  }

  public String getPackageName(Element element) {
    assertNotNull(element);
    PackageElement packageElement = elementUtils.getPackageOf(element);
    return packageElement.getQualifiedName().toString();
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

  public TypeElement getTypeElement(String binaryName) {
    assertNotNull(binaryName);
    String[] parts = binaryName.split("\\$");
    if (parts.length > 1) {
      TypeElement topElement = getTypeElement(parts[0]);
      if (topElement == null) {
        return null;
      }
      return getEnclosedTypeElement(topElement, Arrays.asList(parts).subList(1, parts.length));
    }
    try {
      return elementUtils.getTypeElement(binaryName);
    } catch (NullPointerException ignored) {
      return null;
    }
  }

  public TypeElement getTypeElement(Class<?> clazz) {
    assertNotNull(clazz);
    return elementUtils.getTypeElement(clazz.getCanonicalName());
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
        element, type -> ctx.getTypes().isSameTypeWithErasure(type, annotationClass));
  }

  public AnnotationMirror getAnnotationMirror(Element element, String annotationClassName) {
    assertNotNull(element, annotationClassName);
    return getAnnotationMirrorInternal(
        element,
        type -> {
          TypeElement typeElement = ctx.getTypes().toTypeElement(type);
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
}
