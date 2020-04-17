package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

public class Resources {

  private Filer filer;

  private String resourcesDir;

  Resources(Context ctx, ProcessingEnvironment env) {
    assertNotNull(ctx, env);
    this.filer = env.getFiler();
    this.resourcesDir = env.getOptions().get(Options.RESOURCES_DIR);
  }

  public JavaFileObject createSourceFile(CharSequence name, Element... originatingElements)
      throws IOException {
    return filer.createSourceFile(name, originatingElements);
  }

  public FileObject getResource(String relativePath) throws IOException {
    assertNotNull(relativePath);
    if (resourcesDir != null) {
      Path path = Paths.get(resourcesDir, relativePath);
      return new FileObjectImpl(path);
    }
    return filer.getResource(StandardLocation.CLASS_OUTPUT, "", relativePath);
  }

  protected static class FileObjectImpl implements FileObject {

    private final Path path;

    FileObjectImpl(Path path) {
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
    public OutputStream openOutputStream() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Reader openReader(boolean ignoreEncodingErrors) {
      throw new UnsupportedOperationException();
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
      throw new UnsupportedOperationException();
    }

    @Override
    public Writer openWriter() {
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
