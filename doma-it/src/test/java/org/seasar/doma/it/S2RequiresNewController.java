package org.seasar.doma.it;

import org.seasar.doma.jdbc.RequiresNewController;
import org.seasar.extension.tx.TransactionCallback;
import org.seasar.extension.tx.TransactionManagerAdapter;
import org.seasar.framework.container.SingletonS2Container;

public class S2RequiresNewController implements RequiresNewController {

    @SuppressWarnings("unchecked")
    @Override
    public <R> R requiresNew(final Callback<R> callback) throws Throwable {
        final TransactionManagerAdapter txAdapter = SingletonS2Container
                .getComponent(TransactionManagerAdapter.class);
        final Object result = txAdapter.requiresNew(new TransactionCallback() {

            public Object execute(final TransactionManagerAdapter adapter)
                    throws Throwable {
                return callback.execute();
            }

        });
        return (R) result;
    }

}
