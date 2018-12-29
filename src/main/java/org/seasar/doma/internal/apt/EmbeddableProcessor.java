package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.IOException;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.Embeddable;
import org.seasar.doma.internal.apt.meta.EmbeddableMeta;
import org.seasar.doma.internal.apt.meta.EmbeddableMetaFactory;
import org.seasar.doma.internal.apt.meta.EmbeddablePropertyMetaFactory;

/** @author nakamura-to */
@SupportedAnnotationTypes({"org.seasar.doma.Embeddable"})
@SupportedOptions({
  Options.VERSION_VALIDATION,
  Options.RESOURCES_DIR,
  Options.LOMBOK_VALUE,
  Options.LOMBOK_ALL_ARGS_CONSTRUCTOR,
  Options.TEST,
  Options.DEBUG
})
public class EmbeddableProcessor extends AbstractGeneratingProcessor<EmbeddableMeta> {

  public EmbeddableProcessor() {
    super(Embeddable.class);
  }

  @Override
  protected EmbeddableMetaFactory createTypeElementMetaFactory() {
    EmbeddablePropertyMetaFactory propertyMetaFactory = createEmbeddablePropertyMetaFactory();
    return new EmbeddableMetaFactory(processingEnv, propertyMetaFactory);
  }

  protected EmbeddablePropertyMetaFactory createEmbeddablePropertyMetaFactory() {
    return new EmbeddablePropertyMetaFactory(processingEnv);
  }

  @Override
  protected Generator createGenerator(TypeElement typeElement, EmbeddableMeta meta)
      throws IOException {
    assertNotNull(typeElement, meta);
    return new EmbeddableTypeGenerator(processingEnv, typeElement, meta);
  }
}
