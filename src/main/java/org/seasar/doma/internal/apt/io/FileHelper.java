package org.seasar.doma.internal.apt.io;

import static org.seasar.doma.internal.util.AssertionUtil.assertUnreachable;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import javax.lang.model.element.ExecutableElement;
import javax.tools.FileObject;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.util.IOUtil;
import org.seasar.doma.message.Message;

public class FileHelper {

  private final Context ctx;
  private final ExecutableElement methodElement;

  public FileHelper(Context ctx, ExecutableElement methodElement) {
    this.ctx = ctx;
    this.methodElement = methodElement;
  }

  public File getFile(String filePath) {
    var fileObject = getFileObject(filePath);
    var uri = fileObject.toUri();
    if (!uri.isAbsolute()) {
      uri = new File(".").toURI().resolve(uri);
    }
    var file = getCanonicalFile(new File(uri));
    if (!file.exists()) {
      throw new AptException(
          Message.DOMA4019, methodElement, new Object[] {filePath, file.getAbsolutePath()});
    }
    if (file.isDirectory()) {
      throw new AptException(
          Message.DOMA4021, methodElement, new Object[] {filePath, file.getAbsolutePath()});
    }
    if (!IOUtil.endsWith(file, filePath)) {
      throw new AptException(
          Message.DOMA4309, methodElement, new Object[] {filePath, file.getAbsolutePath()});
    }
    return file;
  }

  public File[] getSiblingFiles(File file) {
    var dir = getDir(file);
    var files = dir.listFiles();
    if (files == null) {
      throw new AptException(Message.DOMA4144, methodElement, new Object[] {dir.getAbsolutePath()});
    }
    return files;
  }

  private File getCanonicalFile(File file) {
    try {
      return file.getCanonicalFile();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private File getDir(File sqlFile) {
    var dir = sqlFile.getParentFile();
    if (dir == null) {
      assertUnreachable();
    }
    return dir;
  }

  private FileObject getFileObject(String path) {
    try {
      return ctx.getResources().getResource(path);
    } catch (IOException e) {
      throw new AptException(Message.DOMA4143, methodElement, e, new Object[] {path, e});
    }
  }
}
