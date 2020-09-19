package org.seasar.doma.internal.apt;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.tools.FileObject;
import org.junit.jupiter.api.Test;

public class ResourcesTest {

  @Test
  public void testFileObjectImpl_toUri() throws Exception {
    Path path = Paths.get("aaa", "bbb");
    FileObject fileObject = new Resources.FileObjectImpl(path);
    assertNotNull(fileObject.toUri());
  }

  @Test
  public void testFileObjectImpl_getName() throws Exception {
    Path path = Paths.get("aaa", "bbb");
    FileObject fileObject = new Resources.FileObjectImpl(path);
    assertNotNull(fileObject.getName());
  }

  @Test
  public void testFileObjectImpl_openInputStream() throws Exception {
    File file = File.createTempFile("aaa", null);
    try {
      FileObject fileObject = new Resources.FileObjectImpl(file.toPath());
      try (InputStream is = fileObject.openInputStream()) {
        //noinspection ResultOfMethodCallIgnored
        is.read();
      }
    } finally {
      //noinspection ResultOfMethodCallIgnored
      file.delete();
    }
  }
}
