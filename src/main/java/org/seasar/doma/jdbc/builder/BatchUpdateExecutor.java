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
import java.util.List;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.UniqueConstraintException;
import org.seasar.doma.jdbc.command.BatchUpdateCommand;
import org.seasar.doma.jdbc.query.SqlBatchUpdateQuery;

/**
 * UPDATE文を組み立てバッチ実行するクラスです。
 * <p>
 * このクラスはスレッドセーフではありません。
 *
 * <h3>例</h3>
 * <h4>Java</h4>
 *
 * <pre>
 * List&lt;Employee&gt; employees = Arrays
 *         .asList(new Employee[] { new Employee(10, &quot;SMITH&quot;, new BigDecimal(&quot;1000&quot;)),
 *                 new Employee(20, &quot;ALLEN&quot;, new BigDecimal(&quot;2000&quot;)) });
 * BatchUpdateExecutor executor = BatchUpdateExecutor.newInstance(config);
 * executor.batchSize(10);
 * executor.execute(employees, (emp, builder) -&gt; {
 *     builder.sql(&quot;update Emp&quot;);
 *     builder.sql(&quot;set&quot;);
 *     builder.sql(&quot;name = &quot;).param(String.class, emp.name).sql(&quot;,&quot;);
 *     builder.sql(&quot;salary = &quot;).param(BigDecimal.class, emp.salary);
 *     builder.sql(&quot;where&quot;);
 *     builder.sql(&quot;id = &quot;).param(int.class, emp.id);
 * });
 * </pre>
 *
 * <h4>実行されるSQL</h4>
 *
 * <pre>
 * update Emp
 * set
 * name = 'SMITH',
 * salary = 1000
 * where
 * id = 10
 *
 * update Emp
 * set
 * name = 'ALLEN',
 * salary = 2000
 * where
 * id = 20
 * </pre>
 *
 *
 * @author bakenezumi
 * @since 2.14.0
 */
public class BatchUpdateExecutor {

    private final SqlBatchUpdateQuery query;

    private BatchUpdateExecutor(Config config) {
        this.query = new SqlBatchUpdateQuery();
        this.query.setConfig(config);
        this.query.setCallerClassName(getClass().getName());
        this.query.setSqlLogType(SqlLogType.FORMATTED);
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
    public static BatchUpdateExecutor newInstance(Config config) {
        if (config == null) {
            throw new DomaNullPointerException("config");
        }
        return new BatchUpdateExecutor(config);
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
        query.setQueryTimeout(queryTimeout);
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
        query.setSqlLogType(sqlLogType);
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
        query.setBatchSize(batchSize);
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
        query.setCallerClassName(className);
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
        query.setCallerMethodName(methodName);
    }

    /**
     * SQLを実行します。
     *
     * @param <P>
     *            パラメータの型
     * @param params
     *            1要素が1クエリのパラメータの元となる {@link java.lang.Iterable} なもの
     * @param buildConsumer
     *            {@link BatchBuilder} を使って1回分のクエリを組み立てるラムダ式。 第一パラメータには params
     *            の要素が、 第二パラメータには {@link BatchBuilder} のインスタンスが渡ります。
     * @return 更新された件数の配列。 戻り値の配列の要素の数はパラメータのparamsの要素の数と等しくなります。
     *         配列のそれぞれの要素が更新された件数を返します。
     * @throws DomaNullPointerException
     *             引数が{@code null} の場合
     * @throws UniqueConstraintException
     *             一意制約違反が発生した場合
     * @throws JdbcException
     *             上記以外でJDBCに関する例外が発生した場合
     */
    public <P> int[] execute(Iterable<P> params, BiConsumer<P, BatchBuilder> buildConsumer) {
        if (params == null) {
            throw new DomaNullPointerException("params");
        }
        if (buildConsumer == null) {
            throw new DomaNullPointerException("buildConsumer");
        }
        if (query.getMethodName() == null) {
            query.setCallerMethodName("execute");
        }
        BatchBuilder builder = BatchBuilder.newInstance(query);
        for (P p : params) {
            buildConsumer.accept(p, builder);
            builder = builder.fixSql();
        }
        return builder.execute(() -> new BatchUpdateCommand(query));
    }

    /**
     * 組み立てられたSQLを返します。
     * 
     * @return 組み立てられたSQL
     */
    public List<? extends Sql<?>> getSqls() {
        return query.getSqls();
    }

}
