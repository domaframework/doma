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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.ExternalDomain;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.generator.ExternalDomainTypeGenerator;
import org.seasar.doma.internal.apt.generator.Generator;
import org.seasar.doma.internal.apt.generator.JavaFileGenerator;
import org.seasar.doma.internal.apt.generator.Printer;
import org.seasar.doma.internal.apt.meta.domain.ExternalDomainMeta;
import org.seasar.doma.internal.apt.meta.domain.ExternalDomainMetaFactory;
import org.seasar.doma.message.Message;

public class ExternalDomainProcessor implements ElementProcessor {

  private final RoundContext ctx;
  private final ElementProcessorSupport<ExternalDomainMeta> support;
  private final ExternalDomainMetaFactory factory;
  private final Map<String, TypeElement> processed = new HashMap<>();

  public ExternalDomainProcessor(RoundContext ctx) {
    this.ctx = Objects.requireNonNull(ctx);
    this.support = new ElementProcessorSupport<>(ctx, ExternalDomain.class);
    this.factory = new ExternalDomainMetaFactory(ctx);
  }

  @Override
  public void process(Set<? extends Element> elements) {
    var metaList = support.processTypeElements(elements, this::processEach);
    ctx.getExternalDomainMetaList().addAll(metaList);
  }

  private ExternalDomainMeta processEach(TypeElement typeElement) {
    var meta = factory.createTypeElementMeta(typeElement);
    var key = meta.asType().toString();
    var anotherTypeElement = processed.get(key);
    if (anotherTypeElement != null) {
      throw new AptException(
          Message.DOMA4490, typeElement, new Object[] {key, typeElement, anotherTypeElement});
    }
    if (!meta.isError()) {
      generate(typeElement, meta);
    }
    processed.put(key, typeElement);
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
