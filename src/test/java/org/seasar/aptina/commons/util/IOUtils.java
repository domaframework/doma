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
package org.seasar.aptina.commons.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import static org.seasar.aptina.commons.util.AssertionUtils.*;

/**
 * 入出力を扱うユーティリティです．
 * 
 * @author koichik
 */
public class IOUtils {

    private IOUtils() {
    }

    /**
     * クローズ可能なオブジェクトをクローズします．
     * <p>
     * 例外が発生しても無視します．
     * </p>
     * 
     * @param closeable
     *            クローズ可能なオブジェクト
     */
    public static void closeSilently(final Closeable closeable) {
        try {
            closeable.close();
        } catch (final IOException ignore) {
        }
    }

    /**
     * ファイルから読み込んだ内容を文字列で返します．
     * <p>
     * ファイルの内容はプラットフォームデフォルトの文字セットで文字列化されます．
     * </p>
     * 
     * @param file
     *            ファイル
     * @return ファイルから読み込んだ内容の文字列
     * @throws IOException
     *             入出力例外が発生した場合
     */
    public static String readString(final File file) throws IOException {
        return new String(readBytes(file));
    }

    /**
     * ファイルから読み込んだ内容を文字列で返します．
     * 
     * @param file
     *            ファイル
     * @param charset
     *            文字セット
     * @return ファイルから読み込んだ内容の文字列
     * @throws IOException
     *             入出力例外が発生した場合
     */
    public static String readString(final File file, final Charset charset)
            throws IOException {
        return new String(readBytes(file), charset);
    }

    /**
     * ファイルから読み込んだ内容を文字列で返します．
     * 
     * @param file
     *            ファイル
     * @param charsetName
     *            文字セットの名前
     * @return ファイルから読み込んだ内容の文字列
     * @throws IOException
     *             入出力例外が発生した場合
     */
    public static String readString(final File file, final String charsetName)
            throws IOException {
        return readString(file, Charset.forName(charsetName));
    }

    /**
     * 入力バイトストリームから読み込んだ文字列を返します．
     * <p>
     * このメソッドは入力バイトストリームからブロックしないで読み込める長さの文字列だけを読み込みます．
     * 入力バイトストリームの内容はプラットフォームデフォルトの文字セットで文字列化されます． 入力ストリームはクローズされません．
     * </p>
     * 
     * @param is
     *            入力バイトストリーム
     * @return 入力バイトストリームから読み込んだ文字列
     * @throws IOException
     *             入出力例外が発生した場合
     */
    public static String readString(final InputStream is) throws IOException {
        return new String(readBytes(is));
    }

    /**
     * 入力バイトストリームから読み込んだ文字列を返します．
     * <p>
     * このメソッドは入力バイトストリームからブロックしないで読み込める長さの文字列だけを読み込みます． 入力ストリームはクローズされません．
     * </p>
     * 
     * @param is
     *            入力バイトストリーム
     * @param charset
     *            文字セット
     * @return 入力バイトストリームから読み込んだ文字列
     * @throws IOException
     *             入出力例外が発生した場合
     */
    public static String readString(final InputStream is, final Charset charset)
            throws IOException {
        return new String(readBytes(is), charset);
    }

    /**
     * 入力バイトストリームから読み込んだ文字列を返します．
     * <p>
     * このメソッドは入力バイトストリームからブロックしないで読み込める長さの文字列だけを読み込みます． 入力ストリームはクローズされません．
     * </p>
     * 
     * @param is
     *            入力バイトストリーム
     * @param charsetName
     *            文字セットの名前
     * @return 入力バイトストリームから読み込んだ文字列
     * @throws IOException
     *             入出力例外が発生した場合
     */
    public static String readString(final InputStream is,
            final String charsetName) throws IOException {
        return readString(is, Charset.forName(charsetName));
    }

    /**
     * ファイルから読み込んだ内容をバイト列で返します．
     * 
     * @param file
     *            ファイル
     * @return ファイルから読み込んだ内容のバイト列
     * @throws IOException
     *             入出力例外が発生した場合
     */
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

    /**
     * 入力バイトストリームから読み込んだバイト列を返します．
     * <p>
     * このメソッドは入力バイトストリームからブロックしないで読み込める長さのバイト列だけを読み込みます． 入力ストリームはクローズされません．
     * </p>
     * 
     * @param is
     *            入力バイトストリーム
     * @return 入力バイトストリームから読み込んだバイト列
     * @throws IOException
     *             入出力例外が発生した場合
     */
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
