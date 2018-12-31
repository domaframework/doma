package org.seasar.doma.internal.apt;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.tools.FileObject;
import junit.framework.TestCase;

public class ResourcesTest extends TestCase {

  public void testFileObjectImpl_toUri() throws Exception {
    Path path = Paths.get("aaa", "bbb");
    FileObject fileObject = new Resources.FileObjectImpl(path);
    assertNotNull(fileObject.toUri());
  }

  public void testFileObjectImpl_getName() throws Exception {
    Path path = Paths.get("aaa", "bbb");
    FileObject fileObject = new Resources.FileObjectImpl(path);
    assertNotNull(fileObject.getName());
  }

  public void testFileObjectImpl_openInputStream() throws Exception {
    File file = File.createTempFile("aaa", null);
    try {
      FileObject fileObject = new Resources.FileObjectImpl(file.toPath());
      try (InputStream is = fileObject.openInputStream()) {
        is.read();
      }
    } finally {
      file.delete();
    }
  }
}
