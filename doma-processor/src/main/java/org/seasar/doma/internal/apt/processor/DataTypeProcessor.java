package org.seasar.doma.internal.apt.processor;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import org.seasar.doma.DataType;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.meta.domain.DataTypeMeta;
import org.seasar.doma.internal.apt.meta.domain.DataTypeMetaFactory;

@SupportedAnnotationTypes({"org.seasar.doma.DataType"})
@SupportedOptions({
  Options.VERSION_VALIDATION,
  Options.RESOURCES_DIR,
  Options.TEST,
  Options.DEBUG,
  Options.CONFIG_PATH
})
public class DataTypeProcessor extends AbstractDomainProcessor<DataTypeMeta> {

  public DataTypeProcessor() {
    super(DataType.class);
  }

  @Override
  protected DataTypeMetaFactory createTypeElementMetaFactory() {
    return new DataTypeMetaFactory(ctx);
  }
}
