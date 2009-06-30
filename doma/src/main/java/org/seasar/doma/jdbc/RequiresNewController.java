package org.seasar.doma.jdbc;

/**
 * @author taedium
 * 
 */
public interface RequiresNewController {

    <R> R requiresNew(Callback<R> callback) throws Throwable;

    interface Callback<R> {

        R execute();
    }
}
