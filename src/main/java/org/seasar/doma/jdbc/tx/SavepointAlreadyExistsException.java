package org.seasar.doma.jdbc.tx;

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/**
 * Thrown to indicate that a save point already exists.
 */
public class SavepointAlreadyExistsException extends JdbcException {

    private static final long serialVersionUID = 1L;

    protected final String savepointName;

    public SavepointAlreadyExistsException(String savepointName) {
        super(Message.DOMA2059, savepointName);
        this.savepointName = savepointName;
    }

    public String getSavepointName() {
        return savepointName;
    }
}
