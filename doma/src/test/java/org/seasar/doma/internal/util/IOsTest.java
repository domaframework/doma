package org.seasar.doma.internal.util;

import java.io.Closeable;
import java.io.IOException;

import org.seasar.doma.internal.util.IOs;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class IOsTest extends TestCase {

    public void test() throws Exception {
        IOs.close(new Closeable() {

            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        });
    }
}
