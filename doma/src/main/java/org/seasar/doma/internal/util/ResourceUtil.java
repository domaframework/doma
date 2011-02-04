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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.seasar.doma.internal.WrapException;

/**
 * @author taedium
 * 
 */
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
