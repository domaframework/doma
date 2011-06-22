/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.seasar.doma.internal.jdbc.sql.node.AnonymousNode;
import org.seasar.doma.internal.jdbc.sql.node.BindVariableNode;
import org.seasar.doma.internal.jdbc.sql.node.BlockNode;
import org.seasar.doma.internal.jdbc.sql.node.CommentNode;
import org.seasar.doma.internal.jdbc.sql.node.ElseNode;
import org.seasar.doma.internal.jdbc.sql.node.ElseifNode;
import org.seasar.doma.internal.jdbc.sql.node.EmbeddedVariableNode;
import org.seasar.doma.internal.jdbc.sql.node.EndNode;
import org.seasar.doma.internal.jdbc.sql.node.EolNode;
import org.seasar.doma.internal.jdbc.sql.node.ForBlockNode;
import org.seasar.doma.internal.jdbc.sql.node.ForNode;
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
import org.seasar.doma.internal.jdbc.sql.node.WhitespaceNode;
import org.seasar.doma.internal.jdbc.sql.node.WordNode;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class SqlParser {

    protected static final Pattern LITERAL_PATTERN = Pattern.compile(
            "[-+'.0-9]|.*'|true|false|null", Pattern.CASE_INSENSITIVE);

    protected final Deque<SqlNode> nodeStack = new LinkedList<SqlNode>();

    protected final String sql;

    protected final SqlTokenizer tokenizer;

    protected final AnonymousNode rootNode;

    protected SqlTokenType tokenType;

    protected String token;

    public SqlParser(String sql) {
        assertNotNull(sql);
        this.sql = sql;
        tokenizer = new SqlTokenizer(sql);
        rootNode = new AnonymousNode();
        nodeStack.push(rootNode);
    }

    public SqlNode parse() {
        outer: for (;;) {
            tokenType = tokenizer.next();
            token = tokenizer.getToken();
            switch (tokenType) {
            case WHITESPACE: {
                parseWhitespace();
                break;
            }
            case WORD:
            case QUOTE: {
                parseWord();
                break;
            }
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
            case OPENED_PARENS: {
                parseOpenedParens();
                break;
            }
            case CLOSED_PARENS: {
                parseClosedParens();
                break;
            }
            case BIND_VARIABLE_BLOCK_COMMENT: {
                parseBindVariableBlockComment();
                break;
            }
            case EMBEDDED_VARIABLE_BLOCK_COMMENT: {
                parseEmbeddedVariableBlockComment();
                break;
            }
            case IF_BLOCK_COMMENT: {
                parseIfBlockComment();
                break;
            }
            case ELSEIF_BLOCK_COMMENT: {
                parseElseifBlockComment();
                break;
            }
            case ELSEIF_LINE_COMMENT: {
                parseElseifLineComment();
                break;
            }
            case ELSE_BLOCK_COMMENT: {
                parseElseBlockComment();
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
            case FOR_BLOCK_COMMENT: {
                parseForBlockComment();
                break;
            }
            case UNION_WORD:
            case EXCEPT_WORD:
            case MINUS_WORD:
            case INTERSECT_WORD: {
                parseSetOperatorWord();
                break;
            }
            case BLOCK_COMMENT:
            case LINE_COMMENT: {
                parseComment();
                break;
            }
            case OTHER: {
                parseOther();
                break;
            }
            case EOL: {
                parseEOL();
                break;
            }
            case DELIMITER:
            case EOF: {
                break outer;
            }
            default: {
                assertUnreachable();
                break;
            }
            }
        }
        validate();
        validateParensClosed();
        return rootNode;
    }

    protected void parseSetOperatorWord() {
        validate();
        AnonymousNode node = new AnonymousNode();
        node.addNode(new WordNode(token));
        if (isInSelectStatementNode()) {
            removeNodesTo(SelectStatementNode.class);
            pop();
        }
        addNode(node);
        push(node);
    }

    protected void parseSelectWord() {
        validate();
        SelectStatementNode selectStatementNode = new SelectStatementNode();
        addNode(selectStatementNode);
        push(selectStatementNode);
        SelectClauseNode selectClauseNode = new SelectClauseNode(token);
        selectStatementNode.setSelectClauseNode(selectClauseNode);
        push(selectClauseNode);
    }

    protected void parseFromWord() {
        validate();
        FromClauseNode node = new FromClauseNode(token);
        if (isInSelectStatementNode()) {
            removeNodesTo(SelectStatementNode.class);
            SelectStatementNode selectStatementNode = peek();
            selectStatementNode.setFromClauseNode(node);
        } else {
            addNode(node);
        }
        push(node);
    }

    protected void parseWhereWord() {
        validate();
        WhereClauseNode node = new WhereClauseNode(token);
        if (isInSelectStatementNode()) {
            removeNodesTo(SelectStatementNode.class);
            SelectStatementNode selectStatementNode = peek();
            selectStatementNode.setWhereClauseNode(node);
        } else {
            addNode(node);
        }
        push(node);
    }

    protected void parseGroupByWord() {
        validate();
        GroupByClauseNode node = new GroupByClauseNode(token);
        if (isInSelectStatementNode()) {
            removeNodesTo(SelectStatementNode.class);
            SelectStatementNode selectStatementNode = peek();
            selectStatementNode.setGroupByClauseNode(node);
        } else {
            addNode(node);
        }
        push(node);
    }

    protected void parseHavingWord() {
        validate();
        HavingClauseNode node = new HavingClauseNode(token);
        if (isInSelectStatementNode()) {
            removeNodesTo(SelectStatementNode.class);
            SelectStatementNode selectStatementNode = peek();
            selectStatementNode.setHavingClauseNode(node);
        } else {
            addNode(node);
        }
        push(node);
    }

    protected void parseOrderByWord() {
        validate();
        OrderByClauseNode node = new OrderByClauseNode(token);
        if (isInSelectStatementNode()) {
            removeNodesTo(SelectStatementNode.class);
            SelectStatementNode selectStatementNode = peek();
            selectStatementNode.setOrderByClauseNode(node);
        } else {
            addNode(node);
        }
        push(node);
    }

    protected void parseForUpdateWord() {
        validate();
        ForUpdateClauseNode node = new ForUpdateClauseNode(token);
        if (isInSelectStatementNode()) {
            removeNodesTo(SelectStatementNode.class);
            SelectStatementNode selectStatementNode = peek();
            selectStatementNode.setForUpdateClauseNode(node);
        } else {
            addNode(node);
        }
        push(node);
    }

    protected void parseLogicalWord() {
        String word = tokenType.extract(token);
        LogicalOperatorNode node = new LogicalOperatorNode(word);
        addNode(node);
        push(node);
    }

    protected void parseWord() {
        WordNode node = new WordNode(token);
        addNode(node);
    }

    protected void parseComment() {
        CommentNode node = new CommentNode(token);
        addNode(node);
    }

    protected void parseOpenedParens() {
        ParensNode parensNode = new ParensNode(getLocation());
        addNode(parensNode);
        push(parensNode);
    }

    protected void parseClosedParens() {
        if (!isInParensNode()) {
            throw new JdbcException(Message.DOMA2109, sql,
                    tokenizer.getLineNumber(), tokenizer.getPosition());
        }
        validate();
        removeNodesTo(ParensNode.class);
        ParensNode parensNode = pop();
        for (SqlNode child : parensNode.getChildren()) {
            if (!(child instanceof WhitespaceNode)
                    && !(child instanceof CommentNode)) {
                parensNode.setEmpty(false);
                break;
            }
        }
        parensNode.close();
    }

    protected void parseBindVariableBlockComment() {
        String varialbeName = tokenType.extract(token);
        if (varialbeName.isEmpty()) {
            throw new JdbcException(Message.DOMA2120, sql,
                    tokenizer.getLineNumber(), tokenizer.getPosition(), token);
        }
        BindVariableNode node = new BindVariableNode(getLocation(),
                varialbeName, token);
        addNode(node);
        push(node);
    }

    protected void parseEmbeddedVariableBlockComment() {
        String varialbeName = tokenType.extract(token);
        if (varialbeName.isEmpty()) {
            throw new JdbcException(Message.DOMA2121, sql,
                    tokenizer.getLineNumber(), tokenizer.getPosition(), token);
        }
        EmbeddedVariableNode node = new EmbeddedVariableNode(getLocation(),
                varialbeName, token);
        addNode(node);
        push(node);
    }

    protected void parseIfBlockComment() {
        IfBlockNode ifBlockNode = new IfBlockNode();
        addNode(ifBlockNode);
        push(ifBlockNode);
        String expression = tokenType.extract(token);
        IfNode ifNode = new IfNode(getLocation(), expression, token);
        ifBlockNode.setIfNode(ifNode);
        push(ifNode);
    }

    protected void parseElseifBlockComment() {
        if (!isInIfBlockNode()) {
            throw new JdbcException(Message.DOMA2138, sql,
                    tokenizer.getLineNumber(), tokenizer.getPosition());
        }
        removeNodesTo(IfBlockNode.class);
        IfBlockNode ifBlockNode = peek();
        if (ifBlockNode.isElseNodeExistent()) {
            throw new JdbcException(Message.DOMA2139, sql,
                    tokenizer.getLineNumber(), tokenizer.getPosition());
        }
        String expression = tokenType.extract(token);
        ElseifNode node = new ElseifNode(getLocation(), expression, token);
        ifBlockNode.addElseifNode(node);
        push(node);
    }

    protected void parseElseifLineComment() {
        if (!isInIfBlockNode()) {
            throw new JdbcException(Message.DOMA2106, sql,
                    tokenizer.getLineNumber(), tokenizer.getPosition());
        }
        removeNodesTo(IfBlockNode.class);
        IfBlockNode ifBlockNode = peek();
        if (ifBlockNode.isElseNodeExistent()) {
            throw new JdbcException(Message.DOMA2108, sql,
                    tokenizer.getLineNumber(), tokenizer.getPosition());
        }
        String expression = tokenType.extract(token);
        ElseifNode node = new ElseifNode(getLocation(), expression, token);
        ifBlockNode.addElseifNode(node);
        push(node);
    }

    protected void parseElseBlockComment() {
        if (!isInIfBlockNode()) {
            throw new JdbcException(Message.DOMA2140, sql,
                    tokenizer.getLineNumber(), tokenizer.getPosition());
        }
        removeNodesTo(IfBlockNode.class);
        IfBlockNode ifBlockNode = peek();
        if (ifBlockNode.isElseNodeExistent()) {
            throw new JdbcException(Message.DOMA2141, sql,
                    tokenizer.getLineNumber(), tokenizer.getPosition());
        }
        ElseNode node = new ElseNode(token);
        ifBlockNode.setElseNode(node);
        push(node);
    }

    protected void parseElseLineComment() {
        if (!isInIfBlockNode()) {
            throw new JdbcException(Message.DOMA2105, sql,
                    tokenizer.getLineNumber(), tokenizer.getPosition());
        }
        removeNodesTo(IfBlockNode.class);
        IfBlockNode ifBlockNode = peek();
        if (ifBlockNode.isElseNodeExistent()) {
            throw new JdbcException(Message.DOMA2107, sql,
                    tokenizer.getLineNumber(), tokenizer.getPosition());
        }
        ElseNode node = new ElseNode(token);
        ifBlockNode.setElseNode(node);
        push(node);
    }

    protected void parseEndBlockComment() {
        if (!isInBlockNode()) {
            throw new JdbcException(Message.DOMA2104, sql,
                    tokenizer.getLineNumber(), tokenizer.getPosition());
        }
        removeNodesTo(BlockNode.class);
        BlockNode blockNode = pop();
        EndNode node = new EndNode(token);
        blockNode.setEndNode(node);
        push(node);
    }

    protected void parseForBlockComment() {
        ForBlockNode forBlockNode = new ForBlockNode();
        addNode(forBlockNode);
        push(forBlockNode);
        String expr = tokenType.extract(token);
        int pos = expr.indexOf(":");
        if (pos == -1) {
            throw new JdbcException(Message.DOMA2124, sql,
                    tokenizer.getLineNumber(), tokenizer.getPosition());
        }
        String identifier = expr.substring(0, pos).trim();
        if (identifier.isEmpty()) {
            throw new JdbcException(Message.DOMA2125, sql,
                    tokenizer.getLineNumber(), tokenizer.getPosition());
        }
        String expression = expr.substring(pos + 1).trim();
        if (expression.isEmpty()) {
            throw new JdbcException(Message.DOMA2126, sql,
                    tokenizer.getLineNumber(), tokenizer.getPosition());
        }
        ForNode forNode = new ForNode(getLocation(), identifier, expression,
                token);
        forBlockNode.setForNode(forNode);
        push(forNode);
    }

    protected void parseOther() {
        addNode(OtherNode.of(token));
    }

    protected void parseEOL() {
        EolNode node = new EolNode(token);
        addNode(node);
    }

    protected boolean containsOnlyWhitespaces(SqlNode node) {
        for (SqlNode child : node.getChildren()) {
            if (!(child instanceof WhitespaceNode)) {
                return false;
            }
        }
        return true;
    }

    protected void parseWhitespace() {
        addNode(WhitespaceNode.of(token));
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

    protected boolean isInSelectStatementNode() {
        for (SqlNode node : nodeStack) {
            if (node instanceof ParensNode) {
                return false;
            }
            if (node instanceof SelectStatementNode) {
                return true;
            }
        }
        return false;
    }

    protected boolean isInIfBlockNode() {
        for (SqlNode node : nodeStack) {
            if (node instanceof ParensNode) {
                return false;
            }
            if (node instanceof IfBlockNode) {
                return true;
            }
        }
        return false;
    }

    protected boolean isInForBlockNode() {
        for (SqlNode node : nodeStack) {
            if (node instanceof ParensNode) {
                return false;
            }
            if (node instanceof ForBlockNode) {
                return true;
            }
        }
        return false;
    }

    protected boolean isInParensNode() {
        for (SqlNode node : nodeStack) {
            if (node instanceof ParensNode) {
                return true;
            }
        }
        return false;
    }

    protected boolean isInBlockNode() {
        for (SqlNode node : nodeStack) {
            if (node instanceof ParensNode) {
                return false;
            }
            if (node instanceof BlockNode) {
                return true;
            }
        }
        return false;
    }

    protected boolean isAfterBindVariableNode() {
        return peek() instanceof BindVariableNode;
    }

    @SuppressWarnings("deprecation")
    protected void addNode(SqlNode node) {
        if (isAfterBindVariableNode()) {
            BindVariableNode bindVariableNode = pop();
            if (node instanceof WordNode) {
                WordNode wordNode = (WordNode) node;
                String word = wordNode.getWord();
                Matcher matcher = LITERAL_PATTERN.matcher(word);
                if (matcher.lookingAt()) {
                    bindVariableNode.setWordNode(wordNode);
                } else {
                    throw new JdbcException(Message.DOMA2142, sql,
                            tokenizer.getLineNumber(), tokenizer.getPosition(),
                            bindVariableNode.getText(), word);
                }
            } else if (node instanceof ParensNode) {
                ParensNode parensNode = (ParensNode) node;
                parensNode.setAttachedWithBindVariable(true);
                bindVariableNode.setParensNode(parensNode);
            } else {
                throw new JdbcException(Message.DOMA2110, sql,
                        tokenizer.getLineNumber(), tokenizer.getPosition(),
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
        return new SqlLocation(sql, tokenizer.getLineNumber(),
                tokenizer.getPosition());
    }

    protected void validate() {
        if (isAfterBindVariableNode()) {
            BindVariableNode bindVariableNode = pop();
            throw new JdbcException(Message.DOMA2110, sql,
                    tokenizer.getLineNumber(), tokenizer.getPosition(),
                    bindVariableNode.getText());
        }
        if (isInIfBlockNode()) {
            removeNodesTo(IfBlockNode.class);
            IfBlockNode ifBlockNode = pop();
            SqlLocation location = ifBlockNode.getIfNode().getLocation();
            throw new JdbcException(Message.DOMA2133, sql,
                    location.getLineNumber(), location.getPosition());
        }
        if (isInForBlockNode()) {
            removeNodesTo(ForBlockNode.class);
            ForBlockNode forBlockNode = pop();
            SqlLocation location = forBlockNode.getForNode().getLocation();
            throw new JdbcException(Message.DOMA2134, sql,
                    location.getLineNumber(), location.getPosition());
        }
    }

    protected void validateParensClosed() {
        if (isInParensNode()) {
            removeNodesTo(ParensNode.class);
            ParensNode parensNode = pop();
            SqlLocation location = parensNode.getLocation();
            throw new JdbcException(Message.DOMA2135, sql,
                    location.getLineNumber(), location.getPosition());
        }
    }

}
