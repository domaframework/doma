package org.seasar.doma.internal.apt.processor;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.Embeddable;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.generator.EmbeddableTypeGenerator;
import org.seasar.doma.internal.apt.generator.Generator;
import org.seasar.doma.internal.apt.generator.Printer;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.internal.apt.meta.entity.EmbeddableMeta;
import org.seasar.doma.internal.apt.meta.entity.EmbeddableMetaFactory;

@SupportedAnnotationTypes({"org.seasar.doma.Embeddable"})
@SupportedOptions({
  Options.VERSION_VALIDATION,
  Options.RESOURCES_DIR,
  Options.LOMBOK_VALUE,
  Options.LOMBOK_ALL_ARGS_CONSTRUCTOR,
  Options.TEST,
  Options.TRACE,
  Options.DEBUG,
  Options.CONFIG_PATH,
  Options.METAMODEL_ENABLED,
  Options.METAMODEL_PREFIX,
  Options.METAMODEL_SUFFIX
})
public class EmbeddableProcessor extends AbstractGeneratingProcessor<EmbeddableMeta> {

  public EmbeddableProcessor() {
    super(Embeddable.class);
  }

  @Override
  protected TypeElementMetaFactory<EmbeddableMeta> createTypeElementMetaFactory() {
    return new EmbeddableMetaFactory(ctx);
  }

  @Override
  protected ClassName createClassName(TypeElement typeElement, EmbeddableMeta meta) {
    assertNotNull(typeElement, meta);
    Name binaryName = ctx.getMoreElements().getBinaryName(typeElement);
    return ClassNames.newEmbeddableTypeClassName(binaryName);
  }

  @Override
  protected Generator createGenerator(ClassName className, Printer printer, EmbeddableMeta meta) {
    assertNotNull(className, meta, printer);
    return new EmbeddableTypeGenerator(ctx, className, printer, meta);
  }
}
