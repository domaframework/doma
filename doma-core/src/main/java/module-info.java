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
  exports org.seasar.doma.jdbc.tx;
  exports org.seasar.doma.jdbc.type;
  exports org.seasar.doma.message;
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

  // Requires
  requires transitive java.sql;
  requires transitive static java.compiler;
}
