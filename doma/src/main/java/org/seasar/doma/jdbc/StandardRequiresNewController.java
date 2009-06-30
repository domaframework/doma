package org.seasar.doma.jdbc;

/**
 * @author taedium
 * 
 */
public class StandardRequiresNewController implements RequiresNewController {

    @Override
    public <R> R requiresNew(Callback<R> callback) throws Throwable {
        return callback.execute();
    }

}
