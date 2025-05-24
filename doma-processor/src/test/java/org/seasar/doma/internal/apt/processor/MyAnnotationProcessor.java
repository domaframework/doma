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

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.ProcessingContext;
import org.seasar.doma.internal.apt.meta.NullElementMeta;

public class MyAnnotationProcessor extends AbstractProcessor {

  private final Function<TypeElement, NullElementMeta> handler;
  private ProcessingContext ctx;

  public MyAnnotationProcessor(Function<TypeElement, NullElementMeta> handler) {
    this.handler = Objects.requireNonNull(handler);
  }

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    this.ctx = ProcessingContext.of(processingEnv);
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Set.of(MyAnnotation.class.getName());
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latest();
  }

  @Override
  public Set<String> getSupportedOptions() {
    return Set.of(Options.TEST_UNIT);
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (roundEnv.processingOver()) {
      return true;
    }
    var roundContext = ctx.createRoundContext(roundEnv);
    var elements = roundContext.getElementsAnnotatedWith(annotations.iterator().next());
    var processor = new MyElementProcessor(roundContext, handler);
    processor.process(elements);
    return true;
  }
}
