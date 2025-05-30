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
package org.seasar.doma.internal.apt;

import java.util.Collections;
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

public abstract class TestProcessor extends AbstractProcessor {

  protected ProcessingContext ctx;

  private boolean handled;

  protected TestProcessor() {}

  @Override
  public synchronized void init(ProcessingEnvironment env) {
    super.init(env);
    ctx = ProcessingContext.of(env);
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
  public Set<String> getSupportedOptions() {
    return Set.of(Options.RESOURCES_DIR, Options.TEST_UNIT);
  }

  @Override
  public boolean process(
      final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
    if (roundEnv.processingOver() || handled) {
      return true;
    }
    RoundContext roundContext = ctx.createRoundContext(roundEnv);
    run(roundContext);
    handled = true;
    return false;
  }

  protected abstract void run(RoundContext roundContext);

  protected ExecutableElement createMethodElement(
      RoundContext ctx, Class<?> clazz, String methodName, Class<?>... parameterClasses) {
    TypeElement typeElement = ctx.getMoreElements().getTypeElement(clazz);
    for (TypeElement t = typeElement;
        t != null && t.asType().getKind() != TypeKind.NONE;
        t = ctx.getMoreTypes().toTypeElement(t.getSuperclass())) {
      for (ExecutableElement methodElement : ElementFilter.methodsIn(t.getEnclosedElements())) {
        if (!methodElement.getSimpleName().contentEquals(methodName)) {
          continue;
        }
        List<? extends VariableElement> parameterElements = methodElement.getParameters();
        if (parameterElements.size() != parameterClasses.length) {
          continue;
        }
        int i = 0;
        for (VariableElement parameterElement : parameterElements) {
          TypeMirror parameterType = parameterElement.asType();
          Class<?> parameterClass = parameterClasses[i];
          if (!ctx.getMoreTypes().isSameTypeWithErasure(parameterType, parameterClass)) {
            throw new AssertionError(i);
          }
          i++;
        }
        return methodElement;
      }
    }
    throw new AssertionError(methodName);
  }

  protected LinkedHashMap<String, TypeMirror> createParameterTypeMap(
      ExecutableElement methodElement) {
    LinkedHashMap<String, TypeMirror> result = new LinkedHashMap<>();
    for (VariableElement parameter : methodElement.getParameters()) {
      String name = parameter.getSimpleName().toString();
      TypeMirror type = parameter.asType();
      result.put(name, type);
    }
    return result;
  }
}
