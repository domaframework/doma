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

import org.seasar.doma.DomaNullPointerException;

/**
 * {@literal REQUIRES_NEW} のトランザクション属性について何ら制御を行わない
 * {@link RequiresNewController}の実装です。
 * <p>
 * 通常は、このクラスを使用せず、環境にあわせた実装を作成し使用してください。
 * 
 * @author taedium
 * 
 */
public class NullRequiresNewController implements RequiresNewController {

    @Override
    public <R> R requiresNew(Callback<R> callback) throws Throwable {
        if (callback == null) {
            throw new DomaNullPointerException("callback");
        }
        return callback.execute();
    }

}
