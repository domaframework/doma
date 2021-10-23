module org.seasar.doma.mock {
  exports org.seasar.doma.internal.jdbc.mock to org.seasar.doma.core, org.seasar.doma.processor;

  requires java.sql;
}
