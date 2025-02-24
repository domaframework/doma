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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.generator.DomainTypeGenerator;
import org.seasar.doma.internal.apt.generator.Generator;
import org.seasar.doma.internal.apt.generator.JavaFileGenerator;
import org.seasar.doma.internal.apt.generator.Printer;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.internal.apt.meta.domain.DomainMeta;

class DomainProcessorSupport<M extends DomainMeta> {

  private final RoundContext ctx;
  private final ElementProcessorSupport<M> support;
  private final TypeElementMetaFactory<M> factory;

  DomainProcessorSupport(
      RoundContext ctx,
      Class<? extends Annotation> supportedAnnotationType,
      TypeElementMetaFactory<M> factory) {
    this.ctx = Objects.requireNonNull(ctx);
    this.support = new ElementProcessorSupport<>(ctx, supportedAnnotationType);
    this.factory = Objects.requireNonNull(factory);
  }

  List<M> process(Set<? extends Element> elements) {
    return support.processTypeElements(elements, this::processEach);
  }

  private M processEach(TypeElement typeElement) {
    var meta = factory.createTypeElementMeta(typeElement);
    if (!meta.isError()) {
      generate(typeElement, meta);
    }
    return meta;
  }

  private void generate(TypeElement typeElement, M meta) {
    var javaFileGenerator =
        new JavaFileGenerator<>(ctx, this::createClassName, this::createGenerator);
    javaFileGenerator.generate(typeElement, meta);
  }

  private ClassName createClassName(TypeElement typeElement, DomainMeta meta) {
    assertNotNull(typeElement, meta);
    var binaryName = ctx.getMoreElements().getBinaryName(typeElement);
    return ClassNames.newDomainTypeClassName(binaryName);
  }

  private Generator createGenerator(ClassName className, Printer printer, DomainMeta meta) {
    assertNotNull(className, meta, printer);
    return new DomainTypeGenerator(ctx, className, printer, meta);
  }
}
