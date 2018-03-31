package org.seasar.doma.internal.apt.processor;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Formatter;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.Entity;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.codespec.CodeSpec;
import org.seasar.doma.internal.apt.generator.EntityDescGenerator;
import org.seasar.doma.internal.apt.generator.Generator;
import org.seasar.doma.internal.apt.meta.entity.EntityMeta;
import org.seasar.doma.internal.apt.meta.entity.EntityMetaFactory;

/** @author taedium */
@SupportedAnnotationTypes({"org.seasar.doma.Entity"})
@SupportedOptions({
  Options.ENTITY_FIELD_PREFIX,
  Options.HOLDER_CONVERTERS,
  Options.VERSION_VALIDATION,
  Options.RESOURCES_DIR,
  Options.LOMBOK_VALUE,
  Options.LOMBOK_ALL_ARGS_CONSTRUCTOR,
  Options.TEST,
  Options.DEBUG
})
public class EntityProcessor extends AbstractGeneratingProcessor<EntityMeta> {

  public EntityProcessor() {
    super(Entity.class);
  }

  @Override
  protected EntityMetaFactory createTypeElementMetaFactory(TypeElement typeElement) {
    return new EntityMetaFactory(ctx, typeElement);
  }

  @Override
  protected CodeSpec createCodeSpec(EntityMeta meta) {
    return ctx.getCodeSpecs().newEntityDescCodeSpec(meta.getEntityElement());
  }

  @Override
  protected Generator createGenerator(EntityMeta meta, CodeSpec codeSpec, Formatter formatter) {
    assertNotNull(meta, codeSpec, formatter);
    return new EntityDescGenerator(ctx, meta, codeSpec, formatter);
  }
}
