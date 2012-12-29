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

import org.seasar.doma.internal.WrapException;

/**
 * デフォルトの {@link ClassHelper} の実装です。
 * 
 * @author taedium
 * @since 1.27.0
 */
public class DefaultClassHelper implements ClassHelper {

    /**
     * このメソッドは {@link Class#forName(String)} を用いてクラスを取得します。
     */
    @Override
    public <T> Class<T> forName(String className) throws Exception {
        try {
            @SuppressWarnings("unchecked")
            Class<T> clazz = (Class<T>) Class.forName(className);
            return clazz;
        } catch (ClassNotFoundException e) {
            throw new WrapException(e);
        }
    }

}
