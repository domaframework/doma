package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.IOException;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.Entity;
import org.seasar.doma.internal.apt.meta.EntityMeta;
import org.seasar.doma.internal.apt.meta.EntityMetaFactory;
import org.seasar.doma.internal.apt.meta.EntityPropertyMetaFactory;

/** @author taedium */
@SupportedAnnotationTypes({"org.seasar.doma.Entity"})
@SupportedOptions({
  Options.ENTITY_FIELD_PREFIX,
  Options.DOMAIN_CONVERTERS,
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
  protected EntityMetaFactory createTypeElementMetaFactory() {
    EntityPropertyMetaFactory propertyMetaFactory = createEntityPropertyMetaFactory();
    return new EntityMetaFactory(processingEnv, propertyMetaFactory);
  }

  protected EntityPropertyMetaFactory createEntityPropertyMetaFactory() {
    return new EntityPropertyMetaFactory(processingEnv);
  }

  @Override
  protected Generator createGenerator(TypeElement typeElement, EntityMeta meta) throws IOException {
    assertNotNull(typeElement, meta);
    return new EntityTypeGenerator(processingEnv, typeElement, meta);
  }
}
