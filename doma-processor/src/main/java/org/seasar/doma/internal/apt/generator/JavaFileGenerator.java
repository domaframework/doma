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
package org.seasar.doma.internal.apt.generator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Formatter;
import java.util.Objects;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.RoundContext;
import org.seasar.doma.internal.apt.meta.TypeElementMeta;
import org.seasar.doma.internal.util.IOUtil;
import org.seasar.doma.message.Message;

public class JavaFileGenerator<M extends TypeElementMeta> {

  private final RoundContext ctx;
  private final ClassNameFactory<M> classNameFactory;
  private final GeneratorFactory<M> generatorFactory;

  public JavaFileGenerator(
      RoundContext ctx,
      ClassNameFactory<M> classNameFactory,
      GeneratorFactory<M> generatorFactory) {
    this.ctx = Objects.requireNonNull(ctx);
    this.classNameFactory = Objects.requireNonNull(classNameFactory);
    this.generatorFactory = Objects.requireNonNull(generatorFactory);
  }

  public void generate(TypeElement typeElement, M meta) {
    ClassName className = classNameFactory.create(typeElement, meta);
    Formatter formatter = null;
    try {
      JavaFileObject file = ctx.getResources().createSourceFile(className, typeElement);
      formatter = new Formatter(new BufferedWriter(file.openWriter()));
      Printer printer = new Printer(ctx, formatter);
      Generator generator = generatorFactory.create(className, printer, meta);
      generator.generate();
    } catch (IOException | UncheckedIOException e) {
      throw new AptException(
          Message.DOMA4011, typeElement, e, new Object[] {typeElement.getQualifiedName(), e});
    } finally {
      IOUtil.close(formatter);
    }
  }

  public interface ClassNameFactory<M extends TypeElementMeta> {
    ClassName create(TypeElement typeElement, M meta);
  }

  public interface GeneratorFactory<M extends TypeElementMeta> {
    Generator create(ClassName className, Printer printer, M meta);
  }
}
