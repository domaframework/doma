/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.apt.processor;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.Entity;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.generator.EntityMetamodelGenerator;
import org.seasar.doma.internal.apt.generator.EntityTypeGenerator;
import org.seasar.doma.internal.apt.generator.Generator;
import org.seasar.doma.internal.apt.generator.JavaFileGenerator;
import org.seasar.doma.internal.apt.generator.Printer;
import org.seasar.doma.internal.apt.meta.entity.EntityMeta;
import org.seasar.doma.internal.apt.meta.entity.EntityMetaFactory;

public class EntityProcessor implements ElementProcessor<EntityMeta> {

  private final RoundContext ctx;
  private final ElementProcessorSupport<EntityMeta> support;
  private final EntityMetaFactory factory;

  public EntityProcessor(RoundContext ctx) {
    this.ctx = Objects.requireNonNull(ctx);
    this.support = new ElementProcessorSupport<>(ctx, Entity.class);
    this.factory = new EntityMetaFactory(ctx);
  }

  @Override
  public List<EntityMeta> process(Set<? extends Element> elements) {
    return support.processTypeElements(elements, this::processEach);
  }

  private EntityMeta processEach(TypeElement typeElement) {
    var meta = factory.createTypeElementMeta(typeElement);
    if (!meta.isError()) {
      generateEntityType(typeElement, meta);
      if (isMetamodelEnabled(meta)) {
        generateEntityMetamodel(typeElement, meta);
      }
    }
    return meta;
  }

  private boolean isMetamodelEnabled(EntityMeta meta) {
    var entityAnnot = meta.getEntityAnnot();
    var metamodelAnnot = entityAnnot.getMetamodelValue();
    return metamodelAnnot != null || ctx.getOptions().isMetamodelEnabled();
  }

  private void generateEntityType(TypeElement typeElement, EntityMeta meta) {
    var generator =
        new JavaFileGenerator<>(
            ctx, this::createEntityTypeClassName, this::createEntityTypeGenerator);
    generator.generate(typeElement, meta);
  }

  private ClassName createEntityTypeClassName(TypeElement typeElement, EntityMeta meta) {
    assertNotNull(typeElement, meta);
    var binaryName = ctx.getMoreElements().getBinaryName(typeElement);
    return ClassNames.newEntityTypeClassName(binaryName);
  }

  private Generator createEntityTypeGenerator(
      ClassName className, Printer printer, EntityMeta meta) {
    assertNotNull(className, meta, printer);
    return new EntityTypeGenerator(ctx, className, printer, meta);
  }

  private void generateEntityMetamodel(TypeElement typeElement, EntityMeta meta) {
    var generator =
        new JavaFileGenerator<>(
            ctx, this::createEntityMetamodelClassName, this::createEntityMetamodelGenerator);
    generator.generate(typeElement, meta);
  }

  private ClassName createEntityMetamodelClassName(TypeElement typeElement, EntityMeta meta) {
    assertNotNull(typeElement, meta);
    var entityAnnot = meta.getEntityAnnot();
    var metamodelAnnot = entityAnnot.getMetamodelValue();
    var binaryName = ctx.getMoreElements().getBinaryName(typeElement);
    var prefix = ctx.getOptions().getMetamodelPrefix();
    var suffix = ctx.getOptions().getMetamodelSuffix();
    if (metamodelAnnot != null) {
      var prefixValue = metamodelAnnot.getPrefixValue();
      var suffixValue = metamodelAnnot.getSuffixValue();
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
    var entityTypeName = createEntityTypeClassName(meta.getTypeElement(), meta);
    return new EntityMetamodelGenerator(ctx, className, printer, meta, entityTypeName);
  }
}
