package org.seasar.doma.internal.util;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.seasar.doma.internal.WrapException;

public final class ResourceUtil {

  public static URL getResource(String path) {
    assertNotNull(path);
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    if (loader == null) {
      return null;
    }
    URL url = loader.getResource(path);
    if (url == null) {
      url = ResourceUtil.class.getResource("/" + path);
    }
    return url;
  }

  public static InputStream getResourceAsStream(String path) {
    assertNotNull(path);
    URL url = getResource(path);
    try {
      return url != null ? url.openStream() : null;
    } catch (IOException e) {
      return null;
    }
  }

  public static String getResourceAsString(String path) throws WrapException {
    assertNotNull(path);
    assertTrue(path.length() > 0);
    InputStream inputStream = getResourceAsStream(path);
    if (inputStream == null) {
      return null;
    }
    return IOUtil.readAsString(inputStream);
  }
}
