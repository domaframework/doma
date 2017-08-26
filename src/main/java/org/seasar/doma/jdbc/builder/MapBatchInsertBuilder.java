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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.UniqueConstraintException;
import org.seasar.doma.message.Message;

/**
 * MAPからINSERT文を自動的に組み立ててバッチ実行するクラスです。
 * <p>
 * このクラスはスレッドセーフではありません。
 *
 * <h3>例</h3>
 * <h4>Java</h4>
 *
 * <pre>
 * MapBatchInsertBuilder builder = MapBatchInsertBuilder.newInstance(config, "Emp");
 * builder.batchSize(10);
 * builder.execute(new ArrayList&lt;Map&lt;String, Object&gt;&gt;() {
 *     {
 *         add(new LinkedHashMap&lt;String, Object&gt;() {
 *             {
 *                 put(&quot;name&quot;, &quot;SMITH&quot;);
 *                 put(&quot;salary&quot;, 1000);
 *             }
 *         });
 *         add(new LinkedHashMap&lt;String, Object&gt;() {
 *             {
 *                 put(&quot;name", &quot;ALLEN&quot;);
 *                 put(&quot;salary&quot;, 2000);
 *             }
 *         });
 *     }
 * });
 * </pre>
 *
 * <h4>実行されるSQL</h4>
 *
 * <pre>
 * insert into Emp
 * (name, salary)
 * values('SMITH', 1000)
 *
 * insert into Emp
 * (name, salary)
 * values('ALLEN', 2000)
 * </pre>
 *
 *
 * @author bakenezumi
 * @since 2.14.0
 */
public class MapBatchInsertBuilder {

    private final BatchInsertExecutor executor;

    private final String tableName;

    private MapBatchInsertBuilder(Config config, String tableName) {
        this.executor = BatchInsertExecutor.newInstance(config);
        executor.callerClassName(getClass().getName());
        this.tableName = tableName;
    }

    /**
     * ファクトリメソッドです。
     *
     * @param config
     *            設定
     * @param tableName
     *            テーブル名
     * @return INSERT文を組み立てるビルダー
     * @throws DomaNullPointerException
     *             引数が{@code null} の場合
     */
    public static MapBatchInsertBuilder newInstance(Config config, String tableName) {
        if (config == null) {
            throw new DomaNullPointerException("config");
        }
        if (tableName == null) {
            throw new DomaNullPointerException("tableName");
        }
        return new MapBatchInsertBuilder(config, tableName);
    }

    /**
     * パラメータからINSERT文を組み立てて実行します。
     *
     * @return 更新された件数の配列。 戻り値の配列の要素の数はパラメータのparameterの要素の数と等しくなります。
     *         配列のそれぞれの要素が更新された件数を返します。
     * @param parameter
     *            INSERT文の生成元となるMapのリスト
     * @throws DomaNullPointerException
     *             parameterがnullの場合
     * @throws DomaIllegalArgumentException
     *             parameterの要素が空の場合
     * @throws UniqueConstraintException
     *             一意制約違反が発生した場合
     * @throws JdbcException
     *             上記以外でJDBCに関する例外が発生した場合
     */
    @SuppressWarnings("unchecked")
    public int[] execute(Iterable<? extends Map<String, Object>> parameter) {
        if (parameter == null) {
            throw new DomaNullPointerException("parameter");
        }
        if (!parameter.iterator().hasNext() || parameter.iterator().next() == null) {
            throw new JdbcException(Message.DOMA2232);
        }
        if (executor.getMethodName() == null) {
            executor.callerMethodName("execute");
        }
        final Set<String> keySet = new LinkedHashSet<>(parameter.iterator().next().keySet());
        final int keySetSize = keySet.size();
        return executor.execute(parameter, (map, builder) -> {
            if (keySetSize != map.size()) {
                throw new JdbcException(Message.DOMA2231);
            }
            builder.sql("insert into ")
                    .sql(tableName)
                    .sql(" (")
                    .sql(keySet.stream().collect(Collectors.joining(", ")))
                    .sql(")");
            builder.sql("values (");
            keySet.forEach(key -> {
                if (!map.containsKey(key)) {
                    throw new JdbcException(Message.DOMA2233, key);
                }
                Object value = map.get(key);
                if (value == null) {
                    builder.param(Object.class, null).sql(", ");
                } else {
                    // 静的な型指定が行えないためObjectにキャストしている
                    // BatchBuilder内で下記clazzを利用した型チェックが行われているため安全である
                    Class<Object> clazz = (Class<Object>) value.getClass();
                    builder.param(clazz, value).sql(", ");
                }
            });
            builder.removeLast().sql(")");
        });

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
        executor.queryTimeout(queryTimeout);
    }

    /**
     * SQLのログの出力形式を設定します。
     *
     * @param sqlLogType
     *            SQLのログの出力形式
     */
    public void sqlLogType(SqlLogType sqlLogType) {
        executor.sqlLogType(sqlLogType);
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
        executor.batchSize(batchSize);
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
        executor.callerClassName(className);
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
        executor.callerMethodName(methodName);
    }

    /**
     * 組み立てられたSQLを返します。
     *
     * @return 組み立てられたSQL
     */
    public List<? extends Sql<?>> getSqls() {
        return executor.getSqls();
    }

}
