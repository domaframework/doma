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
package org.seasar.doma.jdbc.builder;

import java.sql.Statement;

import java.util.function.BiConsumer;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.UniqueConstraintException;
import org.seasar.doma.jdbc.command.BatchInsertCommand;
import org.seasar.doma.jdbc.query.SqlBatchInsertQuery;

/**
 * INSERT文を組み立てバッチ実行するクラスです。
 * <p>
 * このクラスはスレッドセーフではありません。
 *
 * <h3>例</h3>
 * <h4>Java</h4>
 *
 * <pre>
 * List&lt;Employee&gt; employees = Arrays.asList(new Employee[] {
 *     new Employee(&quot;SMITH&quot;, 100),
 *     new Employee(&quot;ALLEN&quot;, 200)
 * });
 * BatchInsertExecutor executor = BatchInsertExecutor.newInstance(config);
 * executor.batchSize(10);
 * executor.execute(employees, (emp, builder) -&gt; {
 *     builder.sql(&quot;insert into Emp&quot;);
 *     builder.sql(&quot;(name, salary)&quot;);
 *     builder.sql(&quot;values (&quot;);
 *     builder.param(String.class, emp.name).sql(&quot;, &quot;);
 *     builder.param(int.class, emp.salary).sql(&quot;)&quot;);
 * });
 * </pre>
 *
 * <h4>実行されるSQL</h4>
 *
 * <pre>
 * insert into Emp
 * (name, salary)
 * values('SMITH', 100)
 *
 * insert into Emp
 * (name, salary)
 * values('ALLEN', 200)
 * </pre>
 *
 *
 * @author bakenezumi
 * @since 2.13.1
 */
public class BatchInsertExecutor {

    private final Config config;
    private int queryTimeout = -1;
    private SqlLogType sqlLogType = null;
    private int batchSize = -1;
    private String className = null;
    private String methodName = null;

    private BatchInsertExecutor(Config config) {
        this.config = config;
    }

    /**
     * ファクトリメソッドです。
     *
     * @param config
     *            設定
     * @return INSERT文をバッチ実行するビルダー
     * @throws DomaNullPointerException
     *             引数が{@code null} の場合
     */
    public static BatchInsertExecutor newInstance(Config config) {
        if (config == null) {
            throw new DomaNullPointerException("config");
        }
        return new BatchInsertExecutor(config);
    }

