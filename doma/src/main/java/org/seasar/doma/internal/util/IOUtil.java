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
package org.seasar.doma.internal.util;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;

import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.WrapException;

/**
 * @author taedium
 * 
 */
public final class IOUtil {

    protected static final int BUF_SIZE = 8192;

    public static String readAsString(InputStream inputStream)
            throws WrapException {
        assertNotNull(inputStream);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream, Constants.UTF_8));
        StringBuilder buf = new StringBuilder(200);
        try {
            CharBuffer c = CharBuffer.allocate(BUF_SIZE);
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

}
