package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.SimpleElementVisitor8;
import javax.lang.model.util.SimpleTypeVisitor8;
import org.seasar.doma.ParameterName;

public class Elements {

  private final Context ctx;

  private final ProcessingEnvironment env;

  private final javax.lang.model.util.Elements elementUtils;

  public Elements(Context ctx) {
    assertNotNull(ctx);
    this.ctx = ctx;
    this.env = ctx.getEnv();
    this.elementUtils = env.getElementUtils();
  }

  // delegate to elementUtils
  public PackageElement getPackageElement(CharSequence name) {
    return elementUtils.getPackageElement(name);
  }

  // delegate to elementUtils
  public TypeElement getTypeElement(CharSequence name) {
    return elementUtils.getTypeElement(name);
  }

  // delegate to elementUtils
  public Map<? extends ExecutableElement, ? extends AnnotationValue> getElementValuesWithDefaults(
      AnnotationMirror a) {
    return elementUtils.getElementValuesWithDefaults(a);
  }

  // delegate to elementUtils
  public String getDocComment(Element e) {
    return elementUtils.getDocComment(e);
  }

  // delegate to elementUtils
  public boolean isDeprecated(Element e) {
    return elementUtils.isDeprecated(e);
  }

  // delegate to elementUtils
  public Name getBinaryName(TypeElement type) {
    return elementUtils.getBinaryName(type);
  }

  // delegate to elementUtils
  public PackageElement getPackageOf(Element type) {
    return elementUtils.getPackageOf(type);
  }

  // delegate to elementUtils
  public List<? extends Element> getAllMembers(TypeElement type) {
    return elementUtils.getAllMembers(type);
  }

  // delegate to elementUtils
  public List<? extends AnnotationMirror> getAllAnnotationMirrors(Element e) {
    return elementUtils.getAllAnnotationMirrors(e);
  }

  // delegate to elementUtils
  public boolean hides(Element hider, Element hidden) {
    return elementUtils.hides(hider, hidden);
  }

  // delegate to elementUtils
  public boolean overrides(
      ExecutableElement overrider, ExecutableElement overridden, TypeElement type) {
    return elementUtils.overrides(overrider, overridden, type);
  }

  // delegate to elementUtils
  public String getConstantExpression(Object value) {
    return elementUtils.getConstantExpression(value);
  }

  // delegate to elementUtils
  public void printElements(Writer w, Element... elements) {
    elementUtils.printElements(w, elements);
  }

  // delegate to elementUtils
  public Name getName(CharSequence cs) {
    return elementUtils.getName(cs);
  }

  // delegate to elementUtils
  public boolean isFunctionalInterface(TypeElement type) {
    return elementUtils.isFunctionalInterface(type);
  }

  public String getBinaryNameAsString(TypeElement type) {
    Name binaryName = elementUtils.getBinaryName(type);
    return binaryName.toString();
  }

  public String getPackageName(Element element) {
    PackageElement packageElement = ctx.getEnv().getElementUtils().getPackageOf(element);
    return packageElement.getQualifiedName().toString();
  }

  public String getPackageExcludedBinaryName(TypeElement typeElement) {
    String binaryName = ctx.getEnv().getElementUtils().getBinaryName(typeElement).toString();
    int pos = binaryName.lastIndexOf('.');
    if (pos < 0) {
      return binaryName;
    }
    return binaryName.substring(pos + 1);
  }

  public String getParameterName(VariableElement variableElement) {
    assertNotNull(variableElement);
    ParameterName parameterName = variableElement.getAnnotation(ParameterName.class);
    if (parameterName != null && !parameterName.value().isEmpty()) {
      return parameterName.value();
    }
    return variableElement.getSimpleName().toString();
  }

  public boolean isEnclosing(Element enclosingElement, Element enclosedElement) {
    assertNotNull(enclosingElement, enclosedElement);
    if (enclosingElement.equals(enclosedElement)) {
      return true;
    }
    for (Element e = enclosedElement; e != null; e = e.getEnclosingElement()) {
      if (enclosingElement.equals(e)) {
        return true;
      }
    }
    return false;
  }

  public ExecutableType toExecutableType(ExecutableElement element) {
    assertNotNull(element);
    return element
        .asType()
        .accept(
            new SimpleTypeVisitor8<ExecutableType, Void>() {

              // delegate to elementUtils
              public ExecutableType visitExecutable(ExecutableType t, Void p) {
                return t;
              }
            },
            null);
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
    // Class<?> clazz = null;
    // try {
    // clazz = Class.forName(className);
    // return getTypeElement(clazz, env);
    // } catch (ClassNotFoundException ignored) {
    // }
    try {
      return elementUtils.getTypeElement(binaryName);
    } catch (NullPointerException ignored) {
      return null;
    }
  }

  public TypeElement getTypeElement(Class<?> clazz) {
    assertNotNull(clazz);
    return ctx.getEnv().getElementUtils().getTypeElement(clazz.getCanonicalName());
  }

  public TypeElement getEnclosedTypeElement(TypeElement typeElement, List<String> enclosedNames) {
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
    return getAnnotationMirrorInternal(
        element, type -> ctx.getTypes().isSameType(type, annotationClass));
  }

  public AnnotationMirror getAnnotationMirror(Element element, String annotationClassName) {
    return getAnnotationMirrorInternal(
        element, type -> ctx.getTypes().getClassName(type).equals(annotationClassName));
  }

  protected AnnotationMirror getAnnotationMirrorInternal(
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
    for (ExecutableElement constructor :
        ElementFilter.constructorsIn(typeElement.getEnclosedElements())) {
      if (constructor.getParameters().isEmpty()) {
        return constructor;
      }
    }
    return null;
  }
}
