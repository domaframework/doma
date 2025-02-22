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

import java.util.ArrayList;
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

  private final List<Operator> operators = new ArrayList<>();
  private ProcessingContext processingContext;

  public DomaProcessor() {
    operators.add(new Operator(EXTERNAL_DOMAIN, DomaProcessor::processExternalDomainElements));
    operators.add(new Operator(DATA_TYPE, DomaProcessor::processDataTypeElements));
    operators.add(new Operator(DOMAIN, DomaProcessor::processDomainElements));
    operators.add(new Operator(DOMAIN_CONVERTERS, DomaProcessor::processDomainConvertersElements));
    operators.add(new Operator(EMBEDDABLE, DomaProcessor::processEmbeddableElements));
    operators.add(new Operator(ENTITY, DomaProcessor::processEntityElements));
    operators.add(
        new Operator(AGGREGATE_STRATEGY, DomaProcessor::processAggregateStrategyElements));
    operators.add(new Operator(DAO, DomaProcessor::processDaoElements));
    operators.add(new Operator(SCOPE, DomaProcessor::processScopeElements));
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

    var roundContext = new RoundContext(processingContext, roundEnv, annotations);

    for (var operator : operators) {
      var elements = roundContext.getElementsAnnotatedWith(operator.name);
      if (!elements.isEmpty()) {
        operator.consumer.accept(roundContext, elements);
      }
    }

    return true;
  }

  private static void processAggregateStrategyElements(
      RoundContext roundContext, Set<? extends Element> elements) {
    var processor = new AggregateStrategyProcessor(roundContext);
    processor.process(elements);
  }

  private static void processDaoElements(
      RoundContext roundContext, Set<? extends Element> elements) {
    var processor = new DaoProcessor(roundContext);
    processor.process(elements);
  }

  private static void processDataTypeElements(
      RoundContext roundContext, Set<? extends Element> elements) {
    var processor = new DataTypeProcessor(roundContext);
    processor.process(elements);
  }

  private static void processDomainConvertersElements(
      RoundContext roundContext, Set<? extends Element> elements) {
    var processor = new DomainConvertersProcessor(roundContext);
    processor.process(elements);
  }

  private static void processDomainElements(
      RoundContext roundContext, Set<? extends Element> elements) {
    var processor = new DomainProcessor(roundContext);
    processor.process(elements);
  }

  private static void processEmbeddableElements(
      RoundContext roundContext, Set<? extends Element> elements) {
    var processor = new EmbeddableProcessor(roundContext);
    processor.process(elements);
  }

  private static void processEntityElements(
      RoundContext roundContext, Set<? extends Element> elements) {
    var processor = new EntityProcessor(roundContext);
    processor.process(elements);
  }

  private static void processExternalDomainElements(
      RoundContext roundContext, Set<? extends Element> elements) {
    var processor = new ExternalDomainProcessor(roundContext);
    var metaList = processor.process(elements);
    roundContext.getExternalDomainMetaList().addAll(metaList);
  }

  private static void processScopeElements(
      RoundContext roundContext, Set<? extends Element> elements) {
    var processor = new ScopeProcessor(roundContext);
    processor.process(elements);
  }

  private record Operator(String name, BiConsumer<RoundContext, Set<? extends Element>> consumer) {
    Operator {
      Objects.requireNonNull(name);
      Objects.requireNonNull(consumer);
    }
  }
}
