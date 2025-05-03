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
 * Provides classes and interfaces for entity mapping and lifecycle management.
 *
 * <p>This package contains the core components for mapping Java objects to database tables and
 * managing their lifecycle during database operations. Key components include:
 *
 * <ul>
 *   <li>{@link org.seasar.doma.jdbc.entity.EntityType} - Metadata about entity classes
 *   <li>{@link org.seasar.doma.jdbc.entity.EntityPropertyType} - Metadata about entity properties
 *   <li>{@link org.seasar.doma.jdbc.entity.EntityListener} - Lifecycle callbacks for entities
 *   <li>{@link org.seasar.doma.jdbc.entity.NamingType} - Naming conventions for table and column
 *       names
 * </ul>
 *
 * <p>The classes in this package are typically used by the Doma annotation processor to generate
 * implementations at compile time, rather than being used directly by application code.
 *
 * @see org.seasar.doma.Entity
 * @see org.seasar.doma.Table
 * @see org.seasar.doma.Column
 */
package org.seasar.doma.jdbc.entity;
