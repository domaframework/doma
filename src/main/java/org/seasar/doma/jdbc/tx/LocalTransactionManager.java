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
 * <p>
 * ローカルトランザクションを使った操作を簡易化するAPIを提供します。
 * 
 * @author nakamura-to
 * @since 2.0.0
 */
public class LocalTransactionManager {

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

    /**
     * ローカルトランザクションを返します。
     * 
     * @return ローカルトランザクション
     */
    public LocalTransaction getLocalTransaction() {
        return transaction;
    }

    /**
     * トランザクション属性がREQUIREDであるトランザクションを実行します。
     * 
     * @param block
     *            トランザクション内で実行する処理
     */
    public void required(Runnable block) {
        if (block == null) {
            throw new DomaNullPointerException("block");
        }
        requiredInternal(toSupplier(block), TransactionIsolationLevel.DEFAULT);
    }

    /**
     * トランザクション属性がREQUIREDであるトランザクションを実行します。
     * 
     * @param block
     *            トランザクション内で実行する処理
     * @param isolationLevel
     *            トランザクション分離レベル
     */
    public void required(Runnable block,
            TransactionIsolationLevel isolationLevel) {
        if (block == null) {
            throw new DomaNullPointerException("block");
        }
        if (isolationLevel == null) {
            throw new DomaNullPointerException("isolationLevel");
        }
        requiredInternal(toSupplier(block), isolationLevel);
    }

    /**
     * トランザクション属性がREQUIREDであるトランザクションを実行します。
     * 
     * @param <RESULT>
     *            結果の型
     * @param supplier
     *            トランザクション内で実行する処理
     * @return 処理の結果
     */
    public <RESULT> RESULT required(Supplier<RESULT> supplier) {
        if (supplier == null) {
            throw new DomaNullPointerException("supplier");
        }
        return requiredInternal(supplier, TransactionIsolationLevel.DEFAULT);
    }

    /**
     * トランザクション属性がREQUIREDであるトランザクションを実行します。
     * 
     * @param <RESULT>
     *            結果の型
     * @param supplier
     *            トランザクション内で実行する処理
     * @param isolationLevel
     *            トランザクション分離レベル
     * @return 処理の結果
     */
    public <RESULT> RESULT required(Supplier<RESULT> supplier,
            TransactionIsolationLevel isolationLevel) {
        if (supplier == null) {
            throw new DomaNullPointerException("supplier");
        }
        if (isolationLevel == null) {
            throw new DomaNullPointerException("isolationLevel");
        }
        return requiredInternal(supplier, isolationLevel);
    }

    protected <RESULT> RESULT requiredInternal(Supplier<RESULT> supplier,
            TransactionIsolationLevel isolationLevel) {
        assertNotNull(supplier, isolationLevel);
        if (transaction.isActive()) {
            return supplier.get();
        } else {
            return executeInTransaction(supplier, isolationLevel);
        }
    }

    /**
     * トランザクション属性がREQUIRES_NEWであるトランザクションを実行します。
     * 
     * @param block
     *            トランザクション内で実行する処理
     */
    public void requiresNew(Runnable block) {
        if (block == null) {
            throw new DomaNullPointerException("block");
        }
        requiresNewInternal(toSupplier(block),
                TransactionIsolationLevel.DEFAULT);
    }

    /**
     * トランザクション属性がREQUIRES_NEWであるトランザクションを実行します。
     * 
     * @param block
     *            トランザクション内で実行する処理
     * @param isolationLevel
     *            トランザクション分離レベル
     */
    public void requiresNew(Runnable block,
            TransactionIsolationLevel isolationLevel) {
        if (block == null) {
            throw new DomaNullPointerException("block");
        }
        if (isolationLevel == null) {
            throw new DomaNullPointerException("isolationLevel");
        }
        requiresNewInternal(toSupplier(block), isolationLevel);
    }

    /**
     * トランザクション属性がREQUIRES_NEWであるトランザクションを実行します。
     * 
     * @param <RESULT>
     *            結果の型
     * @param supplier
     *            トランザクション内で実行する処理
     * @return 処理の結果
     */
    public <RESULT> RESULT requiresNew(Supplier<RESULT> supplier) {
        if (supplier == null) {
            throw new DomaNullPointerException("supplier");
        }
        return requiresNewInternal(supplier, TransactionIsolationLevel.DEFAULT);
    }

