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
 * A builder that builds SQL INSERT statements from maps for a batch execution.
 * <p>
 * This is not thread safe.
 *
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
 * <h4>built SQLs</h4>
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
     * Creates a new instance.
     * 
     * @param config
     *            the configuration
     * @param tableName
     *            the table name
     * @return a builder
     * @throws DomaNullPointerException
     *             if {@code config} or {@code tableName} is {@code null}
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
     * Executes SQL INSERT statements.
     * 
     * @param parameter
     *            the parameter
     * @return the array whose each element contains affected rows count. The
     *         array length is equal to the {@code parameter} size.
     * @throws DomaNullPointerException
     *             if {@code parameter} is {@code null}
     * @throws DomaIllegalArgumentException
     *             if {@code parameter} is empty
     * @throws UniqueConstraintException
     *             if an unique constraint violation occurs
     * @throws JdbcException
     *             if a JDBC related error occurs
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
                    Class<Object> clazz = (Class<Object>) value.getClass();
                    builder.param(clazz, value).sql(", ");
                }
            });
            builder.removeLast().sql(")");
        });

    }

    /**
     * Sets the query timeout limit in seconds.
     * <p>
     * If not specified, the value of {@link Config#getQueryTimeout()} is used.
     * 
     * @param queryTimeout
     *            the query timeout limit in seconds
     * @see Statement#setQueryTimeout(int)
     */
    public void queryTimeout(int queryTimeout) {
        executor.queryTimeout(queryTimeout);
    }

    /**
     * Sets the SQL log format.
     * 
     * @param sqlLogType
     *            the SQL log format type
     */
    public void sqlLogType(SqlLogType sqlLogType) {
        executor.sqlLogType(sqlLogType);
    }

    /**
     * Sets the batch size.
     * <p>
     * If not specified, the value of {@link Config#getBatchSize()} is used.
     *
     * @param batchSize
     *            the batch size
     */
    public void batchSize(int batchSize) {
        executor.batchSize(batchSize);
    }

    /**
     * Sets the caller class name.
     * <p>
     * If not specified, the class name of this instance is used.
     * 
     * @param className
     *            the caller class name
     * @throws DomaNullPointerException
     *             if {@code className} is {@code null}
     */
    public void callerClassName(String className) {
        executor.callerClassName(className);
    }

    /**
     * Sets the caller method name.
     * <p>
     * if not specified, {@code execute} is used.
     * 
     * @param methodName
     *            the caller method name
     * @throws DomaNullPointerException
     *             if {@code methodName} is {@code null}
     */
    public void callerMethodName(String methodName) {
        if (methodName == null) {
            throw new DomaNullPointerException("methodName");
        }
        executor.callerMethodName(methodName);
    }

    /**
     * Returns the built SQL.
     * 
     * @return the built SQL
     */
    public List<? extends Sql<?>> getSqls() {
        return executor.getSqls();
    }

}
