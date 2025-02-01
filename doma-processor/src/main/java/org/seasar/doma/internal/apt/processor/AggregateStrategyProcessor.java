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
import org.seasar.doma.AggregateStrategy;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.generator.AggregateStrategyTypeGenerator;
import org.seasar.doma.internal.apt.generator.Generator;
import org.seasar.doma.internal.apt.generator.Printer;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.internal.apt.meta.entity.AggregateStrategyMeta;
import org.seasar.doma.internal.apt.meta.entity.AggregateStrategyMetaFactory;

@SupportedAnnotationTypes({"org.seasar.doma.AggregateStrategy"})
@SupportedOptions({
  Options.VERSION_VALIDATION,
  Options.RESOURCES_DIR,
  Options.TEST,
  Options.TRACE,
  Options.DEBUG,
  Options.CONFIG_PATH,
})
public class AggregateStrategyProcessor extends AbstractGeneratingProcessor<AggregateStrategyMeta> {

  public AggregateStrategyProcessor() {
    super(AggregateStrategy.class);
  }

  @Override
  protected TypeElementMetaFactory<AggregateStrategyMeta> createTypeElementMetaFactory() {
    return new AggregateStrategyMetaFactory(ctx);
  }

  @Override
  protected ClassName createClassName(TypeElement typeElement, AggregateStrategyMeta meta) {
    assertNotNull(typeElement, meta);
    Name binaryName = ctx.getMoreElements().getBinaryName(typeElement);
    return ClassNames.newAggregateStrategyTypeClassName(binaryName);
  }

  @Override
  protected Generator createGenerator(
      ClassName className, Printer printer, AggregateStrategyMeta meta) {
    assertNotNull(className, meta, printer);
    return new AggregateStrategyTypeGenerator(ctx, className, printer, meta);
  }
}
