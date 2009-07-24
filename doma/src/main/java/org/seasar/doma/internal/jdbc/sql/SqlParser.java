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

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import org.seasar.doma.DomaMessageCode;
import org.seasar.doma.internal.jdbc.sql.node.AnonymousNode;
import org.seasar.doma.internal.jdbc.sql.node.BindVariableNode;
import org.seasar.doma.internal.jdbc.sql.node.ElseNode;
import org.seasar.doma.internal.jdbc.sql.node.ElseifNode;
import org.seasar.doma.internal.jdbc.sql.node.EndNode;
import org.seasar.doma.internal.jdbc.sql.node.ForUpdateClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.FromClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.GroupByClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.HavingClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.IfBlockNode;
import org.seasar.doma.internal.jdbc.sql.node.IfNode;
import org.seasar.doma.internal.jdbc.sql.node.LogicalOperatorNode;
import org.seasar.doma.internal.jdbc.sql.node.OrderByClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.OtherNode;
import org.seasar.doma.internal.jdbc.sql.node.ParensNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNode;
import org.seasar.doma.internal.jdbc.sql.node.SqlLocation;
import org.seasar.doma.internal.jdbc.sql.node.WhereClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.WordNode;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SqlNode;


/**
 * @author taedium
 * 
 */
public class SqlParser {

    protected final Deque<SqlNode> nodeStack = new LinkedList<SqlNode>();

    protected final String sql;

    protected final SqlTokenizer tokenizer;

    protected final AnonymousNode anonymousNode;

    protected SqlTokenType tokenType;

    protected String token;

    public SqlParser(String sql) {
        assertNotNull(sql);
        this.sql = sql;
        tokenizer = new SqlTokenizer(sql);
        anonymousNode = new AnonymousNode();
        nodeStack.push(anonymousNode);
    }

    public SqlNode parse() {
        outer: for (;;) {
            tokenType = tokenizer.next();
            token = tokenizer.getToken();
            switch (tokenType) {
            case SELECT_WORD: {
                parseSelectWord();
                break;
            }
            case FROM_WORD: {
                parseFromWord();
                break;
            }
            case WHERE_WORD: {
                parseWhereWord();
                break;
            }
            case GROUP_BY_WORD: {
                parseGroupByWord();
                break;
            }
            case HAVING_WORD: {
                parseHavingWord();
                break;
            }
            case ORDER_BY_WORD: {
                parseOrderByWord();
                break;
            }
            case FOR_UPDATE_WORD: {
                parseForUpdateWord();
                break;
            }
            case AND_WORD:
            case OR_WORD: {
                parseLogicalWord();
                break;
            }
            case WORD:
            case QUOTE: {
                parseWord();
                break;
            }
            case OPENED_PARENS: {
                parseOpenedParens();
                break;
            }
            case CLOSED_PARENS: {
                parseClosedParens();
                break;
            }
            case BIND_BLOCK_COMMENT: {
                parseBindBlockComment();
                break;
            }
            case IF_BLOCK_COMMENT: {
                parseIfBlockComment();
                break;
            }
            case ELSEIF_LINE_COMMENT: {
                parseElseifLineComment();
                break;
            }
            case ELSE_LINE_COMMENT: {
                parseElseLineComment();
                break;
            }
            case END_BLOCK_COMMENT: {
                parseEndBlockComment();
                break;
            }
            case EOL: {
                break;
            }
            case DELIMITER:
            case EOF: {
                break outer;
            }
            default: {
                appendOther(token);
                break;
            }
            }
        }
        return anonymousNode;
    }

    protected void parseSelectWord() {
        SelectStatementNode selectStatementNode = new SelectStatementNode();
        addNode(selectStatementNode);
        push(selectStatementNode);
        SelectClauseNode selectClauseNode = new SelectClauseNode(token);
        selectStatementNode.setSelectClauseNode(selectClauseNode);
        push(selectClauseNode);
    }

    protected void parseFromWord() {
        FromClauseNode node = new FromClauseNode(token);
        if (isInSelectStatement()) {
            removeNodesTo(SelectStatementNode.class);
            SelectStatementNode selectStatementNode = peek();
            selectStatementNode.setFromClauseNode(node);
        } else {
            addNode(node);
        }
        push(node);
    }

    protected void parseWhereWord() {
        WhereClauseNode node = new WhereClauseNode(token);
        if (isInSelectStatement()) {
            removeNodesTo(SelectStatementNode.class);
            SelectStatementNode selectStatementNode = peek();
            selectStatementNode.setWhereClauseNode(node);
        } else {
            addNode(node);
        }
        push(node);
    }

    protected void parseGroupByWord() {
        GroupByClauseNode node = new GroupByClauseNode(token);
        if (isInSelectStatement()) {
            removeNodesTo(SelectStatementNode.class);
            SelectStatementNode selectStatementNode = peek();
            selectStatementNode.setGroupByClauseNode(node);
        } else {
            addNode(node);
        }
        push(node);
    }

    protected void parseHavingWord() {
        HavingClauseNode node = new HavingClauseNode(token);
        if (isInSelectStatement()) {
            removeNodesTo(SelectStatementNode.class);
            SelectStatementNode selectStatementNode = peek();
            selectStatementNode.setHavingClauseNode(node);
        } else {
            addNode(node);
        }
        push(node);
    }

    protected void parseOrderByWord() {
        OrderByClauseNode node = new OrderByClauseNode(token);
        if (isInSelectStatement()) {
            removeNodesTo(SelectStatementNode.class);
            SelectStatementNode selectStatementNode = peek();
            selectStatementNode.setOrderByClauseNode(node);
        } else {
            addNode(node);
        }
        push(node);
    }

