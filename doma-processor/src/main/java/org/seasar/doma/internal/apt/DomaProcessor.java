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

import static org.seasar.doma.internal.apt.AnnotationTypes.AGGREGATE_STRATEGY;
import static org.seasar.doma.internal.apt.AnnotationTypes.DAO;
import static org.seasar.doma.internal.apt.AnnotationTypes.DATA_TYPE;
import static org.seasar.doma.internal.apt.AnnotationTypes.DOMAIN;
import static org.seasar.doma.internal.apt.AnnotationTypes.DOMAIN_CONVERTERS;
import static org.seasar.doma.internal.apt.AnnotationTypes.EMBEDDABLE;
import static org.seasar.doma.internal.apt.AnnotationTypes.ENTITY;
import static org.seasar.doma.internal.apt.AnnotationTypes.EXTERNAL_DOMAIN;
import static org.seasar.doma.internal.apt.AnnotationTypes.SCOPE;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.processor.AggregateStrategyProcessor;
import org.seasar.doma.internal.apt.processor.DaoProcessor;
import org.seasar.doma.internal.apt.processor.DataTypeProcessor;
import org.seasar.doma.internal.apt.processor.DomainConvertersProcessor;
import org.seasar.doma.internal.apt.processor.DomainProcessor;
import org.seasar.doma.internal.apt.processor.ElementProcessor;
import org.seasar.doma.internal.apt.processor.EmbeddableProcessor;
import org.seasar.doma.internal.apt.processor.EntityProcessor;
import org.seasar.doma.internal.apt.processor.ExternalDomainProcessor;
import org.seasar.doma.internal.apt.processor.ScopeProcessor;

@SupportedAnnotationTypes({
  AGGREGATE_STRATEGY,
  DAO,
  DATA_TYPE,
  DOMAIN_CONVERTERS,
  DOMAIN,
  EMBEDDABLE,
  ENTITY,
  EXTERNAL_DOMAIN,
  SCOPE,
})
@SupportedOptions({
  Options.CONFIG_PATH,
  Options.DAO_PACKAGE,
  Options.DAO_SUBPACKAGE,
  Options.DAO_SUFFIX,
  Options.DEBUG,
  Options.DOMAIN_CONVERTERS,
  Options.ENTITY_FIELD_PREFIX,
  Options.EXPR_FUNCTIONS,
  Options.LOMBOK_ALL_ARGS_CONSTRUCTOR,
  Options.LOMBOK_VALUE,
  Options.METAMODEL_ENABLED,
  Options.METAMODEL_PREFIX,
  Options.METAMODEL_SUFFIX,
  Options.RESOURCES_DIR,
  Options.SQL_VALIDATION,
  Options.TEST_INTEGRATION,
  Options.TEST_UNIT,
  Options.TRACE,
  Options.VERSION_VALIDATION,
})
public class DomaProcessor extends AbstractProcessor {

  private static final Map<String, Function<RoundContext, ElementProcessor>> functionMap =
      Map.of(
          EXTERNAL_DOMAIN, ExternalDomainProcessor::new,
          DATA_TYPE, DataTypeProcessor::new,
          DOMAIN, DomainProcessor::new,
          DOMAIN_CONVERTERS, DomainConvertersProcessor::new,
          EMBEDDABLE, EmbeddableProcessor::new,
          ENTITY, EntityProcessor::new,
          AGGREGATE_STRATEGY, AggregateStrategyProcessor::new,
          DAO, DaoProcessor::new,
          SCOPE, ScopeProcessor::new);

  private ProcessingContext processingContext;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    processingContext = new ProcessingContext(processingEnv);
    processingContext.init();
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latest();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (roundEnv.processingOver()) {
      return true;
    }

    var roundContext = processingContext.createRoundContext(roundEnv);

    var externalDomainAnnotation = new HashSet<TypeElement>(1);
    var otherAnnotations = new HashSet<TypeElement>(functionMap.size() - 1);

    for (var annotation : annotations) {
      if (annotation.getQualifiedName().contentEquals(EXTERNAL_DOMAIN)) {
        externalDomainAnnotation.add(annotation);
      } else {
        otherAnnotations.add(annotation);
      }
    }

    // process ExternalDomain annotation first
    processWithRoundContext(externalDomainAnnotation, roundContext);

    // process other annotations
    processWithRoundContext(otherAnnotations, roundContext);

    return true;
  }

  private void processWithRoundContext(Set<TypeElement> annotations, RoundContext roundContext) {
    for (var annotation : annotations) {
      String annotationName = annotation.getQualifiedName().toString();
      var function = functionMap.get(annotationName);
      if (function != null) {
        var elements = roundContext.getElementsAnnotatedWith(annotation);
        if (!elements.isEmpty()) {
          var processor = function.apply(roundContext);
          processor.process(elements);
        }
      }
    }
  }
}
