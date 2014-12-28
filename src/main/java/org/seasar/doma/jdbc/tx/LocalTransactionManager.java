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

import java.util.function.Supplier;

import org.seasar.doma.DomaNullPointerException;

/**
 * ローカルトランザクションのマネージャーです。
 * 
 * @author nakamura-to
 * @since 2.0.0
 */
public class LocalTransactionManager implements TransactionManager {

    /**
     * ローカルトランザクション
     */
    protected final LocalTransaction transaction;

    /**
     * インスタンスを構築します。
     * 
     * @param transaction
     *            ローカルトランザクション
     */
    public LocalTransactionManager(LocalTransaction transaction) {
        if (transaction == null) {
            throw new DomaNullPointerException("transaction");
        }
        this.transaction = transaction;
    }

    @Override
    public void required(Runnable block) {
        if (block == null) {
            throw new DomaNullPointerException("block");
        }
        requiredInternal(TransactionIsolationLevel.DEFAULT, toSupplier(block));
    }

    @Override
    public void required(TransactionIsolationLevel isolationLevel,
            Runnable block) {
        if (isolationLevel == null) {
            throw new DomaNullPointerException("isolationLevel");
        }
        if (block == null) {
            throw new DomaNullPointerException("block");
        }
        requiredInternal(isolationLevel, toSupplier(block));
    }

    @Override
    public <RESULT> RESULT required(Supplier<RESULT> supplier) {
        if (supplier == null) {
            throw new DomaNullPointerException("supplier");
        }
        return requiredInternal(TransactionIsolationLevel.DEFAULT, supplier);
    }

    @Override
    public <RESULT> RESULT required(TransactionIsolationLevel isolationLevel,
            Supplier<RESULT> supplier) {
        if (isolationLevel == null) {
            throw new DomaNullPointerException("isolationLevel");
        }
        if (supplier == null) {
            throw new DomaNullPointerException("supplier");
        }
        return requiredInternal(isolationLevel, supplier);
    }

    protected <RESULT> RESULT requiredInternal(
            TransactionIsolationLevel isolationLevel, Supplier<RESULT> supplier) {
        assertNotNull(isolationLevel, supplier);
        if (transaction.isActive()) {
            return supplier.get();
        } else {
            return executeInTransaction(isolationLevel, supplier);
        }
    }

    @Override
    public void requiresNew(Runnable block) {
        if (block == null) {
            throw new DomaNullPointerException("block");
        }
        requiresNewInternal(TransactionIsolationLevel.DEFAULT,
                toSupplier(block));
    }

    @Override
    public void requiresNew(TransactionIsolationLevel isolationLevel,
            Runnable block) {
        if (isolationLevel == null) {
            throw new DomaNullPointerException("isolationLevel");
        }
        if (block == null) {
            throw new DomaNullPointerException("block");
        }
        requiresNewInternal(isolationLevel, toSupplier(block));
    }

    @Override
    public <RESULT> RESULT requiresNew(Supplier<RESULT> supplier) {
        if (supplier == null) {
            throw new DomaNullPointerException("supplier");
        }
        return requiresNewInternal(TransactionIsolationLevel.DEFAULT, supplier);
    }

    @Override
    public <RESULT> RESULT requiresNew(
            TransactionIsolationLevel isolationLevel, Supplier<RESULT> supplier) {
        if (isolationLevel == null) {
            throw new DomaNullPointerException("isolationLevel");
        }
        if (supplier == null) {
            throw new DomaNullPointerException("supplier");
        }
        return requiresNewInternal(isolationLevel, supplier);
    }

    protected <RESULT> RESULT requiresNewInternal(
            TransactionIsolationLevel isolationLevel, Supplier<RESULT> supplier) {
        assertNotNull(isolationLevel, supplier);
        if (transaction.isActive()) {
            LocalTransactionContext context = transaction.suspend();
            try {
                return executeInTransaction(isolationLevel, supplier);
            } finally {
                transaction.resume(context);
            }
        } else {
            return executeInTransaction(isolationLevel, supplier);
        }
    }

    @Override
    public void notSupported(Runnable block) {
        if (block == null) {
            throw new DomaNullPointerException("block");
        }
        notSupportedInternal(TransactionIsolationLevel.DEFAULT,
                toSupplier(block));
    }

    @Override
    public void notSupported(TransactionIsolationLevel isolationLevel,
            Runnable block) {
        if (isolationLevel == null) {
            throw new DomaNullPointerException("isolationLevel");
        }
        if (block == null) {
            throw new DomaNullPointerException("block");
        }
        notSupportedInternal(isolationLevel, toSupplier(block));
    }

    @Override
    public <RESULT> RESULT notSupported(Supplier<RESULT> supplier) {
        if (supplier == null) {
            throw new DomaNullPointerException("supplier");
        }
        return notSupportedInternal(TransactionIsolationLevel.DEFAULT, supplier);
    }

    @Override
    public <RESULT> RESULT notSupported(
            TransactionIsolationLevel isolationLevel, Supplier<RESULT> supplier) {
        if (isolationLevel == null) {
            throw new DomaNullPointerException("isolationLevel");
        }
        if (supplier == null) {
            throw new DomaNullPointerException("supplier");
        }
        return notSupportedInternal(isolationLevel, supplier);
    }

    protected <RESULT> RESULT notSupportedInternal(
            TransactionIsolationLevel isolationLevel, Supplier<RESULT> supplier) {
        assertNotNull(isolationLevel, supplier);
        if (transaction.isActive()) {
            LocalTransactionContext context = transaction.suspend();
            try {
                return supplier.get();
            } finally {
                transaction.resume(context);
            }
        } else {
            return supplier.get();
        }
    }

    protected Supplier<Void> toSupplier(Runnable block) {
        return () -> {
            block.run();
            return null;
        };
    }

    @Override
    public void setRollbackOnly() {
        transaction.setRollbackOnly();
    }

    @Override
    public boolean isRollbackOnly() {
        return transaction.isRollbackOnly();
    }

    /**
     * トランザクション内で実行します。
     * 
     * @param isolationLevel
     *            トランザクション分離レベル
     * @param supplier
     *            トランザクション内で実行する処理
     * @param <RESULT>
     *            結果の型
     * @return 処理の結果
     */
    protected <RESULT> RESULT executeInTransaction(
            TransactionIsolationLevel isolationLevel, Supplier<RESULT> supplier) {
        assertNotNull(isolationLevel, supplier);
        transaction.begin(isolationLevel);
        try {
            RESULT result = supplier.get();
            if (!transaction.isRollbackOnly()) {
                transaction.commit();
            }
            return result;
        } finally {
            transaction.rollback();
        }
    }

    @Override
    public void setSavepoint(String savepointName) {
        transaction.setSavepoint(savepointName);
    }

    @Override
    public boolean hasSavepoint(String savepointName) {
        return transaction.hasSavepoint(savepointName);
    }

    @Override
    public void releaseSavepoint(String savepointName) {
        transaction.releaseSavepoint(savepointName);
    }

    @Override
    public void rollback(String savepointName) {
        transaction.rollback(savepointName);
    }

}
