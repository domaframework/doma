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
package org.seasar.doma.jdbc.entity;

/**
 * Represents the metadata for a database column.
 *
 * <p>This record encapsulates the configuration of a column in a database table, including its name
 * and behavioral attributes. It is used internally by the Doma framework to manage column mappings
 * and SQL generation.
 *
 * <p>The column type information is typically derived from {@link org.seasar.doma.Column}
 * annotations or {@link org.seasar.doma.ColumnOverride} configurations, and is used to control how
 * entity properties are mapped to database columns.
 *
 * @param name the column name in the database
 * @param insertable whether the column should be included in SQL INSERT statements
 * @param updatable whether the column should be included in SQL UPDATE statements
 * @param quote whether the column name should be enclosed by quotation marks in SQL statements
 * @see org.seasar.doma.Column
 * @see org.seasar.doma.ColumnOverride
 */
public record ColumnType(String name, Boolean insertable, Boolean updatable, Boolean quote) {}
