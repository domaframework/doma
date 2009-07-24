/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.seasar.doma.DomaMessageCode;
import org.seasar.doma.DomaUnsupportedOperationException;
import org.seasar.doma.domain.Domain;
import org.seasar.doma.internal.expr.EvaluationResult;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.ExpressionException;
import org.seasar.doma.internal.expr.ExpressionParser;
import org.seasar.doma.internal.expr.node.ExpressionNode;
import org.seasar.doma.internal.jdbc.sql.node.AnonymousNode;
import org.seasar.doma.internal.jdbc.sql.node.AnonymousNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.BindVariableNode;
import org.seasar.doma.internal.jdbc.sql.node.BindVariableNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.ClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.ElseNode;
import org.seasar.doma.internal.jdbc.sql.node.ElseNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.ElseifNode;
import org.seasar.doma.internal.jdbc.sql.node.ElseifNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.EndNode;
import org.seasar.doma.internal.jdbc.sql.node.EndNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.ForUpdateClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.ForUpdateClauseNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.FragmentNode;
import org.seasar.doma.internal.jdbc.sql.node.FragmentNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.FromClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.FromClauseNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.GroupByClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.GroupByClauseNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.HavingClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.HavingClauseNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.IfBlockNode;
import org.seasar.doma.internal.jdbc.sql.node.IfBlockNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.IfNode;
import org.seasar.doma.internal.jdbc.sql.node.IfNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.LogicalOperatorNode;
import org.seasar.doma.internal.jdbc.sql.node.LogicalOperatorNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.OrderByClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.OrderByClauseNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.OtherNode;
import org.seasar.doma.internal.jdbc.sql.node.OtherNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.ParensNode;
import org.seasar.doma.internal.jdbc.sql.node.ParensNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.SelectClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectClauseNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.SqlLocation;
import org.seasar.doma.internal.jdbc.sql.node.WhereClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.WhereClauseNodeVisitor;
import org.seasar.doma.internal.jdbc.sql.node.WordNode;
import org.seasar.doma.internal.jdbc.sql.node.WordNodeVisitor;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SqlLogFormattingFunction;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public class NodePreparedSqlBuilder implements
        SqlNodeVisitor<Void, NodePreparedSqlBuilder.Context>,
        AnonymousNodeVisitor<Void, NodePreparedSqlBuilder.Context>,
        BindVariableNodeVisitor<Void, NodePreparedSqlBuilder.Context>,
        ElseifNodeVisitor<Void, NodePreparedSqlBuilder.Context>,
        ElseNodeVisitor<Void, NodePreparedSqlBuilder.Context>,
        EndNodeVisitor<Void, NodePreparedSqlBuilder.Context>,
        ForUpdateClauseNodeVisitor<Void, NodePreparedSqlBuilder.Context>,
        FragmentNodeVisitor<Void, NodePreparedSqlBuilder.Context>,
        FromClauseNodeVisitor<Void, NodePreparedSqlBuilder.Context>,
        GroupByClauseNodeVisitor<Void, NodePreparedSqlBuilder.Context>,
        HavingClauseNodeVisitor<Void, NodePreparedSqlBuilder.Context>,
        IfBlockNodeVisitor<Void, NodePreparedSqlBuilder.Context>,
        IfNodeVisitor<Void, NodePreparedSqlBuilder.Context>,
        LogicalOperatorNodeVisitor<Void, NodePreparedSqlBuilder.Context>,
        OrderByClauseNodeVisitor<Void, NodePreparedSqlBuilder.Context>,
        OtherNodeVisitor<Void, NodePreparedSqlBuilder.Context>,
        ParensNodeVisitor<Void, NodePreparedSqlBuilder.Context>,
        SelectClauseNodeVisitor<Void, NodePreparedSqlBuilder.Context>,
        SelectStatementNodeVisitor<Void, NodePreparedSqlBuilder.Context>,
        WhereClauseNodeVisitor<Void, NodePreparedSqlBuilder.Context>,
        WordNodeVisitor<Void, NodePreparedSqlBuilder.Context> {

    protected final ExpressionEvaluator evaluator;

    protected final Config config;

    protected final SqlLogFormattingFunction formattingFunction;

    public NodePreparedSqlBuilder(Config config) {
        this(config, new ExpressionEvaluator());
    }

    public NodePreparedSqlBuilder(Config config, ExpressionEvaluator evaluator) {
        assertNotNull(config, evaluator);
        this.config = config;
        this.evaluator = evaluator;
        this.formattingFunction = new ConvertToLogFormatFunction();
    }

    public PreparedSql build(SqlNode sqlNode) {
        assertNotNull(sqlNode);
        Context context = new Context();
        sqlNode.accept(this, context);
        return new PreparedSql(context.getSqlBuf(), context
                .getFormattedSqlBuf(), context.getParameters());
    }

    @Override
    public Void visitAnonymousNode(AnonymousNode node, Context p) {
        for (SqlNode child : node.getChildren()) {
            child.accept(this, p);
        }
        return null;
    }

    @Override
    public Void visitOtherNode(OtherNode node, Context p) {
        String other = node.getOther();
        p.appendRawSql(other);
        p.appendFormattedSql(other);
        return null;
    }

    @Override
    public Void visitBindVariableNode(BindVariableNode node, Context p) {
        SqlLocation location = node.getLocation();
        String name = node.getVariableName();
        EvaluationResult result = evaluate(location, name);
        Object value = result.getValue();
        Class<?> valueClass = result.getValueClass();
        p.setAvailable(true);
        if (node.isWordNodeIgnored()) {
            handleSingleBindVarialbeNode(node, p, value, valueClass);
        } else if (node.isParensNodeIgnored()) {
            ParensNode parensNode = node.getParensNode();
            OtherNode openedFragmentNode = parensNode.getOpenedFragmentNode();
            openedFragmentNode.accept(this, p);
            if (Collection.class.isAssignableFrom(valueClass)) {
                handleCollectionBindVarialbeNode(node, p, value, valueClass);
            } else {
                throw new JdbcException(DomaMessageCode.DOMA2112,
                        location.getSql(), location.getLineNumber(), location
                                .getPosition(), node.getText(), valueClass);
            }
            OtherNode closedFragmentNode = parensNode.getClosedFragmentNode();
            closedFragmentNode.accept(this, p);
        } else {
            assertUnreachable();
        }
        return null;
    }

    protected Void handleSingleBindVarialbeNode(BindVariableNode node,
            Context p, Object value, Class<?> valueClass) {
        if (!Domain.class.isAssignableFrom(valueClass)) {
            SqlLocation location = node.getLocation();
            throw new JdbcException(DomaMessageCode.DOMA2114, location.getSql(),
                    location.getLineNumber(), location.getPosition(), node
                            .getText(), valueClass.getName());
        }
        Domain<?, ?> domain = Domain.class.cast(value);
        p.addBindValue(domain);
        return null;
    }

    protected void handleCollectionBindVarialbeNode(BindVariableNode node,
            Context p, Object value, Class<?> valueClass) {
        Collection<?> values = Collection.class.cast(value);
        int index = 0;
        for (Object v : values) {
            if (v == null) {
                SqlLocation location = node.getLocation();
                throw new JdbcException(DomaMessageCode.DOMA2115,
                        location.getSql(), location.getLineNumber(), location
                                .getPosition(), node.getText(), index);
            }
            if (!Domain.class.isInstance(v)) {
                SqlLocation location = node.getLocation();
                throw new JdbcException(DomaMessageCode.DOMA2113,
                        location.getSql(), location.getLineNumber(), location
                                .getPosition(), node.getText(), v.getClass()
                                .getName());
            }
            Domain<?, ?> domain = Domain.class.cast(v);
            p.addBindValue(domain);
            p.appendRawSql(", ");
            p.appendFormattedSql(", ");
            index++;
        }
        if (index > 0) {
            p.cutBackSqlBuf(2);
            p.cutBackFormattedSqlBuf(2);
        }
    }

    @Override
    public Void visitIfBlockNode(IfBlockNode node, Context p) {
        if (handleIfNode(node, p)) {
            p.setAvailable(true);
        } else if (handleElseifNode(node, p)) {
            p.setAvailable(true);
        } else if (handleElseNode(node, p)) {
            p.setAvailable(true);
        }
        EndNode endNode = node.getEndNode();
        endNode.accept(this, p);
        return null;
    }

    protected boolean handleIfNode(IfBlockNode node, Context p) {
        IfNode ifNode = node.getIfNode();
        SqlLocation location = ifNode.getLocation();
        String expression = ifNode.getExpression();
        EvaluationResult ifResult = evaluate(location, expression);
        if (ifResult.getBooleanValue()) {
            ifNode.accept(this, p);
            return true;
        }
        return false;
    }

    protected boolean handleElseifNode(IfBlockNode node, Context p) {
        for (ElseifNode elseifNode : node.getElseifNodes()) {
            SqlLocation location = elseifNode.getLocation();
            String expression = elseifNode.getExpression();
            EvaluationResult elseifResult = evaluate(location, expression);
            if (elseifResult.getBooleanValue()) {
                elseifNode.accept(this, p);
                return true;
            }
        }
        return false;
    }

    protected boolean handleElseNode(IfBlockNode node, Context p) {
        ElseNode elseNode = node.getElseNode();
        if (elseNode != null) {
            elseNode.accept(this, p);
            return true;
        }
        return false;
    }

    @Override
    public Void visitIfNode(IfNode node, Context p) {
        for (SqlNode child : node.getChildren()) {
            child.accept(this, p);
        }
        return null;
    }

    @Override
    public Void visitElseifNode(ElseifNode node, Context p) {
        for (SqlNode child : node.getChildren()) {
            child.accept(this, p);
        }
        return null;
    }

    @Override
    public Void visitElseNode(ElseNode node, Context p) {
        for (SqlNode child : node.getChildren()) {
            child.accept(this, p);
        }
        return null;
    }

    @Override
    public Void visitEndNode(EndNode node, Context p) {
        return null;
    }

    @Override
    public Void visitSelectStatementNode(SelectStatementNode node, Context p) {
        SelectClauseNode selectClauseNode = node.getSelectClauseNode();
        if (selectClauseNode != null) {
            selectClauseNode.accept(this, p);
        }
        FromClauseNode fromClauseNode = node.getFromClauseNode();
        if (fromClauseNode != null) {
            fromClauseNode.accept(this, p);
        }
        WhereClauseNode whereClauseNode = node.getWhereClauseNode();
        if (whereClauseNode != null) {
            whereClauseNode.accept(this, p);
        }
        GroupByClauseNode groupByClauseNode = node.getGroupByClauseNode();
        if (groupByClauseNode != null) {
            groupByClauseNode.accept(this, p);
        }
        HavingClauseNode havingClauseNode = node.getHavingClauseNode();
        if (havingClauseNode != null) {
            havingClauseNode.accept(this, p);
        }
        OrderByClauseNode orderByClauseNode = node.getOrderByClauseNode();
        if (orderByClauseNode != null) {
            orderByClauseNode.accept(this, p);
        }
        ForUpdateClauseNode forUpdateClauseNode = node.getForUpdateClauseNode();
        if (forUpdateClauseNode != null) {
            forUpdateClauseNode.accept(this, p);
        }
        return null;
    }

    @Override
    public Void visitSelectClauseNode(SelectClauseNode node, Context p) {
        WordNode wordNode = node.getWordNode();
        wordNode.accept(this, p);
        for (SqlNode child : node.getChildren()) {
            child.accept(this, p);
        }
        return null;
    }

    @Override
    public Void visitFromClauseNode(FromClauseNode node, Context p) {
        WordNode wordNode = node.getWordNode();
        wordNode.accept(this, p);
        for (SqlNode child : node.getChildren()) {
            child.accept(this, p);
        }
        return null;
    }

    @Override
    public Void visitWhereClauseNode(WhereClauseNode node, Context p) {
        handleConditionalClauseNode(node, p);
        return null;
    }

    @Override
    public Void visitGroupByClauseNode(GroupByClauseNode node, Context p) {
        WordNode wordNode = node.getWordNode();
        wordNode.accept(this, p);
        for (SqlNode child : node.getChildren()) {
            child.accept(this, p);
        }
        return null;
    }

    @Override
    public Void visitHavingClauseNode(HavingClauseNode node, Context p) {
        handleConditionalClauseNode(node, p);
        return null;
    }

    @Override
    public Void visitOrderByClauseNode(OrderByClauseNode node, Context p) {
        WordNode wordNode = node.getWordNode();
        wordNode.accept(this, p);
        for (SqlNode child : node.getChildren()) {
            child.accept(this, p);
        }
        return null;
    }

    @Override
    public Void visitForUpdateClauseNode(ForUpdateClauseNode node, Context p) {
        WordNode wordNode = node.getWordNode();
        wordNode.accept(this, p);
        for (SqlNode child : node.getChildren()) {
            child.accept(this, p);
        }
        return null;
    }

    protected void handleConditionalClauseNode(ClauseNode node, Context p) {
        Context context = new Context();
        for (SqlNode child : node.getChildren()) {
            child.accept(this, context);
        }
        if (context.isAvailable()) {
            node.getWordNode().accept(this, p);
            p.setAvailable(true);
            p.appendRawSql(context.getSqlBuf());
            p.appendFormattedSql(context.getFormattedSqlBuf());
            p.addAllParameters(context.getParameters());
        }
    }

    @Override
    public Void visitLogicalOperatorNode(LogicalOperatorNode node, Context p) {
        if (p.isAvailable()) {
            WordNode wordNode = node.getWordNode();
            wordNode.accept(this, p);
        }
        for (SqlNode child : node.getChildren()) {
            child.accept(this, p);
        }
        return null;
    }

    @Override
    public Void visitWordNode(WordNode node, Context p) {
        p.setAvailable(true);
        String word = node.getWord();
        p.appendRawSql(word);
        p.appendFormattedSql(word);
        return null;
    }

    @Override
    public Void visitFragmentNode(FragmentNode node, Context p) {
        p.setAvailable(true);
        String fragment = node.getFragment();
        p.appendRawSql(fragment);
        p.appendFormattedSql(fragment);
        return null;
    }

    @Override
    public Void visitParensNode(ParensNode node, Context p) {
        if (node.isAttachedWithBindVariable()) {
            return null;
        }
        OtherNode openedFragmentNode = node.getOpenedFragmentNode();
        openedFragmentNode.accept(this, p);
        for (SqlNode child : node.getChildren()) {
            child.accept(this, p);
        }
        OtherNode closedFragmentNode = node.getClosedFragmentNode();
        closedFragmentNode.accept(this, p);
        return null;
    }

    @Override
    public Void visitUnknownNode(SqlNode node, Context p) {
        throw new DomaUnsupportedOperationException(getClass().getName(),
                "visitUnknownNode");
    }

    protected EvaluationResult evaluate(SqlLocation location, String expression) {
        try {
            ExpressionParser parser = new ExpressionParser(expression);
            ExpressionNode expressionNode = parser.parse();
            return evaluator.evaluate(expressionNode);
        } catch (ExpressionException e) {
            throw new JdbcException(DomaMessageCode.DOMA2111, e, location.getSql(),
                    location.getLineNumber(), location.getPosition(), e);
        }
    }

    protected class Context {

        private final StringBuilder rawSqlBuf = new StringBuilder(200);

        private final StringBuilder formattedSqlBuf = new StringBuilder(200);

        private final List<PreparedSqlParameter> parameters = new ArrayList<PreparedSqlParameter>();

        private boolean available;

        protected void appendRawSql(CharSequence sql) {
            rawSqlBuf.append(sql);
        }

        protected void appendFormattedSql(CharSequence sql) {
            formattedSqlBuf.append(sql);
        }

        protected void cutBackSqlBuf(int size) {
            rawSqlBuf.setLength(rawSqlBuf.length() - size);
        }

        protected void cutBackFormattedSqlBuf(int size) {
            formattedSqlBuf.setLength(formattedSqlBuf.length() - size);
        }

        protected CharSequence getSqlBuf() {
            return rawSqlBuf;
        }

        protected CharSequence getFormattedSqlBuf() {
            return formattedSqlBuf;
        }

        protected void addBindValue(Domain<?, ?> value) {
            parameters.add(new InParameter(value));
            rawSqlBuf.append("?");
            formattedSqlBuf.append(value.accept(config.dialect()
                    .getSqlLogFormattingVisitor(), formattingFunction));
        }

        protected void addAllParameters(List<PreparedSqlParameter> values) {
            parameters.addAll(values);
        }

        protected List<PreparedSqlParameter> getParameters() {
            return parameters;
        }

        void setAvailable(boolean available) {
            this.available = available;
        }

        boolean isAvailable() {
            return available;
        }

        @Override
        public String toString() {
            return rawSqlBuf.toString();
        }
    }
}
