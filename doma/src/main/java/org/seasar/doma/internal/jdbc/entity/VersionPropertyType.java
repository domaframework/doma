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
package org.seasar.doma.internal.jdbc.entity;

import org.seasar.doma.wrapper.NumberWrapper;

/**
 * バージョンプロパティです。
 * 
 * @author taedium
 * 
 */
public class VersionPropertyType<W extends NumberWrapper<?>> extends
        BasicPropertyType<W> {

    public VersionPropertyType(String name, String columnName, W wrapper) {
        super(name, columnName, wrapper, true, true);
    }

    @Override
    public boolean isVersion() {
        return true;
    }

    public void setIfNecessary(Number value) {
        if (wrapper.get() == null || wrapper.get().intValue() < 0) {
            wrapper.set(value);
        }
    }

    public void increment() {
        if (wrapper.get() != null) {
            int i = wrapper.get().intValue();
            wrapper.set(i + 1);
        }
    }

}
