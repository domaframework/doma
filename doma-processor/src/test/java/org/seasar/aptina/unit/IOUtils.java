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
package org.seasar.aptina.unit;

import static org.seasar.aptina.unit.AssertionUtils.assertEquals;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/** @author koichik */
class IOUtils {

  private IOUtils() {}

  public static void closeSilently(final Closeable closeable) {
    try {
      closeable.close();
    } catch (final IOException ignore) {
    }
  }

  public static String readString(final File file) throws IOException {
    return new String(readBytes(file));
  }

  public static String readString(final File file, final Charset charset) throws IOException {
    return new String(readBytes(file), charset);
  }

  public static String readString(final File file, final String charsetName) throws IOException {
    return readString(file, Charset.forName(charsetName));
  }

  public static String readString(final InputStream is) throws IOException {
    return new String(readBytes(is));
  }

  public static String readString(final InputStream is, final Charset charset) throws IOException {
    return new String(readBytes(is), charset);
  }

  public static String readString(final InputStream is, final String charsetName)
      throws IOException {
    return readString(is, Charset.forName(charsetName));
  }

  public static byte[] readBytes(final File file) throws IOException {
    if (!file.exists()) {
      throw new FileNotFoundException(file.getPath());
    }
    final FileInputStream is = new FileInputStream(file);
    try {
      final FileChannel channel = is.getChannel();
      final int size = (int) channel.size();
      final ByteBuffer buffer = ByteBuffer.allocate(size);
      final int readSize = channel.read(buffer);
      assertEquals(size, readSize);
      return buffer.array();
    } finally {
      closeSilently(is);
    }
  }

  public static byte[] readBytes(final InputStream is) throws IOException {
    final int size = is.available();
    final byte[] bytes = new byte[size];
    int readSize = 0;
    while (readSize < size) {
      readSize += is.read(bytes, readSize, size - readSize);
    }
    return bytes;
  }
}
