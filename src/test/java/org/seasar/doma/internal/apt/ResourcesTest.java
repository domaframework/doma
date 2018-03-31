package org.seasar.doma.internal.apt;

import java.io.File;
import java.nio.file.Paths;
import javax.tools.FileObject;
import junit.framework.TestCase;

public class ResourcesTest extends TestCase {

  public void testFileObjectImpl_toUri() throws Exception {
    var path = Paths.get("aaa", "bbb");
    FileObject fileObject = new Resources.FileObjectImpl(path);
    assertNotNull(fileObject.toUri());
  }

  public void testFileObjectImpl_getName() throws Exception {
    var path = Paths.get("aaa", "bbb");
    FileObject fileObject = new Resources.FileObjectImpl(path);
    assertNotNull(fileObject.getName());
  }

  public void testFileObjectImpl_openInputStream() throws Exception {
    var file = File.createTempFile("aaa", null);
    try {
      FileObject fileObject = new Resources.FileObjectImpl(file.toPath());
      try (var is = fileObject.openInputStream()) {
        is.read();
      }
    } finally {
      file.delete();
    }
  }
}
