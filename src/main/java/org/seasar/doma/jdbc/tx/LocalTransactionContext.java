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
package org.seasar.doma.jdbc.tx;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.seasar.doma.message.Message;

/**
 * @author taedium
 * @since 1.1.0
 */
public class LocalTransactionContext {

    private final List<String> savepointNames = new ArrayList<String>();

    private final Map<String, Savepoint> savepointMap = new HashMap<String, Savepoint>();

    private LocalTransactionConnection localTxConnection;

    private Supplier<LocalTransactionConnection> localTxConnectionSupplier;

    private String id;

    private boolean rollbackOnly;

    void begin(Supplier<LocalTransactionConnection> localTxConnectionSupplier) {
        assertNotNull(localTxConnectionSupplier);
        id = String.valueOf(System.identityHashCode(localTxConnectionSupplier));
        this.localTxConnectionSupplier = localTxConnectionSupplier;
    }

    void end() {
        id = null;
        localTxConnectionSupplier = null;
    }

    LocalTransactionConnection getConnection() {
        if (localTxConnection == null) {
            if (localTxConnectionSupplier == null) {
                throw new TransactionNotYetBegunException(Message.DOMA2048);
            }
            localTxConnection = localTxConnectionSupplier.get();
        }
        return localTxConnection;
    }

    boolean hasConnection() {
        return localTxConnection != null;
    }

    Savepoint getSavepoint(String savepointName) {
        assertNotNull(savepointName);
        return savepointMap.get(savepointName);
    }

    void addSavepoint(String savepointName, Savepoint savepoint) {
        assertNotNull(savepointName, savepoint);
        savepointNames.add(savepointName);
        savepointMap.put(savepointName, savepoint);
    }

    Savepoint releaseAndGetSavepoint(String savepointName) {
        assertNotNull(savepointName);
        Savepoint result = savepointMap.get(savepointName);
        if (result == null) {
            return null;
        }
        int pos = savepointNames.lastIndexOf(savepointName);
        if (pos > -1) {
            List<String> subList = savepointNames.subList(pos,
                    savepointNames.size());
            for (String name : subList) {
                savepointMap.remove(name);
            }
            subList.clear();
        }
        return result;
    }

    String getId() {
        return id;
    }

    void setRollbackOnly() {
        this.rollbackOnly = true;
    }

    boolean isRollbackOnly() {
        return rollbackOnly;
    }

}
