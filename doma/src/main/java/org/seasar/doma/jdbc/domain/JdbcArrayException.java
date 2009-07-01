package org.seasar.doma.jdbc.domain;

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
public class JdbcArrayException extends JdbcException {

    private static final long serialVersionUID = 1L;

    public JdbcArrayException(Throwable cause, Object... args) {
        super(MessageCode.DOMA2027, cause, args);
    }

}
