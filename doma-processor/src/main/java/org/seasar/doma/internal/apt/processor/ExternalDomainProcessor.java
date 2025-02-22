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

import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.ExternalDomain;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.generator.ExternalDomainTypeGenerator;
import org.seasar.doma.internal.apt.generator.Generator;
import org.seasar.doma.internal.apt.generator.JavaFileGenerator;
import org.seasar.doma.internal.apt.generator.Printer;
import org.seasar.doma.internal.apt.meta.domain.ExternalDomainMeta;
import org.seasar.doma.internal.apt.meta.domain.ExternalDomainMetaFactory;

public class ExternalDomainProcessor implements ElementProcessor<ExternalDomainMeta> {

  private final RoundContext ctx;
  private final ElementProcessorSupport<ExternalDomainMeta> support;
  private final ExternalDomainMetaFactory factory;

  public ExternalDomainProcessor(RoundContext ctx) {
    this.ctx = Objects.requireNonNull(ctx);
    this.support = new ElementProcessorSupport<>(ctx, ExternalDomain.class);
    this.factory = new ExternalDomainMetaFactory(ctx);
  }

  @Override
  public List<ExternalDomainMeta> process(Set<? extends Element> elements) {
    return support.processTypeElements(elements, this::processEach);
  }

  private ExternalDomainMeta processEach(TypeElement typeElement) {
    var meta = factory.createTypeElementMeta(typeElement);
    if (!meta.isError()) {
      generate(typeElement, meta);
    }
    return meta;
  }

  private void generate(TypeElement typeElement, ExternalDomainMeta meta) {
    var javaFileGenerator =
        new JavaFileGenerator<>(ctx, this::createClassName, this::createGenerator);
    javaFileGenerator.generate(typeElement, meta);
  }

  private ClassName createClassName(TypeElement typeElement, ExternalDomainMeta meta) {
    assertNotNull(typeElement, meta);
    var name = ctx.getNames().createExternalDomainName(meta.asType());
    return ClassNames.newExternalDomainTypeClassName(name);
  }

  private Generator createGenerator(ClassName className, Printer printer, ExternalDomainMeta meta) {
    assertNotNull(className, meta, printer);
    return new ExternalDomainTypeGenerator(ctx, className, printer, meta);
  }
}
