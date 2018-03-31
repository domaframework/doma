package org.seasar.doma.internal.util;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.nio.file.Paths;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.WrapException;

public final class IOUtil {

  protected static final int BUF_SIZE = 8192;

  public static String readAsString(InputStream inputStream) throws WrapException {
    assertNotNull(inputStream);
    var reader = new BufferedReader(new InputStreamReader(inputStream, Constants.UTF_8));
    var buf = new StringBuilder(200);
    try {
      var c = CharBuffer.allocate(BUF_SIZE);
      while (reader.read(c) > -1) {
        c.flip();
        buf.append(c);
        c.clear();
      }
    } catch (IOException e) {
      throw new WrapException(e);
    } finally {
      IOUtil.close(reader);
    }
    return buf.toString();
  }

  public static String readAsString(File file) throws WrapException {
    assertNotNull(file);
    InputStream inputStream = null;
    try {
      inputStream = new FileInputStream(file);
      return readAsString(inputStream);
    } catch (FileNotFoundException e) {
      throw new WrapException(e);
    } finally {
      IOUtil.close(inputStream);
    }
  }

  public static void close(Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (IOException ignored) {
      }
    }
  }

  public static boolean endsWith(File file, String pathname) {
    var path = file.toPath();
    var other = Paths.get(pathname);
    var i = path.getNameCount() - 1;
    var j = other.getNameCount() - 1;
    for (; i >= 0 && j >= 0; i--, j--) {
      // avoid Path#equals to make comparison case sensitive
      var element = path.getName(i).toString();
      var otherElement = other.getName(j).toString();
      if (!element.equals(otherElement)) {
        return false;
      }
    }
    return true;
  }
}
