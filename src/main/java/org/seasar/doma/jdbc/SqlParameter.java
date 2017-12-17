package org.seasar.doma.jdbc;

/**
 * An SQL parameter.
 */
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
     * @param visitor
     *            the visitor
     * @param p
     *            the parameter
     * @return the value that is processed by the visitor
     * @throws TH
     *             the exception
     */
    <R, P, TH extends Throwable> R accept(SqlParameterVisitor<R, P, TH> visitor, P p) throws TH;
}
