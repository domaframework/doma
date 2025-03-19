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

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.processor.AggregateStrategyProcessor;
import org.seasar.doma.internal.apt.processor.DaoProcessor;
import org.seasar.doma.internal.apt.processor.DataTypeProcessor;
import org.seasar.doma.internal.apt.processor.DomainConvertersProcessor;
import org.seasar.doma.internal.apt.processor.DomainProcessor;
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
  Options.TEST,
  Options.TRACE,
  Options.VERSION_VALIDATION,
})
public class DomaProcessor extends AbstractProcessor {

  private final List<Operator> operators;
  private ProcessingContext processingContext;

  public DomaProcessor() {
    operators =
        List.of(
            new Operator(EXTERNAL_DOMAIN, DomaProcessor::processExternalDomain),
            new Operator(DATA_TYPE, DomaProcessor::processDataType),
            new Operator(DOMAIN, DomaProcessor::processDomain),
            new Operator(DOMAIN_CONVERTERS, DomaProcessor::processDomainConverters),
            new Operator(EMBEDDABLE, DomaProcessor::processEmbeddable),
            new Operator(ENTITY, DomaProcessor::processEntity),
            new Operator(AGGREGATE_STRATEGY, DomaProcessor::processAggregateStrategy),
            new Operator(DAO, DomaProcessor::processDao),
            new Operator(SCOPE, DomaProcessor::processScope));
  }

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

    var roundContext = processingContext.createRoundContext(annotations, roundEnv);

    for (var operator : operators) {
      var elements = roundContext.getElementsAnnotatedWith(operator.annotationName);
      if (!elements.isEmpty()) {
        operator.consumer.accept(roundContext, elements);
      }
    }

    return true;
  }

  private static void processAggregateStrategy(
      RoundContext roundContext, Set<? extends Element> elements) {
    var processor = new AggregateStrategyProcessor(roundContext);
    processor.process(elements);
  }

  private static void processDao(RoundContext roundContext, Set<? extends Element> elements) {
    var processor = new DaoProcessor(roundContext);
    processor.process(elements);
  }

  private static void processDataType(RoundContext roundContext, Set<? extends Element> elements) {
    var processor = new DataTypeProcessor(roundContext);
    processor.process(elements);
  }

  private static void processDomainConverters(
      RoundContext roundContext, Set<? extends Element> elements) {
    var processor = new DomainConvertersProcessor(roundContext);
    processor.process(elements);
  }

  private static void processDomain(RoundContext roundContext, Set<? extends Element> elements) {
    var processor = new DomainProcessor(roundContext);
    processor.process(elements);
  }

  private static void processEmbeddable(
      RoundContext roundContext, Set<? extends Element> elements) {
    var processor = new EmbeddableProcessor(roundContext);
    processor.process(elements);
  }

  private static void processEntity(RoundContext roundContext, Set<? extends Element> elements) {
    var processor = new EntityProcessor(roundContext);
    processor.process(elements);
  }

  private static void processExternalDomain(
      RoundContext roundContext, Set<? extends Element> elements) {
    var processor = new ExternalDomainProcessor(roundContext);
    processor.process(elements);
  }

  private static void processScope(RoundContext roundContext, Set<? extends Element> elements) {
    var processor = new ScopeProcessor(roundContext);
    processor.process(elements);
  }

  private record Operator(
      String annotationName, BiConsumer<RoundContext, Set<? extends Element>> consumer) {
    Operator {
      Objects.requireNonNull(annotationName);
      Objects.requireNonNull(consumer);
    }
  }
}
