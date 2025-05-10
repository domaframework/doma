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
 * Provides classes and interfaces for entity aggregation and association operations.
 *
 * <p>This package contains components for aggregating entities and establishing associations
 * between them during database operations. Key components include:
 *
 * <ul>
 *   <li>{@link org.seasar.doma.jdbc.aggregate.AggregateCommand} - A command for executing aggregate
 *       operations on a stream of entities
 *   <li>{@link org.seasar.doma.jdbc.aggregate.AggregateStrategyType} - A strategy for defining an
 *       aggregate structure involving a root entity and its associated entities
 *   <li>{@link org.seasar.doma.jdbc.aggregate.AssociationLinkerType} - Defines how entities are
 *       linked in an association
 *   <li>{@link org.seasar.doma.jdbc.aggregate.StreamReducer} - Reduces a stream of entities to a
 *       result
 * </ul>
 */
package org.seasar.doma.jdbc.aggregate;
