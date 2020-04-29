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
