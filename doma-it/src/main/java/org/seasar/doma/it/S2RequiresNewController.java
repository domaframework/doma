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
package org.seasar.doma.it;

import org.seasar.doma.jdbc.RequiresNewController;
import org.seasar.extension.tx.TransactionCallback;
import org.seasar.extension.tx.TransactionManagerAdapter;
import org.seasar.framework.container.SingletonS2Container;

public class S2RequiresNewController implements RequiresNewController {

    @SuppressWarnings("unchecked")
    @Override
    public <R> R requiresNew(final Callback<R> callback) throws Throwable {
        TransactionManagerAdapter txAdapter = SingletonS2Container
                .getComponent(TransactionManagerAdapter.class);
        Object result = txAdapter.requiresNew(new TransactionCallback() {

            public Object execute(final TransactionManagerAdapter adapter)
                    throws Throwable {
                return callback.execute();
            }

        });
        return (R) result;
    }
}
