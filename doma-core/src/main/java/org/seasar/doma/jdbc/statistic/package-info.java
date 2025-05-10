/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Provides classes and interfaces for collecting and managing SQL execution statistics.
 *
 * <p>This package contains components for monitoring and analyzing the performance of SQL
 * statements executed through Doma. Key components include:
 *
 * <ul>
 *   <li>{@link org.seasar.doma.jdbc.statistic.StatisticManager} - Interface for managing SQL
 *       execution statistics
 *   <li>{@link org.seasar.doma.jdbc.statistic.Statistic} - Record representing statistics for a
 *       specific SQL statement
 * </ul>
 *
 * <p>These classes can be used to monitor the performance of SQL statements in an application,
 * helping to identify slow queries and optimize database access.
 *
 * @see org.seasar.doma.jdbc.Config#getStatisticManager()
 */
package org.seasar.doma.jdbc.statistic;
