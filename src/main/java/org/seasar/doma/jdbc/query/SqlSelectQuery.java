package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.jdbc.SelectOptionsAccessor;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlNode;

public class SqlSelectQuery extends AbstractSelectQuery {

    protected SqlNode sqlNode;

    @Override
    public void prepare() {
        super.prepare();
        assertNotNull(sqlNode);
    }

    protected void prepareSql() {
        SqlNode transformedSqlNode = config.getDialect().transformSelectSqlNode(sqlNode, options);
        buildSql((evaluator, expander) -> {
            NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(config, SqlKind.SELECT,
                    null, evaluator, sqlLogType, expander);
            return sqlBuilder.build(transformedSqlNode, this::comment);
        });

    }

    @Override
    public void complete() {
        if (SelectOptionsAccessor.isCount(options)) {
            executeCount(sqlNode);
        }
    }

    public void setSqlNode(SqlNode sqlNode) {
        this.sqlNode = sqlNode;
    }

}
