package org.seasar.doma.jdbc.tx;

import java.io.Serializable;

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/**
 * Thrown to indicate that a save point is not found.
 */
public class SavepointNotFoundException extends JdbcException implements Serializable {

    private static final long serialVersionUID = 1L;

    protected final String savepointName;

    public SavepointNotFoundException(String savepointName) {
        super(Message.DOMA2054, savepointName);
        this.savepointName = savepointName;
    }

    public String getSavepointName() {
        return savepointName;
    }
}