    /**
     * トランザクション属性がREQUIRES_NEWであるトランザクションを実行します。
     * 
     * @param <RESULT>
     *            結果の型
     * @param supplier
     *            トランザクション内で実行する処理
     * @param isolationLevel
     *            トランザクション分離レベル
     * @return 処理の結果
     */
    public <RESULT> RESULT requiresNew(Supplier<RESULT> supplier,
            TransactionIsolationLevel isolationLevel) {
        if (supplier == null) {
            throw new DomaNullPointerException("supplier");
        }
        if (isolationLevel == null) {
            throw new DomaNullPointerException("isolationLevel");
        }
        return requiresNewInternal(supplier, isolationLevel);
    }

    protected <RESULT> RESULT requiresNewInternal(Supplier<RESULT> supplier,
            TransactionIsolationLevel isolationLevel) {
        assertNotNull(supplier, isolationLevel);
        if (transaction.isActive()) {
            LocalTransactionContext context = transaction.suspend();
            try {
                return executeInTransaction(supplier, isolationLevel);
            } finally {
                transaction.resume(context);
            }
        } else {
            return executeInTransaction(supplier, isolationLevel);
        }
    }

    /**
     * トランザクション属性がNOT_SUPPORTEDであるトランザクションを実行します。
     * 
     * @param block
     *            トランザクション内で実行する処理
     */
    public void notSupported(Runnable block) {
        if (block == null) {
            throw new DomaNullPointerException("block");
        }
        notSupportedInternal(toSupplier(block),
                TransactionIsolationLevel.DEFAULT);
    }

    /**
     * トランザクション属性がNOT_SUPPORTEDであるトランザクションを実行します。
     * 
     * @param block
     *            トランザクション内で実行する処理
     * @param isolationLevel
     *            トランザクション分離レベル
     */
    public void notSupported(Runnable block,
            TransactionIsolationLevel isolationLevel) {
        if (block == null) {
            throw new DomaNullPointerException("block");
        }
        if (isolationLevel == null) {
            throw new DomaNullPointerException("isolationLevel");
        }
        notSupportedInternal(toSupplier(block), isolationLevel);
    }

    /**
     * トランザクション属性がNOT_SUPPORTEDであるトランザクションを実行します。
     * 
     * @param <RESULT>
     *            結果の型
     * @param supplier
     *            トランザクション内で実行する処理
     * @return 処理の結果
     */
    public <RESULT> RESULT notSupported(Supplier<RESULT> supplier) {
        if (supplier == null) {
            throw new DomaNullPointerException("supplier");
        }
        return notSupportedInternal(supplier, TransactionIsolationLevel.DEFAULT);
    }

    /**
     * トランザクション属性がNOT_SUPPORTEDであるトランザクションを実行します。
     * 
     * @param <RESULT>
     *            結果の型
     * @param supplier
     *            トランザクション内で実行する処理
     * @param isolationLevel
     *            トランザクション分離レベル
     * @return 処理の結果
     */
    public <RESULT> RESULT notSupported(Supplier<RESULT> supplier,
            TransactionIsolationLevel isolationLevel) {
        if (supplier == null) {
            throw new DomaNullPointerException("supplier");
        }
        if (isolationLevel == null) {
            throw new DomaNullPointerException("isolationLevel");
        }
        return notSupportedInternal(supplier, isolationLevel);
    }

    protected <RESULT> RESULT notSupportedInternal(Supplier<RESULT> supplier,
            TransactionIsolationLevel isolationLevel) {
        assertNotNull(supplier, isolationLevel);
        if (transaction.isActive()) {
            LocalTransactionContext context = transaction.suspend();
            try {
                return executeInTransaction(supplier, isolationLevel);
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

    /**
     * 現在のトランザクションをロールバックすることを予約します。
     */
    public void setRollbackOnly() {
        transaction.setRollbackOnly();
    }

    /**
     * 現在のトランザクションがロールバックされるように予約されているかどうかを返します。
     * 
     * @return ロールバックされる場合 {@code true}
     */
    public boolean isRollbackOnly() {
        return transaction.isRollbackOnly();
    }

    /**
     * トランザクション内で実行します。
     * 
     * @param <RESULT>
     *            結果の型
     * @param supplier
     *            トランザクション内で実行する処理
     * @param isolationLevel
     *            トランザクション分離レベル
     * @return 処理の結果
     */
    protected <RESULT> RESULT executeInTransaction(Supplier<RESULT> supplier,
            TransactionIsolationLevel isolationLevel) {
        assertNotNull(supplier, isolationLevel);
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

}
