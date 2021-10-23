module org.seasar.doma.kotlin {
  exports org.seasar.doma.kotlin.jdbc.criteria;
  exports org.seasar.doma.kotlin.jdbc.criteria.declaration;
  exports org.seasar.doma.kotlin.jdbc.criteria.expression;
  exports org.seasar.doma.kotlin.jdbc.criteria.statement;

  requires kotlin.stdlib.jdk8;
  requires org.seasar.doma.core;
}