    /**
     * クエリタイムアウト（秒）を設定します。
     * <p>
     * 指定しない場合、 {@link Config#getQueryTimeout()} が使用されます。
     *
     * @param queryTimeout
     *            クエリタイムアウト（秒）
     * @see Statement#setQueryTimeout(int)
     */
    public void queryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
    }

    /**
     * SQLのログの出力形式を設定します。
     *
     * @param sqlLogType
     *            SQLのログの出力形式
     */
    public void sqlLogType(SqlLogType sqlLogType) {
        if (sqlLogType == null) {
            throw new DomaNullPointerException("sqlLogType");
        }
        this.sqlLogType = sqlLogType;
    }

    /**
     * バッチサイズを設定します。
     * <p>
     * 指定しない場合、 {@link Config#getBatchSize()} が使用されます。
     *
     * @param batchSize
     *            バッチサイズ
     */
    public void batchSize(int batchSize) {
        this.batchSize = batchSize;
    }
 
    /**
     * 呼び出し元のクラス名です。
     * <p>
     * 指定しない場合このクラスの名前が使用されます。
     *
     * @param className
     *            呼び出し元のクラス名
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     */
    public void callerClassName(String className) {
        if (className == null) {
            throw new DomaNullPointerException("className");
        }
        this.className = className;
    }

    /**
     * 呼び出し元のメソッド名です。
     * <p>
     * 指定しない場合このSQLを生成するメソッド（{@link #execute})）の名前が使用されます。
     *
     * @param methodName
     *            呼び出し元のメソッド名
     * @throws DomaNullPointerException
     *             引数が {@code null} の場合
     */
    public void callerMethodName(String methodName) {
        if (methodName == null) {
            throw new DomaNullPointerException("methodName");
        }
        this.methodName = methodName;
    }

    /**
     * SQLを実行します。
     *
     * @param <P>
     *            パラメータの型
     * @param params
     *             1要素が1クエリのパラメータの元となるIterableなもの
     * @param buildConsumer
     *             BatchInsertBuilderを使って1回分のクエリを組み立てるラムダ式。
     *             第一パラメータには params の要素が、
     *             第二パラメータには {@link BatchInsertBuilder} のインスタンスが渡ります。
     * @return 更新された件数の配列
     *             戻り値の配列の要素の数はパラメータのparamsの要素の数と等しくなります。
     *             配列のそれぞれの要素が更新された件数を返します。
     * @throws DomaNullPointerException
     *             引数が{@code null} の場合
     * @throws UniqueConstraintException
     *             一意制約違反が発生した場合
     * @throws JdbcException
     *             上記以外でJDBCに関する例外が発生した場合
     */
    public <P> int[] execute(Iterable<P> params, BiConsumer<P, BatchInsertBuilder> buildConsumer) {
        if (params == null) {
            throw new DomaNullPointerException("params");
        }
        if (buildConsumer == null) {
            throw new DomaNullPointerException("buildConsumer");
        }
        BatchInsertBuilder builder = BatchInsertBuilder.newInstance(config);
        if (queryTimeout > 0) {
            builder.queryTimeout(queryTimeout);
        }
        if (sqlLogType != null) {
            builder.queryTimeout(queryTimeout);
        }
        if (batchSize > 0) {
            builder.batchSize(batchSize);
        }
        if (className != null) {
            builder.batchSize(batchSize);
        }
        if (methodName != null) {
            builder.batchSize(batchSize);
        }                
        for (P p : params) {
            buildConsumer.accept(p, builder);
            builder = builder.fixSql();
        }
        return builder.execute();
    }

    /**
     * {@link BatchInsertExecutor#execute} で利用される、INSERT文を組み立てるクラスです。
     */
    public static abstract class BatchInsertBuilder {

        final BatchBuildingHelper helper;

        final SqlBatchInsertQuery query;

        final ParamIndex paramIndex;

        final Map<Integer, BatchParam<?>> paramMap;

        BatchInsertBuilder(Config config) {
            this.helper = new BatchBuildingHelper();
            this.query = new SqlBatchInsertQuery();
            this.query.setConfig(config);
            this.query.setCallerClassName(getClass().getName());
            this.query.setSqlLogType(SqlLogType.FORMATTED);
            this.paramIndex = new ParamIndex();
            this.paramMap = new LinkedHashMap<>();
        }

        BatchInsertBuilder(BatchBuildingHelper builder, SqlBatchInsertQuery query,
                           ParamIndex paramIndex, Map<Integer, BatchParam<?>> paramMap) {
            this.helper = builder;
            this.query = query;
            this.paramIndex = paramIndex;
            this.paramMap = paramMap;
        }

        static BatchInsertBuilder newInstance(Config config) {
            if (config == null) {
                throw new DomaNullPointerException("config");
            }
            return new InitialBatchInsertBuilder(config);
        }

        void queryTimeout(int queryTimeout) {
            query.setQueryTimeout(queryTimeout);
        }

        void sqlLogType(SqlLogType sqlLogType) {
            if (sqlLogType == null) {
                throw new DomaNullPointerException("sqlLogType");
            }
            query.setSqlLogType(sqlLogType);
        }

        void batchSize(int batchSize) {
            query.setBatchSize(batchSize);
        }

        /**
         * SQLの断片を追加します。
         *
         * @param sql
         *            SQLの断片
         * @return このインスタンス
         * @throws DomaNullPointerException
         *             引数が {@code null} の場合
         */
        public abstract BatchInsertBuilder sql(String sql);

        /**
         * 最後に追加したSQLもしくはパラメータを削除します。
         *
         * @return このインスタンス
         */
        public abstract BatchInsertBuilder removeLast();

        /**
         * パラメータを追加します。
         * <p>
         * パラメータの型には基本型かドメインクラスを要素とする、
         * {@link java.lang.Iterable} のサブタイプを指定できます。
         *
         * @param <P>
         *            パラメータの型
         * @param paramClass
         *            パラメータの要素のクラス
         * @param param
         *            パラメータ
         * @return このインスタンス
         * @throws DomaNullPointerException
         *             {@code paramClass} が {@code null} の場合
         */
        public <P> BatchInsertBuilder param(Class<P> paramClass, P param) {
            if (paramClass == null) {
                throw new DomaNullPointerException("paramClass");
            }
            return appendParam(paramClass, param, false);
        }

        /**
         * リテラルとしてパラメータを追加します。
         * <p>
         * パラメータの型には基本型かドメインクラスを要素とする、
         * {@link java.lang.Iterable} のサブタイプを指定できます。
         *
         * @param <P>
         *            パラメータの型
         * @param paramClass
         *            パラメータのクラス
         * @param param
         *            パラメータ
         * @return このインスタンス
         * @throws DomaNullPointerException
         *             {@code paramClass} が {@code null} の場合
         */
        public <P> BatchInsertBuilder literal(Class<P> paramClass, P param) {
            if (paramClass == null) {
                throw new DomaNullPointerException("paramClass");
            }
            return appendParam(paramClass, param, true);
        }

        abstract <P> BatchInsertBuilder appendParam(Class<P> paramClass, P param, boolean literal);

        BatchInsertBuilder fixSql() {
            return new FixedBatchInsertBuilder(helper, query, paramMap);
        }

        private void prepare() {
            query.clearParameters();
            for (BatchParam p : helper.getParams()) {
                query.addParameter(p.name, p.paramClass, p.params);
            }
            query.setSqlNode(helper.getSqlNode());
            query.prepare();
        }

        int[] execute() {
            if (query.getMethodName() == null) {
                query.setCallerMethodName("execute");
            }
            prepare();
            BatchInsertCommand command = new BatchInsertCommand(query);
            int[] result = command.execute();
            query.complete();
            return result;
        }

        void callerClassName(String className) {
            query.setCallerClassName(className);
        }

        void callerMethodName(String methodName) {
            query.setCallerMethodName(methodName);
        }

        List<? extends Sql<?>> getSqls() {
            if (query.getMethodName() == null) {
                query.setCallerMethodName("getSqls");
            }
            prepare();
            return query.getSqls();
        }
    }

    private static class InitialBatchInsertBuilder extends BatchInsertBuilder {

        private InitialBatchInsertBuilder(Config config) {
            super(config);
        }

        private InitialBatchInsertBuilder(BatchBuildingHelper builder, SqlBatchInsertQuery query,
                                          ParamIndex paramIndex, Map<Integer, BatchParam<?>> paramMap) {
            super(builder, query, paramIndex, paramMap);
        }

        @Override
        public BatchInsertBuilder sql(String sql) {
            if (sql == null) {
                throw new DomaNullPointerException("sql");
            }
            helper.appendSqlWithLineSeparator(sql);
            return new SubsequentBatchInsertBuilder(helper, query, paramIndex, paramMap);
        }

        @Override
        public BatchInsertBuilder removeLast() {
            helper.removeLast();
            return new SubsequentBatchInsertBuilder(helper, query, paramIndex, paramMap);
        }

        @Override
        <P> BatchInsertBuilder appendParam(Class<P> paramClass, P param, boolean literal) {
            BatchParam batchParam = new BatchParam(paramClass, paramIndex, literal);
            paramMap.put(paramIndex.getValue(), batchParam);
            batchParam.add(param);
            helper.appendParam(batchParam);
            paramIndex.increment();
            return new SubsequentBatchInsertBuilder(helper, query, paramIndex, paramMap);
        }

    }

    private static class SubsequentBatchInsertBuilder extends InitialBatchInsertBuilder {

        SubsequentBatchInsertBuilder(BatchBuildingHelper builder,
                                     SqlBatchInsertQuery query, ParamIndex paramIndex,
                                     Map<Integer, BatchParam<?>> paramMap) {
            super(builder, query, paramIndex, paramMap);
        }

        @Override
        public BatchInsertBuilder sql(String sql) {
            if (sql == null) {
                throw new DomaNullPointerException("sql");
            }
            super.helper.appendSql(sql);
            return this;
        }
    }

    private static class FixedBatchInsertBuilder extends BatchInsertBuilder {

        private FixedBatchInsertBuilder(BatchBuildingHelper builder, SqlBatchInsertQuery query, Map<Integer, BatchParam<?>> paramMap) {
            super(builder, query, new ParamIndex(), paramMap);
        }

        @Override
        public BatchInsertBuilder sql(String sql) {
            return this;
        }

        @Override
        public BatchInsertBuilder removeLast() {
            return this;
        }

        @Override
        <P> BatchInsertBuilder appendParam(Class<P> paramClass, P param, boolean literal) {
            final Integer pi = paramIndex.getValue();
            if (paramMap.containsKey(pi)) {
                BatchParam batchParam = paramMap.get(pi);
                if (paramClass != batchParam.paramClass) {
                    throw new IllegalStateException("パラメータの型が異なります。バッチ実行されるクエリは全て同一でなければなりません。");
                }
                if (literal != batchParam.literal) {
                    throw new IllegalStateException("パラメータなのかリテラルなのかは動的に変更できません。バッチ実行されるクエリは全て同一でなければなりません。");
                }
                batchParam.add(param);
            }
            paramIndex.increment();
            return this;
        }

    }

}
