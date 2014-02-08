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

/**
 * {@link Config} が返すインタフェースのデフォルト実装を提供します。
 * 
 * @author nakamura-to
 * @since 2.0.0
 */
public final class ConfigSupport {

    public static SqlFileRepository defaultSqlFileRepository = new GreedyCacheSqlFileRepository();

    public static JdbcLogger defaultJdbcLogger = new UtilLoggingJdbcLogger();

    public static RequiresNewController defaultRequiresNewController = new RequiresNewController() {
    };

    public static ClassHelper defaultClassHelper = new ClassHelper() {
    };

    public static CommandImplementors defaultCommandImplementors = new CommandImplementors() {
    };

    public static QueryImplementors defaultQueryImplementors = new QueryImplementors() {
    };

    public static UnknownColumnHandler defaultUnknownColumnHandler = new UnknownColumnHandler() {
    };

}
