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
module org.seasar.doma.core {
  // Exports
  exports org.seasar.doma;
  exports org.seasar.doma.expr;
  exports org.seasar.doma.jdbc;
  exports org.seasar.doma.jdbc.builder;
  exports org.seasar.doma.jdbc.command;
  exports org.seasar.doma.jdbc.criteria;
  exports org.seasar.doma.jdbc.criteria.command;
  exports org.seasar.doma.jdbc.criteria.context;
  exports org.seasar.doma.jdbc.criteria.declaration;
  exports org.seasar.doma.jdbc.criteria.expression;
  exports org.seasar.doma.jdbc.criteria.metamodel;
  exports org.seasar.doma.jdbc.criteria.option;
  exports org.seasar.doma.jdbc.criteria.query;
  exports org.seasar.doma.jdbc.criteria.statement;
  exports org.seasar.doma.jdbc.criteria.tuple;
  exports org.seasar.doma.jdbc.dialect;
  exports org.seasar.doma.jdbc.domain;
  exports org.seasar.doma.jdbc.entity;
  exports org.seasar.doma.jdbc.id;
  exports org.seasar.doma.jdbc.query;
  exports org.seasar.doma.jdbc.statistic;
  exports org.seasar.doma.jdbc.tx;
  exports org.seasar.doma.jdbc.type;
  exports org.seasar.doma.message;
  exports org.seasar.doma.util;
  exports org.seasar.doma.wrapper;

  // Exports: These are mainly accessed from generated code
  exports org.seasar.doma.internal;
  exports org.seasar.doma.internal.jdbc.command;
  exports org.seasar.doma.internal.jdbc.dao;
  exports org.seasar.doma.internal.jdbc.entity;
  exports org.seasar.doma.internal.jdbc.scalar;
  exports org.seasar.doma.internal.jdbc.sql;
  exports org.seasar.doma.internal.wrapper;
  exports org.seasar.doma.internal.util;

  // Exports: These are mainly accessed from other doma modules
  exports org.seasar.doma.internal.expr;
  exports org.seasar.doma.internal.expr.node;
  exports org.seasar.doma.internal.jdbc.sql.node;
  exports org.seasar.doma.internal.jdbc.util;
  exports org.seasar.doma.jdbc.aggregate;

  // Requires
  requires transitive java.sql;
  requires transitive static java.compiler;
}
