package org.seasar.doma.jdbc;

/**
 * @author taedium
 * 
 */
public interface IterationCallback<R, T> {

    R iterate(T target, IterationContext context);
}
