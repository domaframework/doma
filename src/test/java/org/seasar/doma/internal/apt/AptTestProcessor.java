package org.seasar.doma.internal.apt;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

public abstract class AptTestProcessor extends AbstractProcessor {

  protected Context ctx;

  protected AptTestProcessor() {}

  @Override
  public synchronized void init(ProcessingEnvironment env) {
    super.init(env);
    this.ctx = new Context(env);
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Collections.singleton("*");
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latest();
  }

  @Override
  public boolean process(
      final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
    if (roundEnv.processingOver()) {
      return true;
    }
    run();
    return true;
  }

  protected abstract void run();

  protected ExecutableElement createMethodElement(
      Class<?> clazz, String methodName, Class<?>... parameterClasses) {
    TypeElement typeElement = ctx.getElements().getTypeElement(clazz);
    for (TypeElement t = typeElement;
        t != null && t.asType().getKind() != TypeKind.NONE;
        t = ctx.getTypes().toTypeElement(t.getSuperclass())) {
      for (ExecutableElement methodElement : ElementFilter.methodsIn(t.getEnclosedElements())) {
        if (!methodElement.getSimpleName().contentEquals(methodName)) {
          continue;
        }
        List<? extends VariableElement> parameterElements = methodElement.getParameters();
        if (parameterElements.size() != parameterClasses.length) {
          continue;
        }
        int i = 0;
        for (Iterator<? extends VariableElement> it = parameterElements.iterator();
            it.hasNext(); ) {
          TypeMirror parameterType = it.next().asType();
          Class<?> parameterClass = parameterClasses[i];
          if (!ctx.getTypes().isSameType(parameterType, parameterClass)) {
            return null;
          }
        }
        return methodElement;
      }
    }
    return null;
  }

  protected LinkedHashMap<String, TypeMirror> createParameterTypeMap(
      ExecutableElement methodElement) {
    LinkedHashMap<String, TypeMirror> result = new LinkedHashMap<String, TypeMirror>();
    for (VariableElement parameter : methodElement.getParameters()) {
      String name = parameter.getSimpleName().toString();
      TypeMirror type = parameter.asType();
      result.put(name, type);
    }
    return result;
  }
}
