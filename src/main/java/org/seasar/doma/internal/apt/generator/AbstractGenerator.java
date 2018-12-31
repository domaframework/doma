package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Formatter;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;
import org.seasar.doma.internal.Artifact;
import org.seasar.doma.internal.Conventions;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.message.Message;

public abstract class AbstractGenerator implements Generator {

  protected static final String INDENT_SPACE = "    ";

  protected final Context ctx;

  protected final TypeElement typeElement;

  protected final String canonicalName;

  protected final String packageName;

  protected final String simpleName;

  protected final String fullpackage;

  protected final String subpackage;

  protected final String prefix;

  protected final String suffix;

  protected final Formatter formatter;

  protected final StringBuilder indentBuffer = new StringBuilder();

  protected AbstractGenerator(
      Context ctx,
      TypeElement typeElement,
      String fullpackage,
      String subpackage,
      String prefix,
      String suffix)
      throws IOException {
    assertNotNull(ctx, typeElement, prefix, suffix);
    this.ctx = ctx;
    this.typeElement = typeElement;
    this.fullpackage = fullpackage;
    this.subpackage = subpackage;
    this.prefix = prefix;
    this.suffix = suffix;
    this.canonicalName =
        createCanonicalName(ctx, typeElement, fullpackage, subpackage, prefix, suffix);
    this.packageName = ClassUtil.getPackageName(canonicalName);
    this.simpleName = ClassUtil.getSimpleName(canonicalName);
    JavaFileObject file = ctx.getResources().createSourceFile(canonicalName, typeElement);
    formatter = new Formatter(new BufferedWriter(file.openWriter()));
  }

  protected String createCanonicalName(
      Context ctx,
      TypeElement typeElement,
      String fullpackage,
      String subpackage,
      String prefix,
      String suffix) {
    String qualifiedNamePrefix = getQualifiedNamePrefix(ctx, typeElement, fullpackage, subpackage);
    String binaryName =
        Conventions.normalizeBinaryName(ctx.getElements().getBinaryNameAsString(typeElement));
    String infix = ClassUtil.getSimpleName(binaryName);
    return qualifiedNamePrefix + prefix + infix + suffix;
  }

  protected String getQualifiedNamePrefix(
      Context ctx, TypeElement typeElement, String fullpackage, String subpackage) {
    if (fullpackage != null) {
      return fullpackage + ".";
    }
    String packageName = ctx.getElements().getPackageName(typeElement);
    String base = "";
    if (packageName != null && packageName.length() > 0) {
      base = packageName + ".";
    }
    if (subpackage != null) {
      return base + subpackage + ".";
    }
    return base;
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
    formatter.format(indentBuffer.toString());
    throwExceptionIfNecessary();
    formatter.format(format, args);
    throwExceptionIfNecessary();
  }

  protected void print(String format, Object... args) {
    formatter.format(format, args);
    throwExceptionIfNecessary();
  }

  protected void throwExceptionIfNecessary() {
    IOException ioException = formatter.ioException();
    if (ioException != null) {
      formatter.close();
      throw new AptException(
          Message.DOMA4079,
          ctx.getEnv(),
          typeElement,
          ioException,
          new Object[] {canonicalName, ioException});
    }
  }

  protected void indent() {
    indentBuffer.append(INDENT_SPACE);
  }

  protected void unindent() {
    if (indentBuffer.length() >= INDENT_SPACE.length()) {
      indentBuffer.setLength(indentBuffer.length() - INDENT_SPACE.length());
    }
  }

  @Override
  public void close() {
    if (formatter != null) {
      formatter.close();
    }
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
