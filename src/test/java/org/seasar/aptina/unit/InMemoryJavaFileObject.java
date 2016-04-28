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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.Charset;

import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;

import org.seasar.aptina.commons.util.IOUtils;

/**
 * 生成されたリソースをメモリ上に保持する{@link JavaFileObject}の実装です。
 * 
 * @author koichik
 */
class InMemoryJavaFileObject extends SimpleJavaFileObject {

    byte[] content;

    ByteArrayOutputStream outputStream;

    final Charset charset;

    /**
     * インスタンスを構築します。
     * 
     * @param uri
     *            ファイルオブジェクトの{@link URI}
     * @param kind
     *            ファイルオブジェクトの種類
     * @param charset
     *            文字セット
     */
    public InMemoryJavaFileObject(final URI uri, final Kind kind,
            final Charset charset) {
        super(uri, kind);
        this.charset = charset;
    }

    /**
     * インスタンスを構築します。
     * 
     * @param uri
     *            ファイルオブジェクトの{@link URI}
     * @param kind
     *            ファイルオブジェクトの種類
     * @param charset
     *            文字セット
     * @param content
     *            ファイルの内容
     */
    public InMemoryJavaFileObject(final URI uri, final Kind kind,
            final Charset charset, final byte[] content) {
        super(uri, kind);
        this.charset = charset;
        this.content = content;
    }

    @Override
    public InputStream openInputStream() throws IOException {
        return new ByteArrayInputStream(content != null ? content
                : outputStream != null ? outputStream.toByteArray()
                        : new byte[0]);
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        content = null;
        outputStream = new ByteArrayOutputStream(1024);
        return outputStream;
    }

    @Override
    public Writer openWriter() throws IOException {
        return new OutputStreamWriter(openOutputStream(), charset);
    }

    @Override
    public CharSequence getCharContent(final boolean ignoreEncodingErrors)
            throws IOException {
        return new String(
            IOUtils.readBytes(openInputStream()),
            charset == null ? Charset.defaultCharset() : charset);
    }

}
