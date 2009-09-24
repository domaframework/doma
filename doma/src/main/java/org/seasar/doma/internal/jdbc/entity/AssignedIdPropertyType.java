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

import org.seasar.doma.wrapper.Wrapper;

/**
 * アプリケーションにより割り当てられる識別子のプロパティです。
 * 
 * @author taedium
 * 
 */
public class AssignedIdPropertyType<W extends Wrapper<?>> extends
        BasicPropertyType<W> {

    /**
     * インスタンスを構築します。
     * 
     * @param name
     *            名前
     * @param columnName
     *            カラム名
     * @param wrapper
     *            ドメイン
     */
    public AssignedIdPropertyType(String name, String columnName, W wrapper) {
        super(name, columnName, wrapper, true, true);
    }

    @Override
    public boolean isId() {
        return true;
    }

}
