package org.seasar.doma.internal.apt.processor;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.Domain;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.TypeName;
import org.seasar.doma.internal.apt.generator.DomainTypeGenerator;
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
  protected TypeName createName(TypeElement typeElement, DomainMeta meta) {
    assertNotNull(typeElement, meta);
    return ctx.getTypeNames().newDomainDescTypeName(typeElement);
  }

  @Override
  protected Generator createGenerator(TypeName typeName, Printer printer, DomainMeta meta) {
    assertNotNull(typeName, meta, printer);
    return new DomainTypeGenerator(ctx, typeName, printer, meta);
  }
}
