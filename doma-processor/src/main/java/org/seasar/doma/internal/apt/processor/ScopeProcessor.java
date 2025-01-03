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
package org.seasar.doma.internal.apt.processor;

import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import org.seasar.doma.Scope;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.message.Message;

@SupportedAnnotationTypes({"org.seasar.doma.Scope"})
@SupportedOptions({
  Options.RESOURCES_DIR,
  Options.TEST,
  Options.TRACE,
  Options.DEBUG,
  Options.CONFIG_PATH
})
public class ScopeProcessor extends AbstractProcessor {

  public ScopeProcessor() {
    super(Scope.class);
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (roundEnv.processingOver()) {
      return true;
    }
    for (TypeElement a : annotations) {
      for (ExecutableElement element :
          ElementFilter.methodsIn(roundEnv.getElementsAnnotatedWith(a))) {
        handleExecutableElement(element, this::validate);
      }
    }
    return true;
  }

  protected void validate(ExecutableElement method) {
    if (method.getParameters().size() < 1) {
      throw new AptException(Message.DOMA4457, method, new Object[] {});
    }
    Set<Modifier> modifiers = method.getModifiers();
    if (modifiers.contains(Modifier.STATIC)) {
      throw new AptException(Message.DOMA4458, method, new Object[] {});
    }
    if (!modifiers.contains(Modifier.PUBLIC)) {
      throw new AptException(Message.DOMA4459, method, new Object[] {});
    }
  }
}
