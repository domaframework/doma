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

import org.seasar.doma.internal.jdbc.sql.node.AnonymousNode;
import org.seasar.doma.internal.jdbc.sql.node.BindVariableNode;
import org.seasar.doma.internal.jdbc.sql.node.BlockNode;
import org.seasar.doma.internal.jdbc.sql.node.DirectiveNode;
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
import org.seasar.doma.internal.jdbc.sql.node.HasNextNode;
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
import org.seasar.doma.internal.message.DomaMessageCode;
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
            case FOR_BLOCK_COMMENT: {
                parseForBlockComment();
                break;
            }
            case HAS_NEXT_LINE_COMMENT: {
                parseHasNextLineComment();
                break;
            }
            case EOL: {
                parseEOL();
                break;
            }
            case DELIMITER:
            case EOF: {
                validateTermination();
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

    protected void parseOpenedParens() {
        ParensNode parensNode = new ParensNode();
        addNode(parensNode);
        push(parensNode);
    }

    protected void parseClosedParens() {
        if (!isInParensNode()) {
            throw new JdbcException(DomaMessageCode.DOMA2109, sql, tokenizer
                    .getLineNumber(), tokenizer.getPosition());
        }
        removeNodesTo(ParensNode.class);
        ParensNode parensNode = pop();
        parensNode.close();
    }

    protected void parseBindVariableBlockComment() {
        String varialbeName = tokenType.extract(token);
        if (varialbeName.isEmpty()) {
            throw new JdbcException(DomaMessageCode.DOMA2120, sql, tokenizer
                    .getLineNumber(), tokenizer.getPosition(), token);
        }
        BindVariableNode node = new BindVariableNode(getLocation(),
                varialbeName, token);
        addNode(node);
        push(node);
    }

    protected void parseEmbeddedVariableBlockComment() {
        String varialbeName = tokenType.extract(token);
        if (varialbeName.isEmpty()) {
            throw new JdbcException(DomaMessageCode.DOMA2121, sql, tokenizer
                    .getLineNumber(), tokenizer.getPosition(), token);
        }
        EmbeddedVariableNode node = new EmbeddedVariableNode(getLocation(),
                varialbeName, token);
        addNode(node);
        push(node);
    }

    protected void parseIfBlockComment() {
        removePrecedentSpaces();
        IfBlockNode ifBlockNode = new IfBlockNode();
        addNode(ifBlockNode);
        push(ifBlockNode);
        String expression = tokenType.extract(token);
        IfNode ifNode = new IfNode(getLocation(), expression, token);
        ifBlockNode.setIfNode(ifNode);
        push(ifNode);
    }

    protected void parseElseifLineComment() {
        if (!isInIfBlockNode()) {
            throw new JdbcException(DomaMessageCode.DOMA2106, sql, tokenizer
                    .getLineNumber(), tokenizer.getPosition());
        }
        removeNodesTo(IfBlockNode.class);
        IfBlockNode ifBlockNode = peek();
        if (ifBlockNode.isElseNodeExistent()) {
            throw new JdbcException(DomaMessageCode.DOMA2108, sql, tokenizer
                    .getLineNumber(), tokenizer.getPosition());
        }
        String expression = tokenType.extract(token);
        ElseifNode node = new ElseifNode(getLocation(), expression, token);
        ifBlockNode.addElseifNode(node);
        push(node);
    }

    protected void parseElseLineComment() {
        if (!isInIfBlockNode()) {
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
        if (!isInBlockNode()) {
            throw new JdbcException(DomaMessageCode.DOMA2104, sql, tokenizer
                    .getLineNumber(), tokenizer.getPosition());
        }
        removePrecedentSpaces();
        removeNodesTo(BlockNode.class);
        BlockNode blockNode = pop();
        EndNode node = new EndNode(token);
        blockNode.setEndNode(node);
        push(node);
    }

    protected void parseForBlockComment() {
        removePrecedentSpaces();
        ForBlockNode forBlockNode = new ForBlockNode();
        addNode(forBlockNode);
        push(forBlockNode);
        String expr = tokenType.extract(token);
        int pos = expr.indexOf(":");
        if (pos == -1) {
            throw new JdbcException(DomaMessageCode.DOMA2124, sql, tokenizer
                    .getLineNumber(), tokenizer.getPosition());
        }
        String identifier = expr.substring(0, pos).trim();
        if (identifier.isEmpty()) {
            throw new JdbcException(DomaMessageCode.DOMA2125, sql, tokenizer
                    .getLineNumber(), tokenizer.getPosition());
        }
        String expression = expr.substring(pos + 1).trim();
        if (expression.isEmpty()) {
            throw new JdbcException(DomaMessageCode.DOMA2126, sql, tokenizer
                    .getLineNumber(), tokenizer.getPosition());
        }
        ForNode forNode = new ForNode(getLocation(), identifier, expression,
                token);
        forBlockNode.setForNode(forNode);
        push(forNode);
    }

    protected void parseHasNextLineComment() {
        if (!isInForBlockNode()) {
            throw new JdbcException(DomaMessageCode.DOMA2127, sql, tokenizer
                    .getLineNumber(), tokenizer.getPosition());
        }
        removeNodesTo(ForBlockNode.class);
        ForBlockNode forBlockNode = peek();
        if (forBlockNode.isHasNextNodeExistent()) {
            throw new JdbcException(DomaMessageCode.DOMA2128, sql, tokenizer
                    .getLineNumber(), tokenizer.getPosition());
        }
        String expression = tokenType.extract(token);
        HasNextNode node = new HasNextNode(getLocation(), expression, token);
        forBlockNode.setHasNextNode(node);
        push(node);
    }

    protected void parseEOL() {
        if (isAfterDirectiveNode()) {
            DirectiveNode directiveNode = peek();
            if (containsOnlySpaces(directiveNode)) {
                directiveNode.clearChildren();
                return;
            }
        }
        EolNode node = new EolNode(token);
        addNode(node);
        push(node);
    }

    protected boolean containsOnlySpaces(SqlNode node) {
        for (SqlNode child : node.getChildren()) {
            if (child != OtherNode.SPACE && child != OtherNode.TAB) {
                return false;
            }
        }
        return true;
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

    protected void removePrecedentSpaces() {
        if (isAfterEolNode()) {
            EolNode eolNode = peek();
            if (containsOnlySpaces(eolNode)) {
                eolNode.clearChildren();
            }
        } else if (isAfterDirectiveNode()) {
            DirectiveNode directiveNode = peek();
            if (containsOnlySpaces(directiveNode)) {
                directiveNode.clearChildren();
            }
        }
    }

    protected boolean isInSelectStatementNode() {
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

    protected boolean isInIfBlockNode() {
        for (SqlNode node : nodeStack) {
            if (IfBlockNode.class.isInstance(node)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isInForBlockNode() {
        for (SqlNode node : nodeStack) {
            if (ForBlockNode.class.isInstance(node)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isInParensNode() {
        for (SqlNode node : nodeStack) {
            if (ParensNode.class.isInstance(node)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isInBlockNode() {
        for (SqlNode node : nodeStack) {
            if (BlockNode.class.isInstance(node)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isAfterEolNode() {
        return EolNode.class.isInstance(peek());
    }

    protected boolean isAfterDirectiveNode() {
        return DirectiveNode.class.isInstance(peek());
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
                throw new JdbcException(DomaMessageCode.DOMA2110, sql,
                        tokenizer.getLineNumber(), tokenizer.getPosition(),
                        bindVariableNode.getText());
            }
        } else {
            peek().addNode(node);
        }
    }

    protected void validateTermination() {
        if (isAfterBindVariable()) {
            BindVariableNode bindVariableNode = pop();
            throw new JdbcException(DomaMessageCode.DOMA2110, sql, tokenizer
                    .getLineNumber(), tokenizer.getPosition(), bindVariableNode
                    .getText());
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
