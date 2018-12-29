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
   * @param <R> the result type
   * @param <P> the parameter type
   * @param <TH> the throwable type
   * @param visitor the visitor
   * @param p the parameter
   * @return the result
   * @throws TH the exception
   */
  <R, P, TH extends Throwable> R accept(SqlParameterVisitor<R, P, TH> visitor, P p) throws TH;
}
