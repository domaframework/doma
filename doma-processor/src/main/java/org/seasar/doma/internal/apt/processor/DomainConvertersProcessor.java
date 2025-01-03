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
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import org.seasar.doma.DomainConverters;
import org.seasar.doma.ExternalDomain;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.annot.DomainConvertersAnnot;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * @since 1.25.0
 */
@SupportedAnnotationTypes({"org.seasar.doma.DomainConverters"})
@SupportedOptions({
  Options.RESOURCES_DIR,
  Options.TEST,
  Options.TRACE,
  Options.DEBUG,
  Options.CONFIG_PATH
})
public class DomainConvertersProcessor extends AbstractProcessor {

  public DomainConvertersProcessor() {
    super(DomainConverters.class);
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (roundEnv.processingOver()) {
      return true;
    }
    for (TypeElement a : annotations) {
      for (TypeElement typeElement : ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(a))) {
        handleTypeElement(typeElement, this::validate);
      }
    }
    return true;
  }

  protected void validate(TypeElement typeElement) {
    DomainConvertersAnnot convertersMirror =
        ctx.getAnnotations().newDomainConvertersAnnot(typeElement);
    for (TypeMirror convType : convertersMirror.getValueValue()) {
      TypeElement convElement = ctx.getMoreTypes().toTypeElement(convType);
      if (convElement == null) {
        continue;
      }
      if (convElement.getAnnotation(ExternalDomain.class) == null) {
        throw new AptException(
            Message.DOMA4196,
            typeElement,
            convertersMirror.getAnnotationMirror(),
            new Object[] {convElement.getQualifiedName()});
      }
    }
  }
}
