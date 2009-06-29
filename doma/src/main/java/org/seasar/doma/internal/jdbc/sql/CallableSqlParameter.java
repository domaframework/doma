package org.seasar.doma.internal.jdbc.sql;

/**
 * @author taedium
 * 
 */
public interface CallableSqlParameter extends SqlParameter {

    <R, P, TH extends Throwable> R accept(
            CallableSqlParameterVisitor<R, P, TH> visitor, P p) throws TH;
}
