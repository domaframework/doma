package org.seasar.doma.internal.apt.processor;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.EntityDesc;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.generator.EntityDefGenerator;
import org.seasar.doma.internal.apt.generator.Generator;
import org.seasar.doma.internal.apt.generator.Printer;
import org.seasar.doma.internal.apt.meta.entity.EntityDescMeta;
import org.seasar.doma.internal.apt.meta.entity.EntityDescMetaFactory;
import org.seasar.doma.internal.apt.meta.entity.EntityMeta;

@SupportedAnnotationTypes({"org.seasar.doma.internal.EntityDesc"})
@SupportedOptions({
  Options.RESOURCES_DIR,
  Options.TEST,
  Options.DEBUG,
  Options.CONFIG_PATH,
  Options.CRITERIA_ENABLED
})
public class EntityDescProcessor extends AbstractGeneratingProcessor<EntityDescMeta> {

  public EntityDescProcessor() {
    super(EntityDesc.class);
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (!ctx.getOptions().isCriteriaEnabled()) {
      return true;
    }
    return super.process(annotations, roundEnv);
  }

  @Override
  protected EntityDescMetaFactory createTypeElementMetaFactory() {
    return new EntityDescMetaFactory(ctx);
  }

  @Override
  protected ClassName createClassName(TypeElement typeElement, EntityDescMeta meta) {
    assertNotNull(typeElement, meta);
    EntityMeta entityMeta = meta.getEntityMeta();
    TypeElement entityTypeElement = entityMeta.getTypeElement();
    return new ClassName(entityTypeElement.getQualifiedName().toString() + "_");
  }

  @Override
  protected Generator createGenerator(ClassName className, Printer printer, EntityDescMeta meta) {
    assertNotNull(className, meta, printer);
    return new EntityDefGenerator(ctx, className, printer, meta);
  }
}
