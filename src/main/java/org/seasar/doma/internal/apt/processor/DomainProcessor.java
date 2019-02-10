package org.seasar.doma.internal.apt.processor;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.Domain;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.generator.DomainDescGenerator;
import org.seasar.doma.internal.apt.generator.Generator;
import org.seasar.doma.internal.apt.generator.Printer;
import org.seasar.doma.internal.apt.meta.domain.DomainMeta;
import org.seasar.doma.internal.apt.meta.domain.DomainMetaFactory;

@SupportedAnnotationTypes({"org.seasar.doma.Domain"})
@SupportedOptions({
  Options.VERSION_VALIDATION,
  Options.RESOURCES_DIR,
  Options.LOMBOK_VALUE,
  Options.TEST,
  Options.DEBUG
})
public class DomainProcessor extends AbstractGeneratingProcessor<DomainMeta> {

  public DomainProcessor() {
    super(Domain.class);
  }

  @Override
  protected DomainMetaFactory createTypeElementMetaFactory() {
    return new DomainMetaFactory(ctx);
  }

  @Override
  protected ClassName createClassName(TypeElement typeElement, DomainMeta meta) {
    assertNotNull(typeElement, meta);
    Name binaryName = ctx.getElements().getBinaryName(typeElement);
    return ClassNames.newDomainDescClassName(binaryName);
  }

  @Override
  protected Generator createGenerator(ClassName className, Printer printer, DomainMeta meta) {
    assertNotNull(className, meta, printer);
    return new DomainDescGenerator(ctx, className, printer, meta);
  }
}
