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

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.ExternalDomain;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.generator.ExternalDomainTypeGenerator;
import org.seasar.doma.internal.apt.generator.Generator;
import org.seasar.doma.internal.apt.generator.Printer;
import org.seasar.doma.internal.apt.meta.domain.ExternalDomainMeta;
import org.seasar.doma.internal.apt.meta.domain.ExternalDomainMetaFactory;

@SupportedAnnotationTypes({"org.seasar.doma.ExternalDomain"})
@SupportedOptions({
  Options.VERSION_VALIDATION,
  Options.RESOURCES_DIR,
  Options.TEST,
  Options.TRACE,
  Options.DEBUG,
  Options.CONFIG_PATH
})
public class ExternalDomainProcessor extends AbstractGeneratingProcessor<ExternalDomainMeta> {

  public ExternalDomainProcessor() {
    super(ExternalDomain.class);
  }

  @Override
  protected ExternalDomainMetaFactory createTypeElementMetaFactory() {
    return new ExternalDomainMetaFactory(ctx);
  }

  @Override
  protected ClassName createClassName(TypeElement typeElement, ExternalDomainMeta meta) {
    assertNotNull(typeElement, meta);
    Name name = ctx.getNames().createExternalDomainName(meta.asType());
    return ClassNames.newExternalDomainTypeClassName(name);
  }

  @Override
  protected Generator createGenerator(
      ClassName className, Printer printer, ExternalDomainMeta meta) {
    assertNotNull(className, meta, printer);
    return new ExternalDomainTypeGenerator(ctx, className, printer, meta);
  }
}
