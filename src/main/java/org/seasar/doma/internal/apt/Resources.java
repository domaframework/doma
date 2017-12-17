package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

public class Resources {

    private final ProcessingEnvironment env;

    private final Filer filer;

    public Resources(Context ctx) {
        assertNotNull(ctx);
        this.env = ctx.getEnv();
        this.filer = env.getFiler();
    }

    public JavaFileObject createSourceFile(CharSequence name, Element... originatingElements)
            throws IOException {
        return filer.createSourceFile(name, originatingElements);
    }

    public FileObject getResource(String relativePath) throws IOException {
        assertNotNull(relativePath);
        Map<String, String> options = env.getOptions();
        String resourcesDir = options.get(Options.RESOURCES_DIR);
        if (resourcesDir != null) {
            Path path = Paths.get(resourcesDir, relativePath);
            return new FileObjectImpl(path);
        }
        return filer.getResource(StandardLocation.CLASS_OUTPUT, "", relativePath);
    }

    public static class FileObjectImpl implements FileObject {

        private final Path path;

        public FileObjectImpl(Path path) {
            this.path = path;
        }

        @Override
        public URI toUri() {
            return path.toUri();
        }

        @Override
        public String getName() {
            return path.toString();
        }

        @Override
        public InputStream openInputStream() throws IOException {
            return Files.newInputStream(path);
        }

        @Override
        public OutputStream openOutputStream() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Writer openWriter() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public long getLastModified() {
            return 0L;
        }

        @Override
        public boolean delete() {
            return false;
        }
    }

}
