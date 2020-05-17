package org.seasar.doma.internal.apt.processor;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import org.seasar.doma.Entity;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.annot.EntityAnnot;
import org.seasar.doma.internal.apt.annot.MetamodelAnnot;
import org.seasar.doma.internal.apt.generator.EntityMetamodelGenerator;
import org.seasar.doma.internal.apt.generator.EntityTypeGenerator;
import org.seasar.doma.internal.apt.generator.Generator;
import org.seasar.doma.internal.apt.generator.JavaFileGenerator;
import org.seasar.doma.internal.apt.generator.Printer;
import org.seasar.doma.internal.apt.meta.entity.EntityMeta;
import org.seasar.doma.internal.apt.meta.entity.EntityMetaFactory;

@SupportedAnnotationTypes({"org.seasar.doma.Entity"})
@SupportedOptions({
  Options.ENTITY_FIELD_PREFIX,
  Options.DOMAIN_CONVERTERS,
  Options.VERSION_VALIDATION,
  Options.RESOURCES_DIR,
  Options.LOMBOK_VALUE,
  Options.LOMBOK_ALL_ARGS_CONSTRUCTOR,
  Options.TEST,
  Options.DEBUG,
  Options.CONFIG_PATH,
  Options.METAMODEL_ENABLED,
  Options.METAMODEL_PREFIX,
  Options.METAMODEL_SUFFIX
})
public class EntityProcessor extends AbstractProcessor {

  public EntityProcessor() {
    super(Entity.class);
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (roundEnv.processingOver()) {
      return true;
    }
    for (TypeElement annotation : annotations) {
      final EntityMetaFactory factory = new EntityMetaFactory(ctx);
      for (TypeElement typeElement :
          ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(annotation))) {
        handleTypeElement(
            typeElement,
            __ -> {
              EntityMeta meta = factory.createTypeElementMeta(typeElement);
              if (!meta.isError()) {
                generateEntityType(typeElement, meta);
                if (isMetamodelEnabled(meta)) {
                  generateEntityMetamodel(typeElement, meta);
                }
              }
            });
      }
    }
    return true;
  }

  private boolean isMetamodelEnabled(EntityMeta meta) {
    EntityAnnot entityAnnot = meta.getEntityAnnot();
    MetamodelAnnot metamodelAnnot = entityAnnot.getMetamodelValue();
    return metamodelAnnot != null || ctx.getOptions().isMetamodelEnabled();
  }

  private void generateEntityType(TypeElement typeElement, EntityMeta meta) {
    JavaFileGenerator<EntityMeta> generator =
        new JavaFileGenerator<>(
            ctx, this::createEntityTypeClassName, this::createEntityTypeGenerator);
    generator.generate(typeElement, meta);
  }

  private ClassName createEntityTypeClassName(TypeElement typeElement, EntityMeta meta) {
    assertNotNull(typeElement, meta);
    Name binaryName = ctx.getMoreElements().getBinaryName(typeElement);
    return ClassNames.newEntityTypeClassName(binaryName);
  }

  private Generator createEntityTypeGenerator(
      ClassName className, Printer printer, EntityMeta meta) {
    assertNotNull(className, meta, printer);
    return new EntityTypeGenerator(ctx, className, printer, meta);
  }

  private void generateEntityMetamodel(TypeElement typeElement, EntityMeta meta) {
    JavaFileGenerator<EntityMeta> generator =
        new JavaFileGenerator<>(
            ctx, this::createEntityMetamodelClassName, this::createEntityMetamodelGenerator);
    generator.generate(typeElement, meta);
  }

  private ClassName createEntityMetamodelClassName(TypeElement typeElement, EntityMeta meta) {
    assertNotNull(typeElement, meta);
    EntityAnnot entityAnnot = meta.getEntityAnnot();
    MetamodelAnnot metamodelAnnot = entityAnnot.getMetamodelValue();
    Name binaryName = ctx.getMoreElements().getBinaryName(typeElement);
    String prefix = ctx.getOptions().getMetamodelPrefix();
    String suffix = ctx.getOptions().getMetamodelSuffix();
    if (metamodelAnnot != null) {
      String prefixValue = metamodelAnnot.getPrefixValue();
      String suffixValue = metamodelAnnot.getSuffixValue();
      if (!prefixValue.isEmpty() || !suffixValue.isEmpty()) {
        prefix = prefixValue;
        suffix = suffixValue;
      }
    }
    return ClassNames.newEntityMetamodelClassNameBuilder(binaryName, prefix, suffix);
  }

  private Generator createEntityMetamodelGenerator(
      ClassName className, Printer printer, EntityMeta meta) {
    assertNotNull(className, meta, printer);
    ClassName entityTypeName = createEntityTypeClassName(meta.getTypeElement(), meta);
    return new EntityMetamodelGenerator(ctx, className, printer, meta, entityTypeName);
  }
}
