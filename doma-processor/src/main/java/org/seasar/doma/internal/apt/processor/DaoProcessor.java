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
import java.util.Set;
import java.util.function.Function;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.Dao;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.generator.DaoImplGenerator;
import org.seasar.doma.internal.apt.generator.Generator;
import org.seasar.doma.internal.apt.generator.JavaFileGenerator;
import org.seasar.doma.internal.apt.generator.Printer;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;
import org.seasar.doma.internal.apt.meta.dao.DaoMetaFactory;
import org.seasar.doma.internal.util.ClassUtil;

public class DaoProcessor implements ElementProcessor<DaoMeta> {

  private final RoundContext ctx;
  private final ElementProcessorSupport<DaoMeta> support;
  private final DaoMetaFactory factory;
  private final Function<TypeElement, ClassName> classNameProvider;

  public DaoProcessor(RoundContext ctx) {
    this.ctx = ctx;
    this.support = new ElementProcessorSupport<>(ctx, Dao.class);
    this.factory = new DaoMetaFactory(ctx);
    this.classNameProvider =
        typeElement -> {
          DaoImplClassNameBuilder builder = new DaoImplClassNameBuilder(typeElement);
          return builder.build();
        };
  }

  @Override
  public List<DaoMeta> process(Set<? extends Element> elements) {
    return support.processTypeElements(elements, this::processEach);
  }

  private DaoMeta processEach(TypeElement typeElement) {
    var meta = factory.createTypeElementMeta(typeElement);
    if (!meta.isError()) {
      generate(typeElement, meta);
    }
    return meta;
  }

  private void generate(TypeElement typeElement, DaoMeta meta) {
    var javaFileGenerator =
        new JavaFileGenerator<>(ctx, this::createClassName, this::createGenerator);
    javaFileGenerator.generate(typeElement, meta);
  }

  private ClassName createClassName(TypeElement typeElement, DaoMeta meta) {
    assertNotNull(typeElement, meta);
    return classNameProvider.apply(typeElement);
  }

  private Generator createGenerator(ClassName className, Printer printer, DaoMeta meta) {
    assertNotNull(className, meta, printer);
    return new DaoImplGenerator(ctx, className, printer, meta, classNameProvider);
  }

  private class DaoImplClassNameBuilder {

    private final TypeElement typeElement;

    private DaoImplClassNameBuilder(TypeElement typeElement) {
      this.typeElement = typeElement;
    }

    protected String prefix() {
      var daoPackage = ctx.getOptions().getDaoPackage();
      if (daoPackage != null) {
        return daoPackage + ".";
      }
      var packageElement = ctx.getMoreElements().getPackageOf(typeElement);
      var packageName = packageElement.getQualifiedName();
      var base = "";
      if (!packageName.isEmpty()) {
        base = packageName + ".";
      }
      var daoSubpackage = ctx.getOptions().getDaoSubpackage();
      if (daoSubpackage != null) {
        return base + daoSubpackage + ".";
      }
      return base;
    }

    protected String infix() {
      var binaryName = ctx.getMoreElements().getBinaryName(typeElement);
      var normalizedName = ClassNames.normalizeBinaryName(binaryName);
      return ClassUtil.getSimpleName(normalizedName);
    }

    protected String suffix() {
      return ctx.getOptions().getDaoSuffix();
    }

    public ClassName build() {
      return new ClassName(prefix() + infix() + suffix());
    }
  }
}
