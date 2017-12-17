package org.seasar.doma.internal.apt.generator;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Formatter;

import javax.annotation.processing.Generated;

import org.seasar.doma.internal.Artifact;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.codespec.CodeSpec;

/**
 * @author taedium
 * 
 */
public abstract class AbstractGenerator implements Generator {

    private static final String INDENT_SPACE = "    ";

    protected final Context ctx;

    protected final CodeSpec codeSpec;

    private final Formatter formatter;

    private final StringBuilder indentBuffer = new StringBuilder();

    protected AbstractGenerator(Context ctx, CodeSpec codeSpec, Formatter formatter) {
        assertNotNull(ctx, codeSpec, formatter);
        this.ctx = ctx;
        this.codeSpec = codeSpec;
        this.formatter = formatter;
    }

    protected void printPackage() {
        if (!codeSpec.getPackageName().isEmpty()) {
            iprint("package %1$s;%n", codeSpec.getPackageName());
            iprint("%n");
        }
    }

    protected void printGenerated() {
        iprint("@%s(value = { \"%s\", \"%s\" }, date = \"%tFT%<tT.%<tL%<tz\")%n",
                Generated.class.getName(), Artifact.getName(), ctx.getOptions().getVersion(),
                ctx.getOptions().getDate());
    }

    protected void printValidateVersionStaticInitializer() {
        if (ctx.getOptions().getVersionValidation()) {
            iprint("static {%n");
            iprint("    %1$s.validateVersion(\"%2$s\");%n", Artifact.class.getName(),
                    ctx.getOptions().getVersion());
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
            throw new UncheckedIOException(ioException);
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

}
