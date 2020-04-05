package org.seasar.doma.internal.apt.processor;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import org.seasar.doma.Domain;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.meta.domain.InternalDomainMeta;
import org.seasar.doma.internal.apt.meta.domain.InternalDomainMetaFactory;

@SupportedAnnotationTypes({"org.seasar.doma.Domain"})
@SupportedOptions({
  Options.VERSION_VALIDATION,
  Options.RESOURCES_DIR,
  Options.LOMBOK_VALUE,
  Options.TEST,
  Options.DEBUG,
  Options.CONFIG_PATH
})
public class DomainProcessor extends AbstractDomainProcessor<InternalDomainMeta> {

  public DomainProcessor() {
    super(Domain.class);
  }

  @Override
  protected InternalDomainMetaFactory createTypeElementMetaFactory() {
    return new InternalDomainMetaFactory(ctx);
  }
}
