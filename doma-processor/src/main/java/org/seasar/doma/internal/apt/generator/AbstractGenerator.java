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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.Artifact;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.apt.RoundContext;

public abstract class AbstractGenerator implements Generator {

  protected final RoundContext ctx;

  protected final ClassName className;

  protected final String packageName;

  protected final String simpleName;

  protected final Printer printer;

  protected AbstractGenerator(RoundContext ctx, ClassName className, Printer printer) {
    assertNotNull(ctx, className, printer);
    this.ctx = ctx;
    this.className = className;
    this.packageName = className.getPackageName();
    this.simpleName = className.getSimpleName();
    this.printer = printer;
  }

  protected void printGenerated() {
    String annotationElements =
        String.format(
            "value = { \"%s\", \"%s\" }, date = \"%tFT%<tT.%<tL%<tz\"",
            Artifact.getName(), ctx.getOptions().getVersion(), ctx.getOptions().getDate());
    TypeMirror generatedTypeMirror = getGeneratedTypeMirror();
    if (generatedTypeMirror == null) {
      iprint("// %s%n", annotationElements);
    }
    iprint("@%s(%s)%n", generatedTypeMirror, annotationElements);
  }

  protected void printValidateVersionStaticInitializer() {
    if (ctx.getOptions().getVersionValidation()) {
      iprint("static {%n");
      iprint(
          "    %1$s.validateVersion(\"%2$s\");%n", Artifact.class, ctx.getOptions().getVersion());
      iprint("}%n");
      print("%n");
    }
  }

  protected void iprint(String format, Object... args) {
    printer.iprint(format, args);
  }

  protected void print(String format, Object... args) {
    printer.print(format, args);
  }

  protected void indent() {
    printer.indent();
  }

  protected void unindent() {
    printer.unindent();
  }

  private TypeMirror getGeneratedTypeMirror() {
    TypeElement generatedElement =
        ctx.getMoreElements().getTypeElement("javax.annotation.processing.Generated");
    if (generatedElement != null) {
      return generatedElement.asType();
    }
    return null;
  }
}
