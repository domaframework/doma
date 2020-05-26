package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.internal.Artifact;
import org.seasar.doma.internal.ClassName;
import org.seasar.doma.internal.apt.Context;

public abstract class AbstractGenerator implements Generator {

  protected final Context ctx;

  protected final ClassName className;

  protected final String packageName;

  protected final String simpleName;

  protected final Printer printer;

  protected AbstractGenerator(Context ctx, ClassName className, Printer printer) {
    assertNotNull(ctx, className, printer);
    this.ctx = ctx;
    this.className = className;
    this.packageName = className.getPackageName();
    this.simpleName = className.getSimpleName();
    this.printer = printer;
  }

  protected void printGenerated() {
    String generationInformation =
        String.format(
            "value = { \"%s\", \"%s\" }, date = \"%tFT%<tT.%<tL%<tz\"",
            Artifact.getName(), ctx.getOptions().getVersion(), ctx.getOptions().getDate());
    iprint("// Generated(%s)%n", generationInformation);
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
}
