package org.seasar.doma.internal.util;

import static org.seasar.doma.internal.util.Assertions.*;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import org.seasar.doma.internal.WrapException;


/**
 * @author taedium
 * 
 */
public final class IOs {

    protected static final int BUF_SIZE = 8192;

    public static String readAsString(InputStream inputStream)
            throws WrapException {
        assertNotNull(inputStream);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream, Charset.forName("UTF-8")));
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
            IOs.close(reader);
        }
        return buf.toString();
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