    protected void parseForUpdateWord() {
        ForUpdateClauseNode node = new ForUpdateClauseNode(token);
        if (isInSelectStatement()) {
            removeNodesTo(SelectStatementNode.class);
            SelectStatementNode selectStatementNode = peek();
            selectStatementNode.setForUpdateClauseNode(node);
        } else {
            addNode(node);
        }
        push(node);
    }

    protected void parseLogicalWord() {
        String word = tokenType.extractExpression(token);
        LogicalOperatorNode node = new LogicalOperatorNode(word);
        addNode(node);
        push(node);
    }

    protected void parseWord() {
        WordNode node = new WordNode(token);
        addNode(node);
    }

    protected void parseOpenedParens() {
        ParensNode parensNode = new ParensNode();
        addNode(parensNode);
        push(parensNode);
    }

    protected void parseClosedParens() {
        if (!isInParens()) {
            throw new JdbcException(DomaMessageCode.DOMA2109, sql, tokenizer
                    .getLineNumber(), tokenizer.getPosition());
        }
        removeNodesTo(ParensNode.class);
        ParensNode parensNode = pop();
        parensNode.close();
    }

    protected void parseBindBlockComment() {
        String varialbeName = tokenType.extractExpression(token);
        BindVariableNode node = new BindVariableNode(getLocation(),
                varialbeName, token);
        addNode(node);
        push(node);
    }

    protected void parseIfBlockComment() {
        IfBlockNode ifBlockNode = new IfBlockNode();
        addNode(ifBlockNode);
        push(ifBlockNode);
        String expression = tokenType.extractExpression(token);
        IfNode node = new IfNode(getLocation(), expression, token);
        ifBlockNode.setIfNode(node);
        push(node);
    }

    protected void parseElseifLineComment() {
        if (!isInIfBlock()) {
            throw new JdbcException(DomaMessageCode.DOMA2106, sql, tokenizer
                    .getLineNumber(), tokenizer.getPosition());
        }
        removeNodesTo(IfBlockNode.class);
        IfBlockNode ifBlockNode = peek();
        if (ifBlockNode.isElseNodeExistent()) {
            throw new JdbcException(DomaMessageCode.DOMA2108, sql, tokenizer
                    .getLineNumber(), tokenizer.getPosition());
        }
        String expression = tokenType.extractExpression(token);
        ElseifNode node = new ElseifNode(getLocation(), expression, token);
        ifBlockNode.addElseifNode(node);
        push(node);
    }

    protected void parseElseLineComment() {
        if (!isInIfBlock()) {
            throw new JdbcException(DomaMessageCode.DOMA2105, sql, tokenizer
                    .getLineNumber(), tokenizer.getPosition());
        }
        removeNodesTo(IfBlockNode.class);
        IfBlockNode ifBlockNode = peek();
        if (ifBlockNode.isElseNodeExistent()) {
            throw new JdbcException(DomaMessageCode.DOMA2107, sql, tokenizer
                    .getLineNumber(), tokenizer.getPosition());
        }
        ElseNode node = new ElseNode(token);
        ifBlockNode.setElseNode(node);
        push(node);
    }

    protected void parseEndBlockComment() {
        if (!isInIfBlock()) {
            throw new JdbcException(DomaMessageCode.DOMA2104, sql, tokenizer
                    .getLineNumber(), tokenizer.getPosition());
        }
        removeNodesTo(IfBlockNode.class);
        IfBlockNode ifBlockNode = pop();
        EndNode node = new EndNode(token);
        ifBlockNode.setEndNode(node);
    }

    protected void appendOther(String token) {
        addNode(OtherNode.of(token));
    }

    protected void removeNodesTo(Class<? extends SqlNode> clazz) {
        for (Iterator<SqlNode> it = nodeStack.iterator(); it.hasNext();) {
            SqlNode node = it.next();
            if (clazz.isInstance(node)) {
                break;
            }
            it.remove();
        }
    }

    protected boolean isInSelectStatement() {
        for (SqlNode node : nodeStack) {
            if (ParensNode.class.isInstance(node)) {
                return false;
            }
            if (SelectStatementNode.class.isInstance(node)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isInIfBlock() {
        for (SqlNode node : nodeStack) {
            if (IfBlockNode.class.isInstance(node)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isInParens() {
        for (SqlNode node : nodeStack) {
            if (ParensNode.class.isInstance(node)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isAfterBindVariable() {
        return BindVariableNode.class.isInstance(peek());
    }

    protected void addNode(SqlNode node) {
        if (isAfterBindVariable()) {
            BindVariableNode bindVariableNode = pop();
            if (WordNode.class.isInstance(node)) {
                WordNode wordNode = WordNode.class.cast(node);
                bindVariableNode.setWordNode(wordNode);
            } else if (ParensNode.class.isInstance(node)) {
                ParensNode parensNode = ParensNode.class.cast(node);
                parensNode.setAttachedWithBindVariable(true);
                bindVariableNode.setParensNode(parensNode);
            } else {
                throw new JdbcException(DomaMessageCode.DOMA2110, sql, tokenizer
                        .getLineNumber(), tokenizer.getPosition(),
                        bindVariableNode.getText());
            }
        } else {
            peek().addNode(node);
        }
    }

    protected void push(SqlNode node) {
        nodeStack.push(node);
    }

    @SuppressWarnings("unchecked")
    protected <T extends SqlNode> T peek() {
        return (T) nodeStack.peek();
    }

    @SuppressWarnings("unchecked")
    protected <T extends SqlNode> T pop() {
        return (T) nodeStack.pop();
    }

    protected SqlLocation getLocation() {
        return new SqlLocation(sql, tokenizer.getLineNumber(), tokenizer
                .getPosition());
    }
}
