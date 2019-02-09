package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.Artifact;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.TypeName;

public abstract class AbstractGenerator implements Generator {

  protected final Context ctx;

  protected final TypeName typeName;

  protected final ClassName className;

  protected final String packageName;

  protected final String simpleName;

  protected final Printer printer;

  protected AbstractGenerator(Context ctx, TypeName typeName, Printer printer) {
    assertNotNull(ctx, typeName, printer);
    this.ctx = ctx;
    this.typeName = typeName;
    this.className = typeName.getClassName();
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
          "    %1$s.validateVersion(\"%2$s\");%n",
          Artifact.class.getName(), ctx.getOptions().getVersion());
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
    TypeElement java8 =
        ctx.getElements().getTypeElement((CharSequence) "javax.annotation.Generated");
    if (java8 != null) {
      return java8.asType();
    }
    TypeElement java9 =
        ctx.getElements().getTypeElement((CharSequence) "javax.annotation.processing.Generated");
    if (java9 != null) {
      return java9.asType();
    }
    return null;
  }
}
