package org.seasar.doma.jdbc;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.internal.util.Resources;
import org.seasar.doma.message.MessageCode;


/**
 * @author taedium
 * 
 */
public class StandardSqlFileRepository implements SqlFileRepository {

    protected static final String SQL_EXTENSION = ".sql";

    protected final ConcurrentMap<String, SqlFile> sqlFileMap = new ConcurrentHashMap<String, SqlFile>(
            200);

    public SqlFile getSqlFile(String path, Dialect dialect) {
        if (path == null) {
            throw new DomaIllegalArgumentException("path", path);
        }
        if (!path.endsWith(SQL_EXTENSION)) {
            throw new DomaIllegalArgumentException("path", path);
        }
        if (dialect == null) {
            throw new DomaIllegalArgumentException("dialect", dialect);
        }
        SqlFile file = sqlFileMap.get(path);
        if (file != null) {
            return file;
        }
        file = createSqlFile(path, dialect);
        SqlFile current = sqlFileMap.putIfAbsent(path, file);
        return current != null ? current : file;
    }

    protected SqlFile createSqlFile(String path, Dialect dialect) {
        String primaryPath = getPrimaryPath(path, dialect);
        String sql = getSql(primaryPath);
        if (sql != null) {
            SqlNode sqlNode = createSqlNode(sql);
            return new SqlFile(primaryPath, sql, sqlNode);
        }
        sql = getSql(path);
        if (sql != null) {
            SqlNode sqlNode = createSqlNode(sql);
            return new SqlFile(path, sql, sqlNode);
        }
        throw new SqlFileNotFoundException(path);
    }

    protected String getPrimaryPath(String path, Dialect dialect) {
        String name = dialect.getName();
        return path.substring(0, path.length() - SQL_EXTENSION.length()) + "_"
                + name + SQL_EXTENSION;
    }

    protected SqlNode createSqlNode(String sql) {
        SqlParser parser = new SqlParser(sql);
        return parser.parse();
    }

    protected String getSql(String path) {
        try {
            return Resources.getResourceAsString(path);
        } catch (WrapException e) {
            Throwable cause = e.getCause();
            throw new JdbcException(MessageCode.DOMA2010, cause, path, cause);
        }
    }

}
