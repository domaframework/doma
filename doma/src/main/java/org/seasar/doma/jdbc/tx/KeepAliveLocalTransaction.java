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

import javax.sql.DataSource;

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.JdbcLogger;

/**
 * 明示的に破棄されるまでJDBC接続を維持し続けるローカルトランザクションです。
 * <p>
 * ただし、例外が発生した場合、接続は閉じられます。
 * <p>
 * このクラスはスレッドセーフです。
 * <p>
 * 
 * <pre>
 * KeepAliveLocalTransaction tx = AppConfig.getKeepAliveLocalTransaction();
 * tx.init();
 * try {
 *     try {
 *         tx.begin();
 *         Employee employee = dao.selectById(1);
 *         employee.setName(&quot;hoge&quot;);
 *         employee.setJobType(JobType.PRESIDENT);
 *         dao.update(employee);
 *         tx.commit();
 *     } finally {
 *         tx.rollback();
 *     }
 *     try {
 *         tx.begin();
 *         Employee employee = dao.selectById(2);
 *         employee.setName(&quot;foo&quot;);
 *         employee.setJobType(JobType.SALESMAN);
 *         dao.update(employee);
 *         tx.commit();
 *     } finally {
 *         tx.rollback();
 *     }
 * } finally {
 *     tx.destroy();
 * }
 * </pre>
 * 
 * @author taedium
 * @since 1.21.0
 */
public class KeepAliveLocalTransaction extends LocalTransaction {

    /**
     * インスタンスを構築します。
     * 
     * @param dataSource
     *            データソース
     * @param localTxContextHolder
     *            ローカルトランザクションコンテキストのホルダー
     * @param jdbcLogger
     *            JDBCに関するロガー
     */
    protected KeepAliveLocalTransaction(DataSource dataSource,
            ThreadLocal<LocalTransactionContext> localTxContextHolder,
            JdbcLogger jdbcLogger) {
        super(dataSource, localTxContextHolder, jdbcLogger);
    }

    /**
     * デフォルトのトランザクション分離レベルを指定してインスタンスを構築します。
     * 
     * @param dataSource
     *            データソース
     * @param localTxContextHolder
     *            ローカルトランザクションコンテキストのホルダー
     * @param jdbcLogger
     *            JDBCに関するロガー
     * @param defaultTransactionIsolationLevel
     *            デフォルトのトランザクション分離レベル
     */
    protected KeepAliveLocalTransaction(DataSource dataSource,
            ThreadLocal<LocalTransactionContext> localTxContextHolder,
            JdbcLogger jdbcLogger,
            TransactionIsolationLevel defaultTransactionIsolationLevel) {
        super(dataSource, localTxContextHolder, jdbcLogger,
                defaultTransactionIsolationLevel);
    }

    /**
     * トランザクションコンテキストを初期化します。
     * <p>
     * この操作によりJDBCの接続が確立されます。
     * <p>
     * このメソッドを呼び出さずに最初の {@link #begin()} を呼び出した場合、その時点でJDBCの接続が確立されます。
     * 
     * @throws JdbcException
     *             JDBCの接続に失敗した場合
     */
    public void init() {
        createLocalTransactionContext();
    }

    @Override
    protected LocalTransactionContext createLocalTransactionContext() {
        LocalTransactionContext context = localTxContextHolder.get();
        if (context != null) {
            return context;
        }
        return super.createLocalTransactionContext();
    }

    /**
     * トランザクションコンテキストを破棄します。
     * <p>
     * この操作によりJDBCの接続が閉じられます。
     * <p>
     * このメソッドは、実行時例外をスローしません。
     */
    public void destroy() {
        LocalTransactionContext context = localTxContextHolder.get();
        if (context == null) {
            return;
        }
        release(context, "destroy");
    }

    /**
     * トランザクションを終了しますがJDBCの接続は閉じません。
     */
    @Override
    protected void endInternal(LocalTransactionContext context,
            String callerMethodName) {
        jdbcLogger.logLocalTransactionEnded(className, callerMethodName,
                context.getId());
        context.setId(null);
    }

    @Override
    protected boolean isActiveInternal(LocalTransactionContext context) {
        return context != null && context.getId() != null;
    }
}
