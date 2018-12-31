package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

public class Resources {

  private Context ctx;

  public Resources(Context ctx) {
    this.ctx = ctx;
  }

  public JavaFileObject createSourceFile(CharSequence name, Element... originatingElements)
      throws IOException {
    Filer filer = ctx.getEnv().getFiler();
    return filer.createSourceFile(name, originatingElements);
  }

  public FileObject getResource(String relativePath) throws IOException {
    assertNotNull(relativePath);
    Map<String, String> options = ctx.getEnv().getOptions();
    String resourcesDir = options.get(Options.RESOURCES_DIR);
    if (resourcesDir != null) {
      Path path = Paths.get(resourcesDir, relativePath);
      return new FileObjectImpl(path);
    }
    Filer filer = ctx.getEnv().getFiler();
    return filer.getResource(StandardLocation.CLASS_OUTPUT, "", relativePath);
  }

  protected static class FileObjectImpl implements FileObject {

    private final Path path;

    protected FileObjectImpl(Path path) {
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
