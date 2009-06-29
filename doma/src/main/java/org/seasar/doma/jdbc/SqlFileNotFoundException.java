package org.seasar.doma.jdbc;

import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
public class SqlFileNotFoundException extends JdbcException {

    private static final long serialVersionUID = 1L;

    protected final String path;

    public SqlFileNotFoundException(String path) {
        super(MessageCode.DOMA2011, path);
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
