package org.seasar.doma.internal.apt.processor;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.EmbeddableDesc;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.generator.EmbeddableDefGenerator;
import org.seasar.doma.internal.apt.generator.Generator;
import org.seasar.doma.internal.apt.generator.Printer;
import org.seasar.doma.internal.apt.meta.entity.EmbeddableDescMeta;
import org.seasar.doma.internal.apt.meta.entity.EmbeddableDescMetaFactory;
import org.seasar.doma.internal.apt.meta.entity.EmbeddableMeta;

@SupportedAnnotationTypes({"org.seasar.doma.internal.EmbeddableDesc"})
@SupportedOptions({
  Options.RESOURCES_DIR,
  Options.TEST,
  Options.DEBUG,
  Options.CONFIG_PATH,
  Options.CRITERIA_ENABLED,
  Options.CRITERIA_PREFIX,
  Options.CRITERIA_SUFFIX
})
public class EmbeddableDescProcessor extends AbstractGeneratingProcessor<EmbeddableDescMeta> {

  public EmbeddableDescProcessor() {
    super(EmbeddableDesc.class);
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (!ctx.getOptions().isCriteriaEnabled()) {
      return true;
    }
    return super.process(annotations, roundEnv);
  }

  @Override
  protected EmbeddableDescMetaFactory createTypeElementMetaFactory() {
    return new EmbeddableDescMetaFactory(ctx);
  }

  @Override
  protected ClassName createClassName(TypeElement typeElement, EmbeddableDescMeta meta) {
    assertNotNull(typeElement, meta);
    EmbeddableMeta embeddableMeta = meta.getEmbeddableMeta();
    TypeElement embeddableTypeElement = embeddableMeta.getTypeElement();
    Name binaryName = ctx.getMoreElements().getBinaryName(embeddableTypeElement);
    String prefix = ctx.getOptions().getCriteriaPrefix();
    String suffix = ctx.getOptions().getCriteriaSuffix();
    return ClassNames.newEmbeddableDefClassNameBuilder(binaryName, prefix, suffix);
  }

  @Override
  protected Generator createGenerator(
      ClassName className, Printer printer, EmbeddableDescMeta meta) {
    assertNotNull(className, meta, printer);
    return new EmbeddableDefGenerator(ctx, className, printer, meta);
  }
}
