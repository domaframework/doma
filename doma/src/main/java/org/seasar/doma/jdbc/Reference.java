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
package org.seasar.doma.jdbc;

/**
 * 値への参照を表します。
 * <p>
 * ストアドプロシージャーやストアドファンクションのOUTパラメーターとIN_OUTパラメーターとして使用されます。
 * 
 * @author taedium
 * 
 * @param <V>
 *            値の型
 */
public class Reference<V> {

    private V value;

    public Reference() {
    }

    public Reference(V value) {
        this.value = value;
    }

    public V get() {
        return value;
    }

    public void set(V value) {
        this.value = value;
    }

}
