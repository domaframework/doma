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
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.apt.generator.DomainTypeGenerator;
import org.seasar.doma.internal.apt.generator.Generator;
import org.seasar.doma.internal.apt.generator.Printer;
import org.seasar.doma.internal.apt.meta.domain.DomainMeta;

public abstract class AbstractDomainProcessor<M extends DomainMeta>
    extends AbstractGeneratingProcessor<M> {

  public AbstractDomainProcessor(Class<? extends Annotation> supportedAnnotationType) {
    super(supportedAnnotationType);
  }

  @Override
  protected ClassName createClassName(TypeElement typeElement, DomainMeta meta) {
    assertNotNull(typeElement, meta);
    Name binaryName = ctx.getMoreElements().getBinaryName(typeElement);
    return ClassNames.newDomainTypeClassName(binaryName);
  }

  @Override
  protected Generator createGenerator(ClassName className, Printer printer, DomainMeta meta) {
    assertNotNull(className, meta, printer);
    return new DomainTypeGenerator(ctx, className, printer, meta);
  }
}
