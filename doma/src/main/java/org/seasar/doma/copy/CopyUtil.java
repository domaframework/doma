/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.doma.copy;

import java.util.Map;

import org.seasar.doma.DomaNullPointerException;

/**
 * @author taedium
 * 
 */
public final class CopyUtil {

    private static volatile CopyUtilDelegate delegate = new BuiltinCopyUtilDelegate();

    public static void setDelegate(CopyUtilDelegate delegate) {
        if (delegate == null) {
            throw new DomaNullPointerException("delegate");
        }
        CopyUtil.delegate = delegate;
    }

    public static void copy(Object src, Object dest) {
        delegate.copy(src, dest, new CopyOptions());
    }

    public static void copy(Object src, Object dest, CopyOptions copyOptions) {
        delegate.copy(src, dest, copyOptions);
    }

    public static void copy(Object src, Map<String, Object> dest) {
        delegate.copy(src, dest, new CopyOptions());
    }

    public static void copy(Object src, Map<String, Object> dest,
            CopyOptions copyOptions) {
        delegate.copy(src, dest, copyOptions);
    }

    public static void copy(Map<String, Object> src, Object dest) {
        delegate.copy(src, dest, new CopyOptions());
    }

    public static void copy(Map<String, Object> src, Object dest,
            CopyOptions copyOptions) {
        delegate.copy(src, dest, copyOptions);
    }
}
