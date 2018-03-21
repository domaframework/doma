package org.seasar.doma.jdbc;

import java.util.List;

import org.seasar.doma.message.Message;

/**
 * Thrown to indicate that all properties in an entity are not mapped to columns
 * in a result set.
 */
public class ResultMappingException extends JdbcException {

    private static final long serialVersionUID = 1L;

    protected final String entityClassName;

    protected final List<String> unmappedPropertyNames;

    protected final List<String> expectedColumnNames;

    protected final SqlKind kind;

    protected final String rawSql;

    protected final String formattedSql;

    protected final String sqlFilePath;

    public ResultMappingException(SqlLogType logType, String entityClassName,
            List<String> unmappedPropertyNames, List<String> expectedColumnNames, SqlKind kind,
            String rawSql, String formattedSql, String sqlFilePath) {
        super(Message.DOMA2216, entityClassName, unmappedPropertyNames, expectedColumnNames,
                sqlFilePath, choiceSql(logType, rawSql, formattedSql));
        this.entityClassName = entityClassName;
        this.unmappedPropertyNames = unmappedPropertyNames;
        this.expectedColumnNames = expectedColumnNames;
        this.kind = kind;
        this.rawSql = rawSql;
        this.formattedSql = formattedSql;
        this.sqlFilePath = sqlFilePath;
    }

    /**
     * Returns the entity class name.
     * 
     * @return the entity class name
     */
    public String getEntityClassName() {
        return entityClassName;
    }

    /**
     * Returns the unmapped property names.
     * 
     * @return the unmapped property names
     */
    public List<String> getUnmappedPropertyNames() {
        return unmappedPropertyNames;
    }

    /**
     * Returns the expected column names.
     * 
     * @return the expected column names
     */
    public List<String> getExpectedColumnNames() {
        return expectedColumnNames;
    }

    /**
     * Returns the SQL kind.
     * 
     * @return the SQL kind
     */
    public SqlKind getKind() {
        return kind;
    }

    /**
     * Returns the raw SQL string.
     * 
     * @return the raw SQL string
     */
    public String getRawSql() {
        return rawSql;
    }

    /**
     * Returns the formatted SQL string.
     * 
     * @return the formatted SQL string or {@code null} if it does not exist
     */
    public String getFormattedSql() {
        return formattedSql;
    }

    /**
     * Returns the SQL file path
     * 
     * @return the SQL file path or {@code null} if the SQL is auto generated
     */
    public String getSqlFilePath() {
        return sqlFilePath;
    }
}
