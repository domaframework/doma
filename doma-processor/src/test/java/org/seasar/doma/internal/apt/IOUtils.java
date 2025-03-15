/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.apt;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
/**
 * This class was forked from <a
 * href="https://github.com/seasarorg/aptina/tree/1.0.0/aptina/aptina-unit">aptina-unit</a>.
 *
 * @author koichik
 */
class IOUtils {

  private IOUtils() {}

  public static void closeSilently(final Closeable closeable) {
    try {
      closeable.close();
    } catch (final IOException ignore) {
    }
  }

  public static String readString(final InputStream is, final Charset charset) throws IOException {
    return new String(readBytes(is), charset);
  }

  private static byte[] readBytes(final InputStream is) throws IOException {
    final int size = is.available();
    final byte[] bytes = new byte[size];
    int readSize = 0;
    while (readSize < size) {
      readSize += is.read(bytes, readSize, size - readSize);
    }
    return bytes;
  }
}
