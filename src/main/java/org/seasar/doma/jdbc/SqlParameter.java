package org.seasar.doma.jdbc;

/** An SQL parameter. */
public interface SqlParameter {

  /**
   * Returns the parameter value.
   *
   * @return the parameter value
   */
  Object getValue();

  /**
   * Accepts the visitor.
   *
   * @param visitor the visitor
   * @param p the parameter
   * @throws TH the exception
   */
  <P, TH extends Throwable> void accept(SqlParameterVisitor<P, TH> visitor, P p) throws TH;
}
